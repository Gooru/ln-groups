package org.gooru.groups.reports.dbhelpers.core.hierarchy;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru
 *
 */
public class GroupHierarchyModelMapper implements ResultSetMapper<GroupHierarchyModel> {

  @Override
  public GroupHierarchyModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    GroupHierarchyModel model = new GroupHierarchyModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setDescription(r.getString("description"));
    return model;
  }

}
