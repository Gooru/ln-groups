
package org.gooru.groups.reports.classes.summary;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class CompetencyStatusMapper implements ResultSetMapper<CompetencyStatus> {

  @Override
  public CompetencyStatus map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    CompetencyStatus activity = new CompetencyStatus();
    activity.setGutCode(r.getString("gut_code"));
    activity.setStatus(r.getInt("status"));
    return activity;
  }

}
