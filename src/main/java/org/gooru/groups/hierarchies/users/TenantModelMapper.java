package org.gooru.groups.hierarchies.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public class TenantModelMapper implements ResultSetMapper<TenantModel> {

  @Override
  public TenantModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    TenantModel model = new TenantModel();
    model.setId(r.getString("id"));
    model.setName(r.getString("name"));
    return model;
  }


}
