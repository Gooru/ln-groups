package org.gooru.groups.app.jdbi;

import java.sql.Array;
import java.util.UUID;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

/**
 * @author ashish on 20/2/18.
 */
public class PostgresUUIDArrayArgumentFactory implements ArgumentFactory<PGArray<UUID>> {
  @SuppressWarnings("unchecked")
  public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx) {
    return value instanceof PGArray && ((PGArray) value).getType().isAssignableFrom(UUID.class);
  }

  @Override
  public Argument build(Class<?> expectedType, final PGArray<UUID> value, StatementContext ctx) {
    return (position, statement, ctx1) -> {
      Array ary = ctx1.getConnection().createArrayOf("uuid", value.getElements());
      statement.setArray(position, ary);
    };
  }

}
