package org.gooru.groups.reports.competency.drilldown;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 20-Feb-2020
 *
 */
public class GroupReportByCountryModelMapper implements ResultSetMapper<GroupReportByCountryModel> {

  @Override
  public GroupReportByCountryModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    GroupReportByCountryModel model = new GroupReportByCountryModel();
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    model.setCountryId(r.getLong("country_id"));
    return model;
  }

}
