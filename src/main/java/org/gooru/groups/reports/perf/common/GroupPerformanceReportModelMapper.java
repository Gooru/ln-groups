package org.gooru.groups.reports.perf.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceReportModelMapper
    implements ResultSetMapper<GroupPerformanceReportModel> {

  @Override
  public GroupPerformanceReportModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    GroupPerformanceReportModel model = new GroupPerformanceReportModel();
    model.setId(r.getLong("id"));
    model.setClassId(r.getString("class_id"));
    model.setAveragePerformance(r.getDouble("average_performance"));
    model.setTotalCollectionTimespent(r.getLong("total_collection_timespent"));
    model.setTotalAssessmentTimespent(r.getLong("total_assessment_timespent"));
    model.setAverageCollectionTimespent(r.getDouble("average_collection_timespent"));
    model.setAverageAssessmentTimespent(r.getDouble("average_assessment_timespent"));
    model.setWeek(r.getInt("week"));
    model.setMonth(r.getInt("month"));
    model.setYear(r.getInt("year"));
    model.setTenant(r.getString("tenant"));

    return model;
  }

}
