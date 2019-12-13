
package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class ClassModelMapper implements ResultSetMapper<ClassModel> {

  @Override
  public ClassModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    ClassModel classModel = new ClassModel();
    classModel.setId(r.getString("id"));
    classModel.setCode(r.getString("code"));
    classModel.setTitle(r.getString("title"));
    classModel.setCreatorId(r.getString("creator_id"));
    classModel.setCourseId(r.getString("course_id"));
    classModel.setGradeCurrent(r.getString("grade_current"));
    return classModel;
  }

}
