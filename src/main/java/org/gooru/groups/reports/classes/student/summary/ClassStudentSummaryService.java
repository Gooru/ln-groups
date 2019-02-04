
package org.gooru.groups.reports.classes.student.summary;

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
public class ClassStudentSummaryService {

  private final ClassSummaryCompetencyMasteryDao classSummaryMasterydao;
  private final StudentContentInteractionDao studentInteractionDao;
  private final CoreService coreService;

  public ClassStudentSummaryService(DBI coreDbi, DBI dsDbi, DBI analyticsDbi) {
    this.coreService = new CoreService(coreDbi);
    this.classSummaryMasterydao = dsDbi.onDemand(ClassSummaryCompetencyMasteryDao.class);
    this.studentInteractionDao = analyticsDbi.onDemand(StudentContentInteractionDao.class);
  }

  public JsonObject fetchClassStudentSummary(ClassStudentSummaryBean bean) {
    JsonObject classObject = new JsonObject();
    JsonArray studentSummary = new JsonArray();

    List<ClassMembersModel> classMembers = this.coreService.fetchClassMembers(bean.getClassId());
    for (ClassMembersModel classMember : classMembers) {

      JsonObject usageSummary = new JsonObject();
      fetchStudentInteractionStats(bean, classMember, usageSummary);
      fetchCompetencyStatsInWeek(bean, classMember, usageSummary);

      JsonObject studentStats = new JsonObject();
      fetchStudentData(classMember, studentStats);
      studentStats.put("usageData", usageSummary);
      studentSummary.add(studentStats);
    }
    fetchClassData(bean.getClassId(), classObject);
    classObject.put("studentSummary", studentSummary);
    return classObject;
  }

  private void fetchCompetencyStatsInWeek(ClassStudentSummaryBean bean,
      ClassMembersModel classMember, JsonObject usageSummary) {
    List<String> competenciesMasteredInWeek =
        this.classSummaryMasterydao.fetchCompetenciesMasteredInWeek(bean.getClassId(),
            classMember.getUserId(), bean.getFromDate(), bean.getToDate());
    int totalCompetenciesMasteredInWeek =
        (competenciesMasteredInWeek != null && !competenciesMasteredInWeek.isEmpty())
            ? competenciesMasteredInWeek.size()
            : 0;

    Integer badgesCountInWeek = this.classSummaryMasterydao.fetchBadgesEarnedInWeek(
        bean.getClassId(), classMember.getUserId(), bean.getFromDate(), bean.getToDate());
    Integer masteredCountUntilNow = this.classSummaryMasterydao.fetchCompetenciesMasteredUntilNow(
        bean.getClassId(), classMember.getUserId(), bean.getToDate());

    usageSummary.put("totalCompetenciesMasteredInWeek", totalCompetenciesMasteredInWeek)
        .put("competenciesMasteredInWeek", competenciesMasteredInWeek)
        .put("inferredCompetencyMasteredInWeek", totalCompetenciesMasteredInWeek)
        .put("badgesEarnedInWeek", badgesCountInWeek)
        .put("totalCompetenciesMastered", masteredCountUntilNow);
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

  private void fetchStudentInteractionStats(ClassStudentSummaryBean bean,
      ClassMembersModel classMember, JsonObject studentUsage) {
    Long totalTimespentInWeek = this.studentInteractionDao.fetchClassTimespentInWeek(
        bean.getClassId(), classMember.getUserId(), bean.getFromDate(), bean.getToDate());

    List<StudentSuggestedItemInteraction> studentInteractionOnSuggestedItems =
        this.studentInteractionDao.fetchStudentInteractionOnSuggestedItemInWeek(bean.getClassId(),
            classMember.getUserId(), bean.getFromDate(), bean.getToDate());
    int takenAssessmentSuggestions = 0;
    int takenCollectionSuggestions = 0;
    for (StudentSuggestedItemInteraction studentInteractionOnSuggestedItem : studentInteractionOnSuggestedItems) {
      if (studentInteractionOnSuggestedItem.getSuggestType().equalsIgnoreCase("assessment")) {
        takenAssessmentSuggestions = studentInteractionOnSuggestedItem.getInteractedSuggestCount();
      } else if (studentInteractionOnSuggestedItem.getSuggestType()
          .equalsIgnoreCase("collection")) {
        takenCollectionSuggestions = studentInteractionOnSuggestedItem.getInteractedSuggestCount();
      }
    }
    studentUsage.put("assessmentSuggestionsTakenInweek", takenAssessmentSuggestions)
        .put("collectionSuggestionsTakenInweek", takenCollectionSuggestions)
        .put("totalTimespentInWeek", totalTimespentInWeek);

    int collectionInteractionCountInWeek = 0;
    int assessmentInteractionCountInWeek = 0;
    StudentContentInteraction studentContentInteraction =
        this.studentInteractionDao.fetchStudentContentInteractionInWeek(bean.getClassId(),
            classMember.getUserId(), bean.getFromDate(), bean.getToDate());
    if (studentContentInteraction != null) {
      collectionInteractionCountInWeek = studentContentInteraction.getCollectionCount();
      assessmentInteractionCountInWeek = studentContentInteraction.getAssessmentCount();
    }
    studentUsage.put("collectionsInteractedInWeek", collectionInteractionCountInWeek)
        .put("assessmentsInteractedInWeek", assessmentInteractionCountInWeek);

  }
}
