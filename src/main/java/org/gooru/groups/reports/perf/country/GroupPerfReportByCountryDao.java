package org.gooru.groups.reports.perf.country;

import java.util.List;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByCountryModel;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByCountryModelMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 04-Feb-2020
 *
 */
public interface GroupPerfReportByCountryDao {

  // ---- Performance and Time spent report by country
  @Mapper(PerformanceAndTSReportByCountryModelMapper.class)
  @SqlQuery("SELECT state_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " class_performance_data_reports WHERE country_id = :countryId AND week = :week AND month = :month AND year = :year AND subject = :subject"
      + " AND framework = :framework GROUP BY state_id")
  List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSWeekReportByCountry(
      @BindBean GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean);
  
  @Mapper(PerformanceAndTSReportByCountryModelMapper.class)
  @SqlQuery("SELECT state_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " class_performance_data_reports WHERE country_id = :countryId AND month = :month AND year = :year AND subject = :subject AND framework ="
      + " :framework GROUP BY state_id")
  List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSMonthReportByCountry(
      @BindBean GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean);
}
