
package org.gooru.groups.reports.competency.country;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyDrilldownReportByCountryModelMapper
    implements ResultSetMapper<GroupCompetencyDrilldownReportByCountryModel> {

  @Override
  public GroupCompetencyDrilldownReportByCountryModel map(int index, ResultSet r,
      StatementContext ctx) throws SQLException {
    GroupCompetencyDrilldownReportByCountryModel model =
        new GroupCompetencyDrilldownReportByCountryModel();
    model.setId(r.getLong("id"));
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    model.setInprogressCompetencies(r.getLong("inprogress_competencies"));
    return model;
  }

}
