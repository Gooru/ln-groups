package org.gooru.groups.reports.competency.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru on 21-Apr-2020
 *
 */
public class GroupCompetencyReportModelMapper implements ResultSetMapper<GroupCompetencyReportModel>{

  @Override
  public GroupCompetencyReportModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    GroupCompetencyReportModel model = new GroupCompetencyReportModel();
    model.setId(r.getLong("id"));
    model.setClassId(r.getString("class_id"));
    model.setCompletedCompetencies(r.getLong("completed_competencies"));
    model.setInferredCompetencies(r.getLong("inferred_competencies"));
    model.setInprogressCompetencies(r.getLong("inprogress_competencies"));
    model.setNotstartedCompetencies(r.getLong("notstarted_competencies"));
    model.setWeek(r.getInt("week"));
    model.setMonth(r.getInt("month"));
    model.setYear(r.getInt("year"));
    model.setTenant(r.getString("tenant"));
    return model;
  }

}
