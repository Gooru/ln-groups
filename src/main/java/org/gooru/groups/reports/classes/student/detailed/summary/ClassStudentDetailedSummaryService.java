
package org.gooru.groups.reports.classes.student.detailed.summary;

import java.util.Collections;
import java.util.List;
import org.gooru.groups.reports.dbhelpers.core.ClassMembersModel;
import org.gooru.groups.reports.dbhelpers.core.ClassModel;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.UserModel;
import org.skife.jdbi.v2.DBI;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author renuka
 */
public class ClassStudentDetailedSummaryService {

  private final StudentPerformanceDao studentInteractionDao;
  private final CoreService coreService;

  public ClassStudentDetailedSummaryService(DBI coreDbi, DBI dsDbi, DBI analyticsDbi) {
    this.coreService = new CoreService(coreDbi);
    this.studentInteractionDao = analyticsDbi.onDemand(StudentPerformanceDao.class);
  }

  public JsonObject fetchClassStudentDetailedSummary(ClassStudentDetailedSummaryBean bean) {
    JsonObject classObject = new JsonObject();
    JsonArray studentSummary = new JsonArray();

    List<ClassMembersModel> classMembers = this.coreService.fetchClassMembers(bean.getClassId());
    for (ClassMembersModel classMember : classMembers) {
      JsonObject studentStats = new JsonObject();

      JsonArray usageList = new JsonArray();
      List<StudentPerformanceModel> studentPerfs =
          this.studentInteractionDao.fetchStudentPerformanceInWeek(bean.getClassId(),
              classMember.getUserId(), bean.getFromDate(), bean.getToDate());
      if (studentPerfs != null && !studentPerfs.isEmpty()) {
        for (StudentPerformanceModel studentPerf : studentPerfs) {
          JsonObject usageSummary = new JsonObject();
          usageSummary.put("courseId", studentPerf.getCourseId());
          usageSummary.put("courseTitle",
              this.coreService.fetchCourseTitle(studentPerf.getCourseId()));

          usageSummary.put("unitId", studentPerf.getUnitId());
          usageSummary.put("unitTitle", this.coreService.fetchUnitTitle(studentPerf.getUnitId()));

          usageSummary.put("lessonId", studentPerf.getLessonId());
          usageSummary.put("lessonTitle",
              this.coreService.fetchLessonTitle(studentPerf.getLessonId()));

          usageSummary.put("collectionId", studentPerf.getCollectionId());
          usageSummary.put("collectionTitle",
              this.coreService.fetchCollectionTitle(studentPerf.getCollectionId()));
          usageSummary.put("collectionType", studentPerf.getCollectionType());

          List<String> competencies =
              this.coreService.findCompetenciesForCollection(studentPerf.getCollectionId());
          usageSummary.put("competencies",
              (competencies != null && !competencies.isEmpty()) ? competencies
                  : Collections.emptyList());
          usageSummary.put("url",
              System.getProperty("host") + "/player/" + studentPerf.getCollectionId());

          usageSummary.put("score",
              studentPerf.getCollectionType().equalsIgnoreCase("assessment")
                  ? studentPerf.getScore()
                  : null);
          usageSummary.put("timespent", studentPerf.getTimespent());

          usageSummary.put("dateOfActivity", studentPerf.getDateOfActivity());

          usageList.add(usageSummary);
        }
      }
      fetchStudentData(classMember, studentStats);
      studentStats.put("usageData", usageList);
      studentSummary.add(studentStats);
    }
    fetchClassData(bean.getClassId(), classObject);
    classObject.put("students", studentSummary);
    return classObject;
  }

  private void fetchStudentData(ClassMembersModel classMember, JsonObject studentStats) {
    UserModel userData = this.coreService.fetchUser(classMember.getUserId());
    studentStats.put("id", classMember.getUserId()).put("firstName", userData.getFirstName())
        .put("lastName", userData.getLastName());
  }

  private void fetchClassData(String classId, JsonObject classObject) {
    ClassModel classData = this.coreService.fetchClass(classId);
    UserModel userData = this.coreService.fetchUser(classData.getCreatorId());
    JsonObject teacher = new JsonObject();
    teacher.put("id", classData.getCreatorId()).put("firstName", userData.getFirstName())
        .put("lastName", userData.getLastName());
    classObject.put("id", classId).put("title", classData.getTitle()).put("teacher", teacher);
  }

}
