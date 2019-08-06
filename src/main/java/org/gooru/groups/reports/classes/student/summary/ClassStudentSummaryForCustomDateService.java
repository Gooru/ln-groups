
package org.gooru.groups.reports.classes.student.summary;

import java.util.List;
import java.util.ResourceBundle;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.constants.StatusConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.processor.utils.ValidatorUtils;
import org.gooru.groups.reports.dbhelpers.core.ClassMembersModel;
import org.gooru.groups.reports.dbhelpers.core.ClassModel;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.UserModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author renuka
 */
public class ClassStudentSummaryForCustomDateService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassStudentSummaryForCustomDateService.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private final ClassSummaryCompetencyMasteryDao classSummaryMasterydao;
  private final StudentItemInteractionDao studentInteractionDao;
  private final CoreService coreService;

  public ClassStudentSummaryForCustomDateService(DBI coreDbi, DBI dsDbi, DBI analyticsDbi) {
    this.coreService = new CoreService(coreDbi);
    this.classSummaryMasterydao = dsDbi.onDemand(ClassSummaryCompetencyMasteryDao.class);
    this.studentInteractionDao = analyticsDbi.onDemand(StudentItemInteractionDao.class);
  }

  public JsonObject fetchClassStudentSummary(ClassStudentSummaryBean bean, String userCdnUrl) {
    JsonObject responseObject = new JsonObject();

    ClassModel classData = this.coreService.fetchClass(bean.getClassId());
    if (classData == null) {
      LOGGER.warn("Requested class is unavailable");
      throw new HttpResponseWrapperException(HttpStatus.NOT_FOUND,
          RESOURCE_BUNDLE.getString("class.unavailable"));
    }

    List<ClassMembersModel> classMembers = this.coreService.fetchClassMembers(bean.getClassId());
    int memberCount = (classMembers != null && !classMembers.isEmpty()) ? classMembers.size() : 0;
    JsonObject classObject = fetchClassDetails(bean.getClassId(), classData, memberCount);
    responseObject.put(Constants.Response.CLASS, classObject);
    
    JsonObject course = fetchCourseData(classData.getCourseId());
    responseObject.put(Constants.Response.COURSE, course);

    JsonObject teacher = fetchUserData(classData.getCreatorId(), userCdnUrl);
    responseObject.put(Constants.Response.TEACHER, teacher);
    
    JsonArray studentSummary = new JsonArray();
    if (memberCount > 0) {
      for (ClassMembersModel classMember : classMembers) {
        JsonObject studentObject = new JsonObject();
        JsonObject student = fetchUserData(classMember.getUserId(), userCdnUrl);
        studentObject.put(Constants.Response.STUDENT, student);

        JsonObject usageSummary = fetchUsageSummary(bean, classMember.getUserId());

        studentObject.put(Constants.Response.SUMMARY_DATA, usageSummary);
        studentSummary.add(studentObject);
      }
    }
    responseObject.put(Constants.Response.STUDENTS, studentSummary);
    return responseObject;
  }

  private JsonObject fetchUserData(String userId, String userCdnUrl) {
    UserModel userData = this.coreService.fetchUser(userId);
    JsonObject user = new JsonObject();
    user.put(Constants.Response.ID, userId)
        .put(Constants.Response.FIRST_NAME, userData.getFirstName())
        .put(Constants.Response.LAST_NAME, userData.getLastName())
        .put(Constants.Response.EMAIL, userData.getEmail());
    String profileImage = null;
    if (!ValidatorUtils.isNullOrEmpty(userData.getProfileImage()) && userCdnUrl != null) {
      profileImage = Constants.HTTP + Constants.COLON + userCdnUrl + userData.getProfileImage();
    }
    user.put(Constants.Response.PROFILE_IMAGE, profileImage);
    return user;
  }

  private JsonObject fetchCourseData(String courseId) {
    JsonObject course = new JsonObject();
    course.put(Constants.Response.ID, courseId);
    course.put(Constants.Response.TITLE, this.coreService.fetchCourseTitle(courseId));
    return course;
  }

  private JsonObject fetchClassDetails(String classId, ClassModel classData, int memberCount) {
    JsonObject classObject = new JsonObject();
    classObject.put(Constants.Response.ID, classId)
        .put(Constants.Response.TITLE, classData.getTitle())
        .put(Constants.Response.CODE, classData.getCode())
        .put(Constants.Response.GRADE, classData.getGradeCurrent());
    classObject.put(Constants.Response.ACTIVE_STUDENT_COUNT, memberCount);
    return classObject;
  }

  private JsonObject fetchUsageSummary(ClassStudentSummaryBean bean, String userId) {
    JsonObject usageSummaryData = new JsonObject();
    generateCompetencyStats(bean, userId, usageSummaryData);

    JsonObject suggestions = new JsonObject();
    JsonObject interactions = new JsonObject();

    List<StudentItemInteraction> studentAssessmentSuggestionInteraction =
        this.studentInteractionDao.fetchAssessmentSuggestionInteractionInAPeriod(bean.getClassId(),
            userId, bean.getFromDate(), bean.getToDate());
    fetchItemInteractionStats(studentAssessmentSuggestionInteraction, suggestions);

    List<StudentItemInteraction> studentAssessmentInteraction =
        this.studentInteractionDao.fetchAssessmentInteractionInAPeriod(bean.getClassId(), userId,
            bean.getFromDate(), bean.getToDate());
    fetchItemInteractionStats(studentAssessmentInteraction, interactions);

    List<StudentItemInteraction> studentCollectionSuggestionInteraction =
        this.studentInteractionDao.fetchCollectionSuggestionInteractionInAPeriod(bean.getClassId(),
            userId, bean.getFromDate(), bean.getToDate());
    fetchItemInteractionStats(studentCollectionSuggestionInteraction, suggestions);

    List<StudentItemInteraction> studentCollectionInteraction =
        this.studentInteractionDao.fetchCollectionInteractionInAPeriod(bean.getClassId(), userId,
            bean.getFromDate(), bean.getToDate());
    fetchItemInteractionStats(studentCollectionInteraction, interactions);

    String lastAccessed = this.studentInteractionDao.fetchLastInteractionDate(bean.getClassId(),
        userId, bean.getFromDate(), bean.getToDate());

    usageSummaryData.put(Constants.Response.LAST_ACCESSED,
        lastAccessed != null ? lastAccessed.toString() : null);

    usageSummaryData
        .put(Constants.Response.START_DATE, Constants.Params.DATE_FORMAT.format(bean.getFromDate()))
        .put(Constants.Response.END_DATE, Constants.Params.DATE_FORMAT.format(bean.getToDate()))
        .put(Constants.Response.SUGGESTIONS, suggestions)
        .put(Constants.Response.INTERACTIONS, interactions);
    return usageSummaryData;
  }

  private JsonObject fetchItemInteractionStats(List<StudentItemInteraction> interactedItems,
      JsonObject interactions) {
    if (interactedItems != null && !interactedItems.isEmpty()) {
      for (StudentItemInteraction interactedItem : interactedItems) {
        JsonObject item = new JsonObject();
        item.put(Constants.Response.COUNT, interactedItem.getUniqueItemCount());
        item.put(Constants.Response.SESSIONS_COUNT, interactedItem.getInteractionCount());
        item.put(Constants.Response.TOTAL_TIME_SPENT, interactedItem.getTimespent());
        item.put(Constants.Response.AVERAGE_SCORE, interactedItem.getScore());
        item.put(Constants.Response.TOTAL_MAX_SCORE, interactedItem.getMaxScore());
        interactions.put(interactedItem.getCollectionType(), item);
      }
    }
    return interactions;
  }

  private void generateCompetencyStats(ClassStudentSummaryBean bean, String userId,
      JsonObject usageSummaryData) {
    List<CompetencyStatusModel> studentCompetencyStudyStatus = this.classSummaryMasterydao
        .fetchCompetenciesInAPeriod(bean.getClassId(), userId, bean.getFromDate(), bean.getToDate());
    JsonArray masteredCompetencyList = new JsonArray();
    JsonArray completedCompetencyList = new JsonArray();
    JsonArray inferredCompetencyList = new JsonArray();
    JsonArray inprogressCompetencyList = new JsonArray();
    aggregateCompetencyPerfPerStatus(studentCompetencyStudyStatus, masteredCompetencyList,
        completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
    usageSummaryData.put(Constants.Response.MASTERED, masteredCompetencyList)
        .put(Constants.Response.COMPLETED, completedCompetencyList)
        .put(Constants.Response.INFERRED, inferredCompetencyList)
        .put(Constants.Response.IN_PROGRESS, inprogressCompetencyList);
  }

  private void aggregateCompetencyPerfPerStatus(
      List<CompetencyStatusModel> studentCompetencyStudyStatus, JsonArray masteredCompetencyList,
      JsonArray completedCompetencyList, JsonArray inferredCompetencyList,
      JsonArray inprogressCompetencyList) {
    for (CompetencyStatusModel competencyStudyStatus : studentCompetencyStudyStatus) {
      switch (competencyStudyStatus.getStatus()) {
        case StatusConstants.MASTERED:
          addToRespectiveArray(competencyStudyStatus, masteredCompetencyList);
          break;
        case StatusConstants.COMPLETED:
          addToRespectiveArray(competencyStudyStatus, completedCompetencyList);
          break;
        case StatusConstants.INFERRED:
          addToRespectiveArray(competencyStudyStatus, inferredCompetencyList);
          break;
        case StatusConstants.IN_PROGRESS:
          addToRespectiveArray(competencyStudyStatus, inprogressCompetencyList);
          break;
        default:
          // Do Nothing
      }
    }
  }

  private void addToRespectiveArray(CompetencyStatusModel competencyStudyStatus,
      JsonArray competencyList) {
    JsonObject competencies = new JsonObject();
    competencies.put(Constants.Response.ID, competencyStudyStatus.getCompetencyCode());
    competencies.put(Constants.Response.CODE, competencyStudyStatus.getCompetencyDisplayCode());
    competencyList.add(competencies);
  }

}
