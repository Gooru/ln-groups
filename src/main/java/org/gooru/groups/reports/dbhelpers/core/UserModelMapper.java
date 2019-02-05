package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class UserModelMapper implements ResultSetMapper<UserModel> {

  @Override
  public UserModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    UserModel userModel = new UserModel();
    userModel.setFirstName(r.getString("first_name"));
    userModel.setLastName(r.getString("last_name"));
    userModel.setEmail(r.getString("email"));
    userModel.setProfileImage(r.getString("thumbnail"));
    return userModel;
  }
}
