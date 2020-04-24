package org.gooru.groups.hierarchies.fetch;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class FetchGroupHierarchiesAllModelMapper
    implements ResultSetMapper<FetchGroupHierarchiesAllModel> {

  @Override
  public FetchGroupHierarchiesAllModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    FetchGroupHierarchiesAllModel model = new FetchGroupHierarchiesAllModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setDescription(r.getString("description"));
    return model;
  }

}
