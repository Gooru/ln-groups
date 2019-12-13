
package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 14-Dec-2019
 */
public class SchoolModelMapper implements ResultSetMapper<SchoolModel> {

  @Override
  public SchoolModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    SchoolModel model = new SchoolModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setCode(r.getString("code"));
    return model;
  }

}
