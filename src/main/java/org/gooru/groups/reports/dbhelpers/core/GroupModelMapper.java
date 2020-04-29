
package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
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
    try {
      String subType = r.getString("sub_type");
      model.setSubType((subType != null && !subType.isEmpty()) ? subType : null);
    } catch (PSQLException pse) {
      // We do not need to do anything here because the query may not always have the sub type
      // column
    }
    return model;
  }

}
