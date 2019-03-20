
package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class ClassTitleModelMapper implements ResultSetMapper<ClassTitleModel> {

  @Override
  public ClassTitleModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    ClassTitleModel model = new ClassTitleModel();
    model.setId(r.getString("id"));
    model.setTitle(r.getString("title"));
    return model;
  }

}
