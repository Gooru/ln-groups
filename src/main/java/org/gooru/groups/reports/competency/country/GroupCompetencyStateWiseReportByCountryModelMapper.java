
package org.gooru.groups.reports.competency.country;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyStateWiseReportByCountryModelMapper
    implements ResultSetMapper<GroupCompetencyStateWiseReportByCountryModel> {

  @Override
  public GroupCompetencyStateWiseReportByCountryModel map(int index, ResultSet r,
      StatementContext ctx) throws SQLException {
    GroupCompetencyStateWiseReportByCountryModel model =
        new GroupCompetencyStateWiseReportByCountryModel();
    model.setStateId(r.getLong("state_id"));
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    model.setInprogressCompetencies(r.getLong("inprogress_competencies"));
    return model;
  }

}
