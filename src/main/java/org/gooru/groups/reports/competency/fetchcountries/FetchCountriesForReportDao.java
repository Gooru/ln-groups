package org.gooru.groups.reports.competency.fetchcountries;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 04-Feb-2020
 *
 */
public interface FetchCountriesForReportDao {

  @Mapper(FetchCountriesForReportModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_competencies, country_id FROM class_competency_data_reports GROUP BY country_id")
  List<FetchCountriesForReportModel> fetchCompetencyCountsAllTime(
      @BindBean FetchCountriesForReportCommand.FetchCountriesForReportCommandBean bean);

  @Mapper(FetchCountriesForReportModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_competencies, country_id FROM class_competency_data_reports WHERE month = :month AND"
      + " year = :year GROUP BY country_id")
  List<FetchCountriesForReportModel> fetchCompetencyCountsByMonthYear(
      @BindBean FetchCountriesForReportCommand.FetchCountriesForReportCommandBean bean);

  @Mapper(FetchCountriesForReportModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_competencies, country_id FROM class_competency_data_reports WHERE tenant = :tenantId"
      + " GROUP BY country_id")
  List<FetchCountriesForReportModel> fetchCompetencyCountsByTenantAllTime(
      @BindBean FetchCountriesForReportCommand.FetchCountriesForReportCommandBean bean);

  @Mapper(FetchCountriesForReportModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_competencies, country_id FROM class_competency_data_reports WHERE month = :month AND"
      + " year = :year AND tenant = :tenantId GROUP BY country_id")
  List<FetchCountriesForReportModel> fetchCompetencyCountsByTenantMonthYear(
      @BindBean FetchCountriesForReportCommand.FetchCountriesForReportCommandBean bean);

}
