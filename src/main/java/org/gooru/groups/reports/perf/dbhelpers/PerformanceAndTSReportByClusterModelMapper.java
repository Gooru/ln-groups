
package org.gooru.groups.reports.perf.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class PerformanceAndTSReportByClusterModelMapper
    implements ResultSetMapper<PerformanceAndTSReportByClusterModel> {

  @Override
  public PerformanceAndTSReportByClusterModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    PerformanceAndTSReportByClusterModel model = new PerformanceAndTSReportByClusterModel();
    model.setSchoolId(r.getLong("school_id"));
    model.setTimespent(r.getLong("collection_ts"));
    model.setPerformance(r.getDouble("assessment_perf"));
    return model;
  }

}
