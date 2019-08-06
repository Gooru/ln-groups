
package org.gooru.groups.reports.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class PerformanceAndTSReportBySchoolModelMapper
    implements ResultSetMapper<PerformanceAndTSReportBySchoolModel> {

  @Override
  public PerformanceAndTSReportBySchoolModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    PerformanceAndTSReportBySchoolModel model = new PerformanceAndTSReportBySchoolModel();
    model.setClassId(r.getString("class_id"));
    model.setTimespent(r.getLong("collection_ts"));
    model.setPerformance(r.getDouble("assessment_perf"));
    return model;
  }

}
