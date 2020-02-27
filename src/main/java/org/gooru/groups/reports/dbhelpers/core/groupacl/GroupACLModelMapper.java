package org.gooru.groups.reports.dbhelpers.core.groupacl;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;

/**
 * @author szgooru on 18-Feb-2020
 *
 */
public class GroupACLModelMapper implements ResultSetMapper<GroupACLModel> {

  @Override
  public GroupACLModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    GroupACLModel model = new GroupACLModel();
    model.setType(r.getString("type"));

    String groupsStr = r.getString("groups");
    if (groupsStr != null && !groupsStr.isEmpty()) {
      try {
        model.setGroups(new JsonArray(groupsStr));
      } catch (DecodeException de) {
        model.setGroups(new JsonArray());
      }
    } else {
      model.setGroups(new JsonArray());
    }
    return model;
  }

}
