
package org.gooru.groups.reports.classes.student.summary;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class StudentContentInteractionMapper implements ResultSetMapper<StudentContentInteraction> {

  @Override
  public StudentContentInteraction map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    StudentContentInteraction activity = new StudentContentInteraction();
    activity.setCollectionCount(r.getInt("collection_count"));
    activity.setAssessmentCount(r.getInt("assessment_count"));
    return activity;
  }

}
