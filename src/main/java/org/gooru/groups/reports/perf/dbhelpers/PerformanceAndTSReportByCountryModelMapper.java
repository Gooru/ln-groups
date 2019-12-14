
package org.gooru.groups.reports.perf.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class PerformanceAndTSReportByCountryModelMapper
    implements ResultSetMapper<PerformanceAndTSReportByCountryModel> {

  @Override
  public PerformanceAndTSReportByCountryModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    PerformanceAndTSReportByCountryModel model = new PerformanceAndTSReportByCountryModel();
    model.setStateId(r.getLong("state_id"));
    model.setTimespent(r.getLong("collection_ts"));
    model.setPerformance(r.getDouble("assessment_perf"));
    return model;
  }

}
