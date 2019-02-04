
package org.gooru.groups.reports.classes.student.summary;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class StudentSuggestedItemInteractionMapper
    implements ResultSetMapper<StudentSuggestedItemInteraction> {

  @Override
  public StudentSuggestedItemInteraction map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    StudentSuggestedItemInteraction activity = new StudentSuggestedItemInteraction();
    activity.setSuggestType(r.getString("collection_type"));
    activity.setInteractedSuggestCount(r.getInt("interacted_suggest_count"));
    return activity;
  }

}
