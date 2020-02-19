
package org.gooru.groups.reports.competency.group;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencySchoolWiseReportBySDorClusterModelMapper
    implements ResultSetMapper<GroupCompetencyDrillDownReportByGroupOrSchoolModel> {

  @Override
  public GroupCompetencyDrillDownReportByGroupOrSchoolModel map(int index, ResultSet r,
      StatementContext ctx) throws SQLException {
    GroupCompetencyDrillDownReportByGroupOrSchoolModel model =
        new GroupCompetencyDrillDownReportByGroupOrSchoolModel();
    model.setId(r.getLong("id"));
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    model.setInprogressCompetencies(r.getLong("inprogress_competencies"));
    return model;
  }

}
