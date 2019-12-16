
package org.gooru.groups.reports.perf.dbhelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public class SubjectFrameworkModelMapper implements ResultSetMapper<SubjectFrameworkModel> {

  @Override
  public SubjectFrameworkModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    SubjectFrameworkModel model = new SubjectFrameworkModel();
    model.setSubject(r.getString("subject"));
    model.setFramework(r.getString("framework"));
    return model;
  }

}
