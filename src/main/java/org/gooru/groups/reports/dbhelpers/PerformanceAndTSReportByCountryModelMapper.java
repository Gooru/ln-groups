
package org.gooru.groups.reports.dbhelpers;

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
    model.setStateId(r.getInt("state_id"));
    model.setName(r.getString("name"));
    model.setCode(r.getString("code"));
    model.setTimespent(r.getLong("collection_ts"));
    model.setPerformance(r.getDouble("assessment_perf"));
    return model;
  }

}
