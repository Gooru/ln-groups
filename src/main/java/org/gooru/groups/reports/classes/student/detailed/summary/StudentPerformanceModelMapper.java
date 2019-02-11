
package org.gooru.groups.reports.classes.student.detailed.summary;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class StudentPerformanceModelMapper implements ResultSetMapper<StudentPerformanceModel> {

  @Override
  public StudentPerformanceModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    StudentPerformanceModel activity = new StudentPerformanceModel();
    activity.setScore(r.getInt("score"));
    activity.setTimespent(r.getLong("timespent"));
    activity.setMaxScore(r.getInt("max_score"));
    activity.setCollectionId(r.getString("collection_id"));
    activity.setCollectionType(r.getString("collection_type"));
    activity.setLessonId(r.getString("lesson_id"));
    activity.setUnitId(r.getString("unit_id"));
    activity.setCourseId(r.getString("course_id"));
    activity.setSessionId(r.getString("session_id"));
    activity.setIsGraded(r.getBoolean("is_graded"));
    activity.setDateOfActivity(r.getString("created_at"));
    return activity;
  }

}
