
package org.gooru.groups.reports.competency.state;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyGroupWiseReportByStateModelMapper
    implements ResultSetMapper<GroupCompetencyGroupWiseReportByStateModel> {

  @Override
  public GroupCompetencyGroupWiseReportByStateModel map(int index, ResultSet r,
      StatementContext ctx) throws SQLException {
    GroupCompetencyGroupWiseReportByStateModel model =
        new GroupCompetencyGroupWiseReportByStateModel();
    model.setGroupId(r.getLong("group_id"));
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    model.setInprogressCompetencies(r.getLong("inprogress_competencies"));
    return model;
  }

}
