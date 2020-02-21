package org.gooru.groups.reports.classes.student.summary;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.constants.StatusConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
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
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
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
    List<CompetencyStatusModel> userSkylineModels = new ArrayList<>();
    // Get completed competencies(with seq) from class in given period and using skyline
    // competencies seq compute inferred.
    List<CompetencyStatusModel> userCompetencyCompletionModels =
        this.classSummaryMasterydao.fetchCompetencyCompletionInClassInWeek(bean.getClassId(),
            userId, bean.getFromDate(), bean.getToDate());
    if (userCompetencyCompletionModels != null && !userCompetencyCompletionModels.isEmpty()) {
      for (CompetencyStatusModel completedCompetencyCode : userCompetencyCompletionModels) {
        String[] s = completedCompetencyCode.getCompetencyCode().split("-");
        if (!bean.getTxSubjectCode().equalsIgnoreCase(s[0])) {
          LOGGER.warn(
              "Either requested subject is not matching with data or multiple subject codes are inferred in this class");
          throw new HttpResponseWrapperException(HttpStatus.CONFLICT,
              RESOURCE_BUNDLE.getString("mismatching.subjectcode"));
        }
      }
      userSkylineModels = classSummaryMasterydao.fetchUserSkyline(userId, bean.getTxSubjectCode());
    }

    if (!userCompetencyCompletionModels.isEmpty()) {
      // generate unique competencies of completed, mastered and inprogress status.
      List<CompetencyStatusModel> completedOrMastered =
          segregateCompetencyCompletionStatus(masteredCompetencyList, inprogressCompetencyList,
              completedCompetencyList, userCompetencyCompletionModels);
      // compute inferred competencies
      computeInferred(inferredCompetencyList, userSkylineModels, completedOrMastered);
      // since we are taking in class competency completion from evidence table, here we are
      // excluding
      // inprogress competencies which are already completed.
      inprogressCompetencyList = removeCompletedFromInprogressList(masteredCompetencyList,
          completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
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
    List<CompetencyStatusModel> userSkylineModels = new ArrayList<>();
    // Get completed competencies(with seq) from class all time and using skyline competencies seq
    // compute inferred.
    List<CompetencyStatusModel> userCompetencyCompletionModels = this.classSummaryMasterydao
        .fetchCompetencyCompletionInClassTillNow(bean.getClassId(), userId, currentDate);
    if (userCompetencyCompletionModels != null && !userCompetencyCompletionModels.isEmpty()) {
      for (CompetencyStatusModel completedCompetencyCode : userCompetencyCompletionModels) {
        String[] s = completedCompetencyCode.getCompetencyCode().split("-");
        if (!bean.getTxSubjectCode().equalsIgnoreCase(s[0])) {
          LOGGER.warn(
              "Either requested subject is not matching or multiple subject codes are inferred in this class");
          throw new HttpResponseWrapperException(HttpStatus.CONFLICT,
              RESOURCE_BUNDLE.getString("mismatching.subjectcode"));
        }
      }
      userSkylineModels = classSummaryMasterydao.fetchUserSkyline(userId, bean.getTxSubjectCode());
    }

    if (!userCompetencyCompletionModels.isEmpty()) {
      // generate unique competencies of completed, mastered and inprogress status.
      List<CompetencyStatusModel> completedOrMastered =
          segregateCompetencyCompletionStatus(masteredCompetencyList, inprogressCompetencyList,
              completedCompetencyList, userCompetencyCompletionModels);
      // compute inferred competencies
      computeInferred(inferredCompetencyList, userSkylineModels, completedOrMastered);
      // since we are taking in class competency completion from evidence table, here we are
      // excluding inprogress competencies which are already completed.
      inprogressCompetencyList = removeCompletedFromInprogressList(masteredCompetencyList,
          completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
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
    List<CompetencyStatusModel> userSkylineModels =
        classSummaryMasterydao.fetchUserSkyline(userId, bean.getTxSubjectCode());
    List<CompetencyStatusModel> userCompetencyCompletionModels =
        classSummaryMasterydao.fetchUserSkylineInWeek(userId, bean.getTxSubjectCode(),
            bean.getFromDate(), bean.getToDate());
    if (!userCompetencyCompletionModels.isEmpty()) {
      List<CompetencyStatusModel> completedOrMastered =
          segregateCompetencyCompletionStatus(masteredCompetencyList, inprogressCompetencyList,
              completedCompetencyList, userCompetencyCompletionModels);
      computeInferred(inferredCompetencyList, userSkylineModels, completedOrMastered);
      inprogressCompetencyList = removeCompletedFromInprogressList(masteredCompetencyList,
          completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
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
    if (!userSkylineModels.isEmpty()) {
      List<CompetencyStatusModel> completedOrMastered =
          segregateCompetencyCompletionStatus(masteredCompetencyList, inprogressCompetencyList,
              completedCompetencyList, userSkylineModels);
      computeInferred(inferredCompetencyList, userSkylineModels, completedOrMastered);
      inprogressCompetencyList = removeCompletedFromInprogressList(masteredCompetencyList,
          completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
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

  private void computeInferred(JsonArray inferredCompetencyList,
      List<CompetencyStatusModel> userSkylineModels, List<CompetencyStatusModel> completedModels) {
    Map<String, Map<String, CompetencyStatusModel>> completedCompMap = new HashMap<>();
    completedModels.forEach(model -> {
      String domain = model.getDomainCode();
      String compCode = model.getCompetencyCode();

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
    Set<String> inferredCompetencySet = new HashSet<>();
    userSkylineModels.forEach(model -> {
      String domainCode = model.getDomainCode();
      int compSequence = model.getCompetencySeq();
      int status = model.getStatus();

      if (completedCompMap.containsKey(domainCode)) {
        Map<String, CompetencyStatusModel> competencies = completedCompMap.get(domainCode);
        for (Map.Entry<String, CompetencyStatusModel> entry : competencies.entrySet()) {
          CompetencyStatusModel compModel = entry.getValue();
          int completedCompSeq = compModel.getCompetencySeq();

          if (compSequence < completedCompSeq && status < StatusConstants.ASSERTED
              && status != StatusConstants.INFERRED
              && !inferredCompetencySet.contains(model.getCompetencyCode())) {
            JsonObject inferredCompetency = new JsonObject();
            inferredCompetency.put(Constants.Response.ID, model.getCompetencyCode());
            inferredCompetency.put(Constants.Response.CODE, model.getCompetencyDisplayCode());
            inferredCompetencyList.add(inferredCompetency);
            inferredCompetencySet.add(model.getCompetencyCode());
          }
        }
      }
    });
  }

  private JsonArray removeCompletedFromInprogressList(JsonArray masteredCompetencyList,
      JsonArray completedCompetencyList, JsonArray inferredCompetencyList,
      JsonArray inprogressCompetencyList) {
    JsonArray masteredOrCompletedList = new JsonArray();
    masteredOrCompletedList.addAll(masteredCompetencyList);
    masteredOrCompletedList.addAll(completedCompetencyList);
    masteredOrCompletedList.addAll(inferredCompetencyList);

    Set<String> completedOrMasteredCompetencySet = new HashSet<>();
    masteredOrCompletedList.forEach(completedCompetencyObj -> {
      JsonObject completedCompetency = (JsonObject) completedCompetencyObj;
      completedOrMasteredCompetencySet.add(completedCompetency.getString(Constants.Response.ID));
    });

    Set<String> inprogressCompetencySet = new HashSet<>();
    JsonArray inprogressCompetencyOpList = new JsonArray();
    inprogressCompetencyList.forEach(inprogressCompetencyObj -> {
      JsonObject inprogressCompetency = (JsonObject) inprogressCompetencyObj;
      if (!completedOrMasteredCompetencySet
          .contains(inprogressCompetency.getString(Constants.Response.ID))
          && !inprogressCompetencySet
              .contains(inprogressCompetency.getString(Constants.Response.ID))) {
        inprogressCompetencyOpList.add(inprogressCompetency);
        inprogressCompetencySet.add(inprogressCompetency.getString(Constants.Response.ID));
      }
    });
    inprogressCompetencyList = inprogressCompetencyOpList;
    return inprogressCompetencyList;
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


}
