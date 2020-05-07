package org.gooru.groups.reports.competency.common;

import java.util.List;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 17-Feb-2020
 *
 */
public interface GroupCompetencyReportDao {

  @Mapper(GroupCompetencyReportModelMapper.class)
  @SqlQuery("SELECT id, class_id, completed_competencies, inferred_competencies, inprogress_competencies, notstarted_competencies, week, month,"
      + " year, tenant FROM class_competency_base_reports_weekly WHERE class_id = ANY(:classIds) AND tenant = ANY(:tenants) AND month = :month"
      + " AND year = :year")
  List<GroupCompetencyReportModel> fetchCompetencyReportByMonthYear(
      @Bind("classIds") PGArray<String> classIds, @Bind("tenants") PGArray<String> tenants,
      @Bind("month") Integer month, @Bind("year") Integer year);
  
}
