package org.gooru.groups.reports.competency.country;

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
public interface GroupCompetencyReportByCountryDao {

  // Group Competency report by Country
  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM class_performance_data_reports WHERE country_id = :countryId AND"
      + " month = :month AND year = :year")
  Double fetchAveragePerformanceByCountry(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean);

  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM class_performance_data_reports WHERE country_id = :countryId AND"
      + " month = :month AND year = :year AND tenant = ANY(:tenantIds)")
  Double fetchAveragePerformanceByCountryAndTenant(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean,
      @Bind("tenantIds") PGArray<String> tenantIds);

  @Mapper(GroupCompetencyReportByCountryModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE country_id = :countryId AND"
      + " month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportByCountryModel> fetchGroupCompetencyReportByCountry(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean);

  @Mapper(GroupCompetencyReportByCountryModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE country_id = :countryId AND"
      + " month = :month AND year = :year AND tenant = ANY(:tenantIds) GROUP BY week")
  List<GroupCompetencyReportByCountryModel> fetchGroupCompetencyReportByCountryAndTenant(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean,
      @Bind("tenantIds") PGArray<String> tenantIds);

  @Mapper(GroupCompetencyDrilldownReportByCountryModelMapper.class)
  @SqlQuery("SELECT state_id as id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE country_id = :countryId AND month = :month AND year = :year GROUP BY state_id")
  List<GroupCompetencyDrilldownReportByCountryModel> fetchGroupCompetencyStateWiseReportByCountry(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean);

  @Mapper(GroupCompetencyDrilldownReportByCountryModelMapper.class)
  @SqlQuery("SELECT state_id as id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE country_id = :countryId AND month = :month AND year = :year AND tenant = ANY(:tenantIds) GROUP BY state_id")
  List<GroupCompetencyDrilldownReportByCountryModel> fetchGroupCompetencyStateWiseReportByCountryAndTenant(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean,
      @Bind("tenantIds") PGArray<String> tenantIds);

  // School wise report by Country

  @Mapper(GroupCompetencyDrilldownReportByCountryModelMapper.class)
  @SqlQuery("SELECT school_id as id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE country_id = :countryId AND month = :month AND year = :year GROUP BY school_id")
  List<GroupCompetencyDrilldownReportByCountryModel> fetchGroupCompetencySchoolWiseReportByCountry(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean);

  @Mapper(GroupCompetencyDrilldownReportByCountryModelMapper.class)
  @SqlQuery("SELECT school_id as id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE country_id = :countryId AND month = :month AND year = :year AND tenant = ANY(:tenantIds) GROUP BY school_id")
  List<GroupCompetencyDrilldownReportByCountryModel> fetchGroupCompetencySchoolWiseReportByCountryAndTenant(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean,
      @Bind("tenantIds") PGArray<String> tenantIds);
}
