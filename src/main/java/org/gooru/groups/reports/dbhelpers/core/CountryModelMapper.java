package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 01-Feb-2020
 *
 */
public class CountryModelMapper implements ResultSetMapper<CountryModel> {

  @Override
  public CountryModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    CountryModel model = new CountryModel();
    model.setId(r.getLong("id"));
    model.setName(r.getString("name"));
    model.setCode(r.getString("code"));
    return model;
  }

}
