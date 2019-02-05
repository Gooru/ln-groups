
package org.gooru.groups.reports.classes.summary;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.constants.StatusConstants;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.processor.utils.ValidatorUtils;
import org.gooru.groups.reports.classes.student.detailed.summary.ClassStudentDetailedSummaryService;
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
public class ClassSummaryService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ClassStudentDetailedSummaryService.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private final CompetencyStatusDao classSummaryMasterydao;
  private final ClassTimespentSummaryDao classTimespentSummaryDao;
  private final CoreService coreService;

  public ClassSummaryService(DBI coreDbi, DBI dsDbi, DBI analyticsDbi) {
    this.coreService = new CoreService(coreDbi);
    this.classSummaryMasterydao = dsDbi.onDemand(CompetencyStatusDao.class);
    this.classTimespentSummaryDao = analyticsDbi.onDemand(ClassTimespentSummaryDao.class);
  }

  public JsonObject fetchClassSummary(ClassSummaryBean bean, String userCdnUrl) {

    JsonObject responseObject = new JsonObject();

    ClassModel classData = this.coreService.fetchClass(bean.getClassId());
    if (classData == null) {
      LOGGER.warn("Requested class is unavailable");
      throw new HttpResponseWrapperException(HttpStatus.NOT_FOUND,
          RESOURCE_BUNDLE.getString("class.unavailable"));
    }

    JsonObject classObject = fetchClassDetails(bean.getClassId(), classData);
    responseObject.put(Constants.Response.CLASS, classObject);

    JsonObject course = fetchCourseData(classData.getCourseId());
    responseObject.put(Constants.Response.COURSE, course);

    JsonObject teacher = fetchUserData(classData.getCreatorId(), userCdnUrl);
    responseObject.put(Constants.Response.TEACHER, teacher);

    JsonObject summaryData = new JsonObject();
    JsonObject weekData = fetchWeekData(bean);
    JsonObject allTimeData = fetchAllTimeData(bean);
    summaryData.put(Constants.Response.WEEK_OF, weekData).put(Constants.Response.ALL_TIME,
        allTimeData);
    responseObject.put(Constants.Response.SUMMARY_DATA, summaryData);

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

  private JsonObject fetchClassDetails(String classId, ClassModel classData) {
    JsonObject classObject = new JsonObject();
    List<ClassMembersModel> classMembersData = this.coreService.fetchClassMembers(classId);
    classObject.put(Constants.Response.ID, classId)
        .put(Constants.Response.TITLE, classData.getTitle())
        .put(Constants.Response.CODE, classData.getCode())
        .put(Constants.Response.GRADE, classData.getGradeCurrent());
    classObject.put(Constants.Response.ACTIVE_STUDENT_COUNT,
        (classMembersData != null && !classMembersData.isEmpty()) ? classMembersData.size() : 0);
    return classObject;
  }

  private JsonObject fetchAllTimeData(ClassSummaryBean bean) {
    JsonObject allTimeData = new JsonObject();
    Date currentDate = Calendar.getInstance().getTime();

    List<CompetencyStatus> studentCompetencyStudyStatus =
        this.classSummaryMasterydao.fetchCompetenciesTillNow(bean.getClassId(), currentDate);
    aggregateCompetenciesPerStatus(studentCompetencyStudyStatus, allTimeData);

    allTimeData.put(Constants.Response.AS_ON, Constants.Params.DATE_FORMAT.format(currentDate));
    return allTimeData;
  }

  private JsonObject fetchWeekData(ClassSummaryBean bean) {
    JsonObject weekData = new JsonObject();

    List<CompetencyStatus> studentCompetencyStudyStatus =
        this.classSummaryMasterydao.fetchCompetenciesInWeek(bean);
    aggregateCompetenciesPerStatus(studentCompetencyStudyStatus, weekData);

    Long totalTimespentInWeek = this.classTimespentSummaryDao.fetchClassTimespentInWeek(bean);

    weekData
        .put(Constants.Response.START_DATE, Constants.Params.DATE_FORMAT.format(bean.getFromDate()))
        .put(Constants.Response.END_DATE, Constants.Params.DATE_FORMAT.format(bean.getToDate()))
        .put(Constants.Response.TOTAL_TIME_SPENT, totalTimespentInWeek);
    return weekData;
  }

  private void aggregateCompetenciesPerStatus(List<CompetencyStatus> studentCompetencyStudyStatus,
      JsonObject resultObject) {
    JsonArray masteredCompetencies = new JsonArray();
    JsonArray completedCompetencies = new JsonArray();
    JsonArray inferredCompetencies = new JsonArray();
    JsonArray inprogressCompetencies = new JsonArray();
    for (CompetencyStatus competencyStudyStatus : studentCompetencyStudyStatus) {
      switch (competencyStudyStatus.getStatus()) {
        case StatusConstants.MASTERED:
          masteredCompetencies.add(competencyStudyStatus.getGutCode());
          break;
        case StatusConstants.COMPLETED:
          completedCompetencies.add(competencyStudyStatus.getGutCode());
          break;
        case StatusConstants.INFERRED:
          inferredCompetencies.add(competencyStudyStatus.getGutCode());
          break;
        case StatusConstants.IN_PROGRESS:
          inprogressCompetencies.add(competencyStudyStatus.getGutCode());
          break;
      }
    }

    resultObject.put(Constants.Response.MASTERED, masteredCompetencies.size())
        .put(Constants.Response.COMPLETED, completedCompetencies.size())
        .put(Constants.Response.INFERRED, inferredCompetencies.size())
        .put(Constants.Response.IN_PROGRESS, inprogressCompetencies.size());
  }

}
