
package org.gooru.groups.reports.competency.country;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportByCountryModelMapper
    implements ResultSetMapper<GroupCompetencyReportByCountryModel> {

  @Override
  public GroupCompetencyReportByCountryModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    GroupCompetencyReportByCountryModel model = new GroupCompetencyReportByCountryModel();
    model.setWeek(r.getInt("week"));
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    return model;
  }

}
