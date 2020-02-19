package org.gooru.groups.reports.competency.school;

import java.util.List;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 06-Feb-2020
 *
 */
public interface GroupCompetencyReportBySchoolDao {

  // Group Competency report by School
  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM class_performance_data_reports WHERE school_id = :schoolId AND"
      + " month = :month AND year = :year")
  Double fetchAveragePerformanceBySchool(@BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean);

  @Mapper(GroupCompetencyReportBySchoolModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE school_id = :schoolId AND"
      + " month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportBySchoolModel> fetchGroupCompetencyReportBySchool(
      @BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean);

  @Mapper(GroupCompetencyClassWiseReportBySchoolModelMapper.class)
  @SqlQuery("SELECT class_id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE school_id = :schoolId AND month = :month AND year = :year GROUP BY class_id")
  List<GroupCompetencyClassWiseReportBySchoolModel> fetchGroupCompetencyClassWiseReportBySchool(
      @BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean);
  
  // Group Competency report by School
  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM class_performance_data_reports WHERE school_id = :schoolId AND"
      + " month = :month AND year = :year AND tenant = ANY(:tenantIds)")
  Double fetchAveragePerformanceBySchoolAndTenant(@BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean, @Bind("tenantIds") PGArray<String> tenantIds);

  @Mapper(GroupCompetencyReportBySchoolModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE school_id = :schoolId AND"
      + " month = :month AND year = :year AND tenant = ANY(:tenantIds) GROUP BY week")
  List<GroupCompetencyReportBySchoolModel> fetchGroupCompetencyReportBySchoolAndTenant(
      @BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean, @Bind("tenantIds") PGArray<String> tenantIds);

  @Mapper(GroupCompetencyClassWiseReportBySchoolModelMapper.class)
  @SqlQuery("SELECT class_id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE school_id = :schoolId AND month = :month AND year = :year AND tenant = ANY(:tenantIds) GROUP BY class_id")
  List<GroupCompetencyClassWiseReportBySchoolModel> fetchGroupCompetencyClassWiseReportBySchoolAndTenant(
      @BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean, @Bind("tenantIds") PGArray<String> tenantIds);
}
