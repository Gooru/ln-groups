package org.gooru.groups.hierarchies.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public class TenantHierarchyModelMapper implements ResultSetMapper<TenantHierarchyModel> {

  @Override
  public TenantHierarchyModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    TenantHierarchyModel model = new TenantHierarchyModel();
    model.setTenant(r.getString("id"));
    model.setHierarchyId(r.getLong("hierarchy_id"));
    return model;
  }


}
