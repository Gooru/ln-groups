package org.gooru.groups.reports.classes.student.summary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.constants.StatusConstants;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


/**
 * @author renuka
 */
public class CompetencyCompletionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyCompletionService.class);
  private static final String COMPLETED_INFERRED = "completedInferred";
  private final ClassSummaryCompetencyMasteryDao classSummaryMasterydao;

  public CompetencyCompletionService(DBI dbi) {
    this.classSummaryMasterydao = dbi.onDemand(ClassSummaryCompetencyMasteryDao.class);
  }

  public void generateWeeklyCompetencyStatsInClass(ClassStudentSummaryBean bean, String userId,
      JsonObject weekData) {
    JsonArray masteredCompetencyList = new JsonArray();
    JsonArray completedCompetencyList = new JsonArray();
    JsonArray inferredCompetencyList = new JsonArray();
    JsonArray inprogressCompetencyList = new JsonArray();
    // Get completed competencies(with seq) from class in given period and using skyline
    // competencies seq compute inferred.
    List<CompetencyStatusModel> userCompetencyCompletionModels =
        this.classSummaryMasterydao.fetchCompetencyCompletionInClassInWeek(bean.getClassId(),
            bean.getTxSubjectCode(), userId, bean.getFromDate(), bean.getToDate());
    List<CompetencyStatusModel> userSkylineModels = classSummaryMasterydao
        .fetchUserSkylineTillToDate(userId, bean.getTxSubjectCode(), bean.getToDate());

    if (!userCompetencyCompletionModels.isEmpty() && !userSkylineModels.isEmpty()) {
      // generate unique competencies of completed, mastered and inprogress status.
      List<CompetencyStatusModel> completedOrMastered =
          segregateCompetencyCompletionStatus(masteredCompetencyList, inprogressCompetencyList,
              completedCompetencyList, userCompetencyCompletionModels);
      List<String> skylineCompletedCompetenciesInGivenPeriod =
          classSummaryMasterydao.fetchUserSkylineInGivenPeriod(userId, bean.getTxSubjectCode(),
              bean.getFromDate(), bean.getToDate());

      // compute inferred competencies
      Map<String, Set<String>> inferredCompetencyCompletedSet =
          computeInferred(inferredCompetencyList, userSkylineModels, completedOrMastered, true);
      // Remove inferred competencies which are completed in other period than requested period
      inferredCompetencyList =
          removeInferredCompetenciesCompletedInAnotherPeriod(inferredCompetencyList,
              skylineCompletedCompetenciesInGivenPeriod, inferredCompetencyCompletedSet);

      // since we are taking in class competency completion from evidence table, here we are
      // excluding inprogress competencies which are already completed.
      inprogressCompetencyList = removeUnwantedSetFromInputCompList(masteredCompetencyList,
          completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
      
      // here we are excluding inferred competencies which are already inferred in another period.
      Date tillDate = getPreviousDateOfGivenDate(bean.getFromDate());
      JsonObject skylineDataTillFromDate = new JsonObject();
      generateAllTimeCompetencyStatsInClass(bean, userId, skylineDataTillFromDate, tillDate);
      JsonArray inferredCompetencyListOfSkylineTillFromDate = new JsonArray();
      inferredCompetencyListOfSkylineTillFromDate = skylineDataTillFromDate.getJsonArray(Constants.Response.INFERRED);
      inferredCompetencyList = removeUnwantedSetFromInputCompList(inferredCompetencyListOfSkylineTillFromDate,
          null, null, inferredCompetencyList);
      // here we are excluding completed competencies which are already mastered.
      completedCompetencyList = removeUnwantedSetFromInputCompList(masteredCompetencyList,
          null, null, completedCompetencyList);
    }

    weekData.put(Constants.Response.MASTERED, masteredCompetencyList)
        .put(Constants.Response.COMPLETED, completedCompetencyList)
        .put(Constants.Response.INFERRED, inferredCompetencyList)
        .put(Constants.Response.IN_PROGRESS, inprogressCompetencyList);
  }

  public void generateAllTimeCompetencyStatsInClass(ClassStudentSummaryBean bean, String userId,
      JsonObject allTimeData, Date currentDate) {
    JsonArray masteredCompetencyList = new JsonArray();
    JsonArray completedCompetencyList = new JsonArray();
    JsonArray inferredCompetencyList = new JsonArray();
    JsonArray inprogressCompetencyList = new JsonArray();
    // Get completed competencies(with seq) from class all time and using skyline competencies seq
    // compute inferred.
    List<CompetencyStatusModel> userCompetencyCompletionModels =
        this.classSummaryMasterydao.fetchCompetencyCompletionInClassTillNow(bean.getClassId(),
            bean.getTxSubjectCode(), userId, currentDate);
    List<CompetencyStatusModel> userSkylineModels =
        classSummaryMasterydao.fetchUserSkyline(userId, bean.getTxSubjectCode());

    if (!userCompetencyCompletionModels.isEmpty() && !userSkylineModels.isEmpty()) {
      // generate unique competencies of completed, mastered and inprogress status.
      List<CompetencyStatusModel> completedOrMastered =
          segregateCompetencyCompletionStatus(masteredCompetencyList, inprogressCompetencyList,
              completedCompetencyList, userCompetencyCompletionModels);

      // compute inferred competencies
      computeInferred(inferredCompetencyList, userSkylineModels, completedOrMastered, false);
      // since we are taking in class competency completion from evidence table, here we are
      // excluding inprogress competencies which are already completed.
      inprogressCompetencyList = removeUnwantedSetFromInputCompList(masteredCompetencyList,
          completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
      // here we are excluding completed competencies which are already mastered.
      completedCompetencyList = removeUnwantedSetFromInputCompList(masteredCompetencyList,
          null, null, completedCompetencyList);
    }

    allTimeData.put(Constants.Response.MASTERED, masteredCompetencyList)
        .put(Constants.Response.COMPLETED, completedCompetencyList)
        .put(Constants.Response.INFERRED, inferredCompetencyList)
        .put(Constants.Response.IN_PROGRESS, inprogressCompetencyList);
  }


  public void generateWeeklyCompetencyStatsInSkyline(ClassStudentSummaryBean bean, String userId,
      JsonObject weekData) {
    JsonArray masteredCompetencyList = new JsonArray();
    JsonArray completedCompetencyList = new JsonArray();
    JsonArray inferredCompetencyList = new JsonArray();
    JsonArray inprogressCompetencyList = new JsonArray();

    // Get completed competencies(with seq) of skyline in given period and using all the
    // competencies seq in user skyline compute inferred.
    List<CompetencyStatusModel> userSkylineModels = classSummaryMasterydao
        .fetchUserSkylineTillToDate(userId, bean.getTxSubjectCode(), bean.getToDate());
    List<CompetencyStatusModel> userCompetencyCompletionModels =
        classSummaryMasterydao.fetchUserSkylineInWeek(userId, bean.getTxSubjectCode(),
            bean.getFromDate(), bean.getToDate());
    if (!userCompetencyCompletionModels.isEmpty() && !userSkylineModels.isEmpty()) {
      // generate unique competencies of completed, mastered and inprogress status.
      List<CompetencyStatusModel> completedOrMastered =
          segregateCompetencyCompletionStatus(masteredCompetencyList, inprogressCompetencyList,
              completedCompetencyList, userCompetencyCompletionModels);
      // compute inferred competencies
      computeInferred(inferredCompetencyList, userSkylineModels, completedOrMastered, true);
      
      // here we are excluding inprogress competencies which are already completed.
      inprogressCompetencyList = removeUnwantedSetFromInputCompList(masteredCompetencyList,
          completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
      
      // here we are excluding inferred competencies which are already inferred in another period.
      Date tillDate = getPreviousDateOfGivenDate(bean.getFromDate());
      JsonObject skylineDataTillFromDate = new JsonObject();
      generateAllTimeCompetencyStatsInSkyline(bean, userId, skylineDataTillFromDate, tillDate);
      JsonArray inferredCompetencyListOfSkylineTillFromDate = new JsonArray();
      inferredCompetencyListOfSkylineTillFromDate = skylineDataTillFromDate.getJsonArray(Constants.Response.INFERRED);
      inferredCompetencyList = removeUnwantedSetFromInputCompList(inferredCompetencyListOfSkylineTillFromDate,
          null, null, inferredCompetencyList);
      
      // here we are excluding completed competencies which are already mastered.
      completedCompetencyList = removeUnwantedSetFromInputCompList(masteredCompetencyList,
          null, null, completedCompetencyList);
    }

    weekData.put(Constants.Response.MASTERED, masteredCompetencyList)
        .put(Constants.Response.COMPLETED, completedCompetencyList)
        .put(Constants.Response.INFERRED, inferredCompetencyList)
        .put(Constants.Response.IN_PROGRESS, inprogressCompetencyList);
  }

  public void generateAllTimeCompetencyStatsInSkyline(ClassStudentSummaryBean bean, String userId,
      JsonObject allTimeData, Date currentDate) {
    JsonArray masteredCompetencyList = new JsonArray();
    JsonArray completedCompetencyList = new JsonArray();
    JsonArray inferredCompetencyList = new JsonArray();
    JsonArray inprogressCompetencyList = new JsonArray();

    // Get completed competencies(with seq) of skyline all time and using all the competencies seq
    // in user skyline compute inferred.
    List<CompetencyStatusModel> userSkylineModels =
        classSummaryMasterydao.fetchUserSkyline(userId, bean.getTxSubjectCode());
    if (!userSkylineModels.isEmpty() && !userSkylineModels.isEmpty()) {
      // generate unique competencies of completed, mastered and inprogress status.
      List<CompetencyStatusModel> completedOrMastered =
          segregateCompetencyCompletionStatus(masteredCompetencyList, inprogressCompetencyList,
              completedCompetencyList, userSkylineModels);
      // compute inferred competencies
      computeInferred(inferredCompetencyList, userSkylineModels, completedOrMastered, false);
      
      // here we are excluding inprogress competencies which are already mastered.
      inprogressCompetencyList = removeUnwantedSetFromInputCompList(masteredCompetencyList,
          completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
      
      // here we are excluding completed competencies which are already mastered.
      completedCompetencyList = removeUnwantedSetFromInputCompList(masteredCompetencyList,
          null, null, completedCompetencyList);
    }

    allTimeData.put(Constants.Response.MASTERED, masteredCompetencyList)
        .put(Constants.Response.COMPLETED, completedCompetencyList)
        .put(Constants.Response.INFERRED, inferredCompetencyList)
        .put(Constants.Response.IN_PROGRESS, inprogressCompetencyList);
  }

  private List<CompetencyStatusModel> segregateCompetencyCompletionStatus(
      JsonArray masteredCompetencyList, JsonArray inprogressCompetencyList,
      JsonArray completedCompetencyList, List<CompetencyStatusModel> userSkylineModels) {
    filterAndAddToRespectiveArray(userSkylineModels, completedCompetencyList,
        StatusConstants.COMPLETED);
    filterAndAddToRespectiveArray(userSkylineModels, masteredCompetencyList,
        StatusConstants.MASTERED);
    filterAndAddToRespectiveArray(userSkylineModels, inprogressCompetencyList,
        StatusConstants.IN_PROGRESS);

    List<CompetencyStatusModel> completedOrMastered = new ArrayList<>();
    Set<String> completedOrMasteredCompetencySet = new HashSet<>();
    userSkylineModels.forEach(model -> {
      if (model.getStatus() >= StatusConstants.COMPLETED
          && !completedOrMasteredCompetencySet.contains(model.getCompetencyCode())) {
        completedOrMastered.add(model);
        completedOrMasteredCompetencySet.add(model.getCompetencyCode());
      }
    });

    return completedOrMastered;
  }

  private Map<String, Set<String>> computeInferred(JsonArray inferredCompetencyList,
      List<CompetencyStatusModel> userSkylineModels, List<CompetencyStatusModel> completedModels,
      boolean fetchForCustomDate) {
    Map<String, Map<String, CompetencyStatusModel>> completedCompMap = new HashMap<>();
    Set<String> completedCompetencySet = new HashSet<>();
    completedModels.forEach(model -> {
      String domain = model.getDomainCode();
      String compCode = model.getCompetencyCode();
      completedCompetencySet.add(compCode);

      if (completedCompMap.containsKey(domain)) {
        Map<String, CompetencyStatusModel> competencies = completedCompMap.get(domain);
        competencies.put(compCode, model);
        completedCompMap.put(domain, competencies);
      } else {
        Map<String, CompetencyStatusModel> competencies = new HashMap<>();
        competencies.put(compCode, model);
        completedCompMap.put(domain, competencies);
      }
    });
    Map<String, Set<String>> completedCompetencyMap = new HashMap<>();
    Set<String> inferredCompetencySet = new HashSet<>();
    Set<String> inferredCompetencyCompletedSet = new HashSet<>();
    for (CompetencyStatusModel model : userSkylineModels) {
      String domainCode = model.getDomainCode();
      int compSequence = model.getCompetencySeq();
      int status = model.getStatus();

      if (completedCompMap.containsKey(domainCode)) {
        Map<String, CompetencyStatusModel> competencies = completedCompMap.get(domainCode);
        for (Map.Entry<String, CompetencyStatusModel> entry : competencies.entrySet()) {
          CompetencyStatusModel compModel = entry.getValue();
          int completedCompSeq = compModel.getCompetencySeq();

          if (compSequence < completedCompSeq && status != StatusConstants.INFERRED
              && !inferredCompetencySet.contains(model.getCompetencyCode())
              && !completedCompetencySet.contains(model.getCompetencyCode())
              && ((!fetchForCustomDate && status < StatusConstants.ASSERTED)
                  || (fetchForCustomDate && status != StatusConstants.ASSERTED))) {
            JsonObject inferredCompetency = new JsonObject();
            inferredCompetency.put(Constants.Response.ID, model.getCompetencyCode());
            inferredCompetency.put(Constants.Response.CODE, model.getCompetencyDisplayCode());
            inferredCompetencyList.add(inferredCompetency);
            inferredCompetencySet.add(model.getCompetencyCode());
            if (fetchForCustomDate && status >= StatusConstants.COMPLETED) {
              inferredCompetencyCompletedSet.add(model.getCompetencyCode());
            }
          }
        }
      }
    }
    completedCompetencyMap.put(Constants.Response.COMPLETED, completedCompetencySet);
    completedCompetencyMap.put(COMPLETED_INFERRED, inferredCompetencyCompletedSet);
    return completedCompetencyMap;
  }

  private JsonArray removeUnwantedSetFromInputCompList(JsonArray masteredCompetencyList,
      JsonArray completedCompetencyList, JsonArray inferredCompetencyList,
      JsonArray inputCompetencyList) {
    JsonArray masteredOrCompletedList = new JsonArray();
    if (masteredCompetencyList != null && !masteredCompetencyList.isEmpty())
      masteredOrCompletedList.addAll(masteredCompetencyList);
    if (completedCompetencyList != null && !completedCompetencyList.isEmpty())
      masteredOrCompletedList.addAll(completedCompetencyList);
    if (inferredCompetencyList != null && !inferredCompetencyList.isEmpty())
      masteredOrCompletedList.addAll(inferredCompetencyList);

    Set<String> completedOrMasteredCompetencySet = new HashSet<>();
    masteredOrCompletedList.forEach(completedCompetencyObj -> {
      JsonObject completedCompetency = (JsonObject) completedCompetencyObj;
      completedOrMasteredCompetencySet.add(completedCompetency.getString(Constants.Response.ID));
    });

    Set<String> finalCompetencySet = new HashSet<>();
    JsonArray finalCompetencyOpList = new JsonArray();
    inputCompetencyList.forEach(inputCompetencyObj -> {
      JsonObject inputCompetency = (JsonObject) inputCompetencyObj;
      if (!completedOrMasteredCompetencySet
          .contains(inputCompetency.getString(Constants.Response.ID))
          && !finalCompetencySet
              .contains(inputCompetency.getString(Constants.Response.ID))) {
        finalCompetencyOpList.add(inputCompetency);
        finalCompetencySet.add(inputCompetency.getString(Constants.Response.ID));
      }
    });
    inputCompetencyList = finalCompetencyOpList;
    return inputCompetencyList;
  }

  private JsonArray removeInferredCompetenciesCompletedInAnotherPeriod(
      JsonArray inferredCompetencyList, List<String> skylineCompletedCompetenciesInGivenPeriod,
      Map<String, Set<String>> completedCompSet) {
    Set<String> completedSet = completedCompSet.get(Constants.Response.COMPLETED);
    Set<String> inferredCompetencyCompletedSet = completedCompSet.get(COMPLETED_INFERRED);
    Set<String> inferredCompetencies = new HashSet<>();
    JsonArray inferredCompetencyOpList = new JsonArray();
    inferredCompetencyList.forEach(inferredCompetencyObj -> {
      JsonObject inferredCompetency = (JsonObject) inferredCompetencyObj;
      if (!inferredCompetencies.contains(inferredCompetency.getString(Constants.Response.ID))) {
        if (inferredCompetencyCompletedSet
            .contains(inferredCompetency.getString(Constants.Response.ID))) {
          if (completedSet.contains(inferredCompetency.getString(Constants.Response.ID))
              && skylineCompletedCompetenciesInGivenPeriod
                  .contains(inferredCompetency.getString(Constants.Response.ID))) {
            inferredCompetencyOpList.add(inferredCompetency);
            inferredCompetencies.add(inferredCompetency.getString(Constants.Response.ID));
          }
        } else {
          inferredCompetencyOpList.add(inferredCompetency);
          inferredCompetencies.add(inferredCompetency.getString(Constants.Response.ID));
        }
      }
    });
    inferredCompetencyList = inferredCompetencyOpList;
    return inferredCompetencyList;
  }

  private void filterAndAddToRespectiveArray(List<CompetencyStatusModel> inputModels,
      JsonArray competencyListToAdd, Integer statusToFilter) {
    List<CompetencyStatusModel> competencyStudyStatus = new ArrayList<>();
    Set<String> competencyStatusSet = new HashSet<>();
    inputModels.forEach(model -> {
      if (model.getStatus() == statusToFilter
          && !competencyStatusSet.contains(model.getCompetencyCode())) {
        competencyStudyStatus.add(model);
        competencyStatusSet.add(model.getCompetencyCode());
      }
    });
    for (CompetencyStatusModel competencyStatus : competencyStudyStatus) {
      JsonObject competencies = new JsonObject();
      competencies.put(Constants.Response.ID, competencyStatus.getCompetencyCode());
      competencies.put(Constants.Response.CODE, competencyStatus.getCompetencyDisplayCode());
      competencyListToAdd.add(competencies);
    }
  }
  
  private Date getPreviousDateOfGivenDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE,-1);
    Date fromDate = new Date(calendar.getTimeInMillis());
    return fromDate;
  }

}
