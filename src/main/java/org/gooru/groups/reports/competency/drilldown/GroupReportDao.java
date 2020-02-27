package org.gooru.groups.reports.competency.drilldown;

import java.util.List;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 17-Feb-2020
 *
 */
public interface GroupReportDao {

  @Mapper(GroupReportByCountryModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_competencies, country_id FROM class_competency_data_reports GROUP BY country_id")
  List<GroupReportByCountryModel> fetchCompetencyCountsAllTime();

  @Mapper(GroupReportByCountryModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_competencies, country_id FROM class_competency_data_reports WHERE month = :month AND"
      + " year = :year GROUP BY country_id")
  List<GroupReportByCountryModel> fetchCompetencyCountsByMonthYear(
      @BindBean GroupReportCommand.GroupReportCommandBean bean);

  @Mapper(GroupReportByCountryModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_competencies, country_id FROM class_competency_data_reports WHERE country_id = ANY(:countryIds::bigint[])"
      + " GROUP BY country_id")
  List<GroupReportByCountryModel> fetchCompetencyCountsByCountryAllTime(
      @BindBean GroupReportCommand.GroupReportCommandBean bean,
      @Bind("countryIds") PGArray<Long> countryIds);

  @Mapper(GroupReportByCountryModelMapper.class)
  @SqlQuery("SELECT SUM(completed_count) AS completed_competencies, country_id FROM class_competency_data_reports WHERE month = :month AND"
      + " year = :year AND country_id = ANY(:countryIds::bigint[]) GROUP BY country_id")
  List<GroupReportByCountryModel> fetchCompetencyCountsByMonthYearAndCountry(
      @BindBean GroupReportCommand.GroupReportCommandBean bean,
      @Bind("countryIds") PGArray<Long> countryIds);
}
