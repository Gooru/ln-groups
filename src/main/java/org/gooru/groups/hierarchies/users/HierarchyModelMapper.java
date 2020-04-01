package org.gooru.groups.hierarchies.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public class HierarchyModelMapper implements ResultSetMapper<HierarchyModel> {

  @Override
  public HierarchyModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    HierarchyModel model = new HierarchyModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setDescription(r.getString("description"));
    return model;
  }


}
