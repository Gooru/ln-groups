
package org.gooru.groups.reports.ca;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 07-Jan-2019
 */
public class ClassActivityMapper implements ResultSetMapper<ClassActivity> {

  @Override
  public ClassActivity map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    ClassActivity activity = new ClassActivity();
    activity.setId(r.getInt("id"));
    activity.setActivationDate(r.getDate("activation_date"));
    return activity;
  }

}
