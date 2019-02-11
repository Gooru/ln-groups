
package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class ClassMembersModelMapper implements ResultSetMapper<ClassMembersModel> {

  @Override
  public ClassMembersModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    ClassMembersModel classModel = new ClassMembersModel();
    classModel.setUserId(r.getString("user_id"));
    classModel.setEmail(r.getString("email"));
    classModel.setFirstName(r.getString("first_name"));
    classModel.setLastName(r.getString("last_name"));
    return classModel;
  }

}
