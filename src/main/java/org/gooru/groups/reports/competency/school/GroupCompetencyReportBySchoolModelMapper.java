
package org.gooru.groups.reports.competency.school;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportBySchoolModelMapper
    implements ResultSetMapper<GroupCompetencyReportBySchoolModel> {

  @Override
  public GroupCompetencyReportBySchoolModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    GroupCompetencyReportBySchoolModel model = new GroupCompetencyReportBySchoolModel();
    model.setWeek(r.getInt("week"));
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    return model;
  }

}
