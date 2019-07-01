
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
public class ClassStudentSummaryService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassStudentSummaryService.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private final ClassSummaryCompetencyMasteryDao classSummaryMasterydao;
  private final StudentItemInteractionDao studentInteractionDao;
  private final CoreService coreService;

  public ClassStudentSummaryService(DBI coreDbi, DBI dsDbi, DBI analyticsDbi) {
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

        JsonObject summaryData = new JsonObject();
        JsonObject asOfNowData = fetchAllTimeData(bean, classMember.getUserId());

        summaryData.put(Constants.Response.ALL_TIME, asOfNowData);

        studentObject.put(Constants.Response.SUMMARY_DATA, summaryData);
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

  private JsonObject fetchAllTimeData(ClassStudentSummaryBean bean, String userId) {
    JsonObject asOfNowData = new JsonObject();
    generateWeeklyCompetencyStats(bean, userId, asOfNowData);

    JsonObject suggestions = new JsonObject();
    JsonObject interactions = new JsonObject();

    List<StudentItemInteraction> studentAssessmentSuggestionInteraction =
        this.studentInteractionDao.fetchAssessmentSuggestionInteraction(bean.getClassId(),
            userId, bean.getDateTill());
    fetchItemInteractionStats(studentAssessmentSuggestionInteraction, suggestions);

    List<StudentItemInteraction> studentAssessmentInteraction =
        this.studentInteractionDao.fetchAssessmentInteraction(bean.getClassId(), userId,
            bean.getDateTill());
    fetchItemInteractionStats(studentAssessmentInteraction, interactions);

    List<StudentItemInteraction> studentCollectionSuggestionInteraction =
        this.studentInteractionDao.fetchCollectionSuggestionInteraction(bean.getClassId(),
            userId, bean.getDateTill());
    fetchItemInteractionStats(studentCollectionSuggestionInteraction, suggestions);

    List<StudentItemInteraction> studentCollectionInteraction =
        this.studentInteractionDao.fetchCollectionInteraction(bean.getClassId(), userId,
            bean.getDateTill());
    fetchItemInteractionStats(studentCollectionInteraction, interactions);

    String lastAccessedDate = this.studentInteractionDao.fetchLastInteractionDate(bean.getClassId(), userId);
    asOfNowData
        .put(Constants.Response.LAST_ACCESSED_DATE, lastAccessedDate != null ? lastAccessedDate.toString() : null)
        .put(Constants.Response.END_DATE, Constants.Params.DATE_FORMAT.format(bean.getDateTill()))
        .put(Constants.Response.SUGGESTIONS, suggestions)
        .put(Constants.Response.INTERACTIONS, interactions);
    return asOfNowData;
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

  private void generateWeeklyCompetencyStats(ClassStudentSummaryBean bean, String userId,
      JsonObject weekData) {
    List<CompetencyStatusModel> studentCompetencyStudyStatus = this.classSummaryMasterydao
        .fetchCompetenciesTillNow(bean.getClassId(), userId, bean.getDateTill());
    JsonArray masteredCompetencyList = new JsonArray();
    JsonArray completedCompetencyList = new JsonArray();
    JsonArray inferredCompetencyList = new JsonArray();
    JsonArray inprogressCompetencyList = new JsonArray();
    aggregateCompetencyPerfPerStatus(studentCompetencyStudyStatus, masteredCompetencyList,
        completedCompetencyList, inferredCompetencyList, inprogressCompetencyList);
    weekData.put(Constants.Response.MASTERED, masteredCompetencyList)
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
