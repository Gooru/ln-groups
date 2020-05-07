package org.gooru.groups.reports.perf.common;

import java.util.List;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 05-May-2020
 *
 */
interface GroupPerformanceReportDao {

  @Mapper(GroupPerformanceReportModelMapper.class)
  @SqlQuery("SELECT id, class_id, average_performance, total_collection_timespent, total_assessment_timespent, average_collection_timespent,"
      + " average_assessment_timespent, week, month, year, tenant FROM class_performance_base_reports_weekly WHERE class_id = ANY(:classIds) AND"
      + " tenant = ANY(:tenants) AND month = :month AND year = :year")
  List<GroupPerformanceReportModel> fetchWeeklyClassPerformance(
      @Bind("classIds") PGArray<String> classIds, @Bind("tenants") PGArray<String> tenants,
      @Bind("month") Integer month, @Bind("year") Integer year);
}
