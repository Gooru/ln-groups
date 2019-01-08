package org.gooru.groups.app.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 20/2/18.
 */
public class UUIDMapper implements ResultSetMapper<UUID> {

  @Override
  public UUID map(final int index, final ResultSet resultSet,
      final StatementContext statementContext) throws SQLException {
    final String uuidString = resultSet.getString(1);
    if (uuidString != null && !uuidString.isEmpty()) {
      return UUID.fromString(uuidString);
    }
    return null;
  }
}
