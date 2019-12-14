
package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 14-Dec-2019
 */
public class SubjectModelMapper implements ResultSetMapper<SubjectModel> {

  @Override
  public SubjectModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    SubjectModel model = new SubjectModel();
    model.setId(r.getString("id"));
    model.setTitle(r.getString("title"));
    return model;
  }

}
