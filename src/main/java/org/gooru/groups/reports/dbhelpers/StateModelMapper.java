
package org.gooru.groups.reports.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public class StateModelMapper implements ResultSetMapper<StateModel> {

  @Override
  public StateModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    StateModel model = new StateModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setCode(r.getString("code"));
    return model;
  }

}
