
package org.gooru.groups.reports.classes.summary;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class StudentItemInteractionMapper implements ResultSetMapper<StudentItemInteraction> {

  @Override
  public StudentItemInteraction map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    StudentItemInteraction activity = new StudentItemInteraction();
    activity.setInteractionCount(r.getInt("interaction_count"));
    activity.setTimespent(r.getLong("timespent"));
    return activity;
  }

}
