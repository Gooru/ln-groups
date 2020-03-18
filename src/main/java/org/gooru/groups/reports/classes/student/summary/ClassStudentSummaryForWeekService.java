
package org.gooru.groups.reports.classes.student.summary;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.processor.utils.ValidatorUtils;
import org.gooru.groups.reports.classes.student.summary.ClassStudentSummaryBean;
import org.gooru.groups.reports.classes.student.summary.CompetencyCompletionService;
import org.gooru.groups.reports.classes.student.summary.StudentItemInteraction;
import org.gooru.groups.reports.classes.student.summary.StudentItemInteractionDao;
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
public class ClassStudentSummaryForWeekService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassStudentSummaryForWeekService.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private final StudentItemInteractionDao studentInteractionDao;
  private final CoreService coreService;
  CompetencyCompletionService competencyCompletionService;

  public ClassStudentSummaryForWeekService(DBI coreDbi, DBI dsDbi, DBI analyticsDbi) {
    this.coreService = new CoreService(coreDbi);
    this.studentInteractionDao = analyticsDbi.onDemand(StudentItemInteractionDao.class);
    this.competencyCompletionService = new CompetencyCompletionService(dsDbi);
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
        JsonObject weekData = fetchWeekData(bean, classMember.getUserId());
        JsonObject allTimeData = fetchAllTimeData(bean, classMember.getUserId());

        summaryData.put(Constants.Response.WEEK_OF, weekData).put(Constants.Response.ALL_TIME,
            allTimeData);

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
    JsonObject allTimeData = new JsonObject();
    Date currentDate = Calendar.getInstance().getTime();
    if (!bean.getSkylineSummary()) {
      competencyCompletionService.generateAllTimeCompetencyStatsInClass(bean, userId, allTimeData,
          currentDate);
    } else {
      competencyCompletionService.generateAllTimeCompetencyStatsInSkyline(bean, userId, allTimeData,
          currentDate);
    }

    allTimeData.put(Constants.Response.AS_ON, Constants.Params.DATE_FORMAT.format(currentDate));
    return allTimeData;
  }

  private JsonObject fetchWeekData(ClassStudentSummaryBean bean, String userId) {
    JsonObject weekData = new JsonObject();

    if (!bean.getSkylineSummary()) {
      competencyCompletionService.generateWeeklyCompetencyStatsInClass(bean, userId, weekData);
    } else {
      competencyCompletionService.generateWeeklyCompetencyStatsInSkyline(bean, userId, weekData);
    }

    JsonObject suggestions = new JsonObject();
    JsonObject interactions = new JsonObject();

    List<StudentItemInteraction> studentAssessmentSuggestionInteraction =
        this.studentInteractionDao.fetchAssessmentSuggestionInteractionInWeek(bean.getClassId(),
            userId, bean.getFromDate(), bean.getToDate());
    fetchItemInteractionStats(studentAssessmentSuggestionInteraction, suggestions);

    List<StudentItemInteraction> studentAssessmentInteraction =
        this.studentInteractionDao.fetchAssessmentInteractionInWeek(bean.getClassId(), userId,
            bean.getFromDate(), bean.getToDate());
    fetchItemInteractionStats(studentAssessmentInteraction, interactions);

    List<StudentItemInteraction> studentCollectionSuggestionInteraction =
        this.studentInteractionDao.fetchCollectionSuggestionInteractionInWeek(bean.getClassId(),
            userId, bean.getFromDate(), bean.getToDate());
    fetchItemInteractionStats(studentCollectionSuggestionInteraction, suggestions);

    List<StudentItemInteraction> studentCollectionInteraction =
        this.studentInteractionDao.fetchCollectionInteractionInWeek(bean.getClassId(), userId,
            bean.getFromDate(), bean.getToDate());
    fetchItemInteractionStats(studentCollectionInteraction, interactions);

    String lastAccessedDate = this.studentInteractionDao.fetchLastInteractionDate(bean.getClassId(),
        userId, bean.getFromDate(), bean.getToDate());

    weekData.put(Constants.Response.LAST_ACCESSED,
        lastAccessedDate != null ? lastAccessedDate.toString() : null);

    weekData
        .put(Constants.Response.START_DATE, Constants.Params.DATE_FORMAT.format(bean.getFromDate()))
        .put(Constants.Response.END_DATE, Constants.Params.DATE_FORMAT.format(bean.getToDate()))
        .put(Constants.Response.SUGGESTIONS, suggestions)
        .put(Constants.Response.INTERACTIONS, interactions);
    return weekData;
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

}
