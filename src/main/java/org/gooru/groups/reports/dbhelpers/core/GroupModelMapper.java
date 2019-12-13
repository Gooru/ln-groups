
package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public class GroupModelMapper implements ResultSetMapper<GroupModel> {

  @Override
  public GroupModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    GroupModel model = new GroupModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setCode(r.getString("code"));
    model.setType(r.getString("type"));
    model.setSubType(r.getString("sub_type"));
    return model;
  }

}
