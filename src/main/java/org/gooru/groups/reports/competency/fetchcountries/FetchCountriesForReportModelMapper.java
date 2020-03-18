package org.gooru.groups.reports.competency.fetchcountries;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 31-Jan-2020
 *
 */
public class FetchCountriesForReportModelMapper
    implements ResultSetMapper<FetchCountriesForReportModel> {

  @Override
  public FetchCountriesForReportModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    FetchCountriesForReportModel model = new FetchCountriesForReportModel();
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    model.setCountryId(r.getLong("country_id"));
    return model;
  }

}
