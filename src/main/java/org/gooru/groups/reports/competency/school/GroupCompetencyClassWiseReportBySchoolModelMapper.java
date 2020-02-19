
package org.gooru.groups.reports.competency.school;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyClassWiseReportBySchoolModelMapper
    implements ResultSetMapper<GroupCompetencyClassWiseReportBySchoolModel> {

  @Override
  public GroupCompetencyClassWiseReportBySchoolModel map(int index, ResultSet r,
      StatementContext ctx) throws SQLException {
    GroupCompetencyClassWiseReportBySchoolModel model =
        new GroupCompetencyClassWiseReportBySchoolModel();
    model.setClassId(r.getString("class_id"));
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    model.setInprogressCompetencies(r.getLong("inprogress_competencies"));
    return model;
  }

}
