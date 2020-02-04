package org.gooru.groups.reports.dbhelpers.core.hierarchy;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru
 *
 */
public class GroupHierarchyDetailsModelMapper
    implements ResultSetMapper<GroupHierarchyDetailsModel> {

  @Override
  public GroupHierarchyDetailsModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    GroupHierarchyDetailsModel model = new GroupHierarchyDetailsModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setType(r.getString("type"));
    model.setHierarchyId(r.getLong("hierarchy_id"));
    model.setSequence(r.getInt("sequence"));
    return model;
  }

}
