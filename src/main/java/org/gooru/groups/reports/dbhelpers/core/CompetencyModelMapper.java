
package org.gooru.groups.reports.dbhelpers.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author renuka
 */
public class CompetencyModelMapper implements ResultSetMapper<CompetencyModel> {

  @Override
  public CompetencyModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    CompetencyModel competencyModel = new CompetencyModel();
    competencyModel.setId(r.getString("id"));
    competencyModel.setCode(r.getString("code"));
    return competencyModel;
  }

}
