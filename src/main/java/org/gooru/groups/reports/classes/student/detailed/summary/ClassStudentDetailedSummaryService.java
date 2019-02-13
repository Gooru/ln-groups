
package org.gooru.groups.reports.classes.student.detailed.summary;

import java.util.List;
import java.util.ResourceBundle;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.processor.utils.ValidatorUtils;
import org.gooru.groups.reports.dbhelpers.core.ClassMembersModel;
import org.gooru.groups.reports.dbhelpers.core.ClassModel;
import org.gooru.groups.reports.dbhelpers.core.CompetencyModel;
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
public class ClassStudentDetailedSummaryService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ClassStudentDetailedSummaryService.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private final StudentPerformanceDao studentInteractionDao;
  private final CoreService coreService;

  public ClassStudentDetailedSummaryService(DBI coreDbi, DBI dsDbi, DBI analyticsDbi) {
    this.coreService = new CoreService(coreDbi);
    this.studentInteractionDao = analyticsDbi.onDemand(StudentPerformanceDao.class);
  }

  public JsonObject fetchClassStudentDetailedSummary(ClassStudentDetailedSummaryBean bean,
      String userCdnUrl) {

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
        JsonObject weekData = fetchWeekData(bean, classMember.getUserId(), classData.getCourseId());

        summaryData.put(Constants.Response.WEEK_OF, weekData);

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

  private JsonObject fetchWeekData(ClassStudentDetailedSummaryBean bean, String userId,
      String courseId) {

    JsonObject weekData = new JsonObject();
    weekData
        .put(Constants.Response.START_DATE, Constants.Params.DATE_FORMAT.format(bean.getFromDate()))
        .put(Constants.Response.END_DATE, Constants.Params.DATE_FORMAT.format(bean.getToDate()));

    JsonArray usageData = fetchSummary(bean, userId, courseId);
    weekData.put(Constants.Response.USAGE_DATA, usageData);

    return weekData;
  }

  private JsonArray fetchSummary(ClassStudentDetailedSummaryBean bean, String userId,
      String courseId) {
    JsonArray usageData = new JsonArray();
    List<StudentPerformanceModel> studentPerfs =
        this.studentInteractionDao.fetchAssessmentPerformanceInWeek(bean.getClassId(), userId,
            bean.getFromDate(), bean.getToDate());
    List<StudentPerformanceModel> collPerfs =
        this.studentInteractionDao.fetchCollectionPerformanceInWeek(bean.getClassId(), userId,
            bean.getFromDate(), bean.getToDate());
    if (collPerfs != null && !collPerfs.isEmpty())
      studentPerfs.addAll(collPerfs);
    if (studentPerfs != null && !studentPerfs.isEmpty()) {
      for (StudentPerformanceModel studentPerf : studentPerfs) {
        JsonObject usageSummary = new JsonObject();

        JsonObject context = populateContextObject(courseId, studentPerf, usageSummary);

        JsonObject content = populateContentObject(studentPerf, usageSummary);
        usageSummary.put(Constants.Response.CONTEXT, context);
        usageSummary.put(Constants.Response.CONTENT, content);
        if (context == null || content == null) {
          continue;
        }

        usageData.add(usageSummary);
      }
    }
    return usageData;
  }

  private JsonObject populateContextObject(String courseId, StudentPerformanceModel studentPerf,
      JsonObject usageSummary) {
    JsonObject context = new JsonObject();
    JsonObject unit = null;
    if (studentPerf.getUnitId() != null) {
      String unitTitle = this.coreService.fetchUnitTitle(studentPerf.getUnitId(), courseId);
      if (unitTitle == null) {
        return null;
      }
      unit = populateContainerMeta(studentPerf.getUnitId(), unitTitle);
    }
    JsonObject lesson = null;
    if (studentPerf.getLessonId() != null) {
      String lessonTitle =
          this.coreService.fetchLessonTitle(studentPerf.getLessonId(), studentPerf.getUnitId());
      if (lessonTitle == null) {
        return null;
      }
      lesson = populateContainerMeta(studentPerf.getLessonId(), lessonTitle);
    }
    context.put(Constants.Response.UNIT, unit);
    context.put(Constants.Response.LESSON, lesson);
    context.put(Constants.Response.SESSION_ID, studentPerf.getSessionId());
    context.put(Constants.Response.DATE_OF_ACTIVITY, studentPerf.getDateOfActivity());
    return context;
  }

  private JsonObject populateContentObject(StudentPerformanceModel studentPerf,
      JsonObject usageSummary) {
    JsonObject content = new JsonObject();
    String collectionTitle = null;
    if (studentPerf.getLessonId() != null) {
      collectionTitle = this.coreService
          .fetchCollectionTitleByLessonId(studentPerf.getCollectionId(), studentPerf.getLessonId());
    } else {
      // May be Offline CA data which will not have unit/lesson 
      collectionTitle = this.coreService.fetchCollectionTitle(studentPerf.getCollectionId());
    }
    if (collectionTitle == null) {
      return null;
    }
    content.put(Constants.Response.ID, studentPerf.getCollectionId());
    content.put(Constants.Response.TITLE, collectionTitle);
    content.put(Constants.Response.TYPE, studentPerf.getCollectionType());
    content.put(Constants.Response.URL,
        System.getProperty(Constants.PRODUCT_DOMAIN) + "/player/" + studentPerf.getCollectionId());
    List<CompetencyModel> competencies =
        this.coreService.findCompetenciesForCollection(studentPerf.getCollectionId());
    JsonArray competencyArray = new JsonArray();
    for (CompetencyModel competency : competencies) {
      competencyArray.add(JsonObject.mapFrom(competency));
    }
    content.put(Constants.Response.COMPETENCIES, competencyArray);
    content.put(Constants.Response.SCORE, studentPerf.getScore());
    content.put(Constants.Response.MAX_SCORE, studentPerf.getMaxScore());
    content.put(Constants.Response.TIMESPENT, studentPerf.getTimespent());
    content.put(Constants.Response.GRADING_STATUS,
        (studentPerf.getIsGraded() != null && !studentPerf.getIsGraded()) ? "in-progress"
            : "complete");
    return content;
  }

  private JsonObject populateContainerMeta(String id, String title) {
    JsonObject container = new JsonObject();
    container.put(Constants.Response.ID, id);
    container.put(Constants.Response.TITLE, title);
    return container;
  }

}
