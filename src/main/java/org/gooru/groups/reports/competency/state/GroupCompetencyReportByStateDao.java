package org.gooru.groups.reports.competency.state;

import java.util.List;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 06-Feb-2020
 *
 */
public interface GroupCompetencyReportByStateDao {

  // Group Competency report by State

  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM class_performance_data_reports WHERE state_id = :stateId AND"
      + " month = :month AND year = :year")
  Double fetchAveragePerformanceByState(
      @BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean);

  @Mapper(GroupCompetencyReportByStateModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[])"
      + " AND state_id = :stateId AND month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportByStateModel> fetchGroupCompetencyReportByState(
      @Bind("groupIds") String groupIds,
      @BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean);

  @Mapper(GroupCompetencyGroupWiseReportByStateModelMapper.class)
  @SqlQuery("SELECT group_id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[]) AND state_id = :stateId AND month = :month AND year = :year GROUP"
      + " BY group_id")
  List<GroupCompetencyGroupWiseReportByStateModel> fetchGroupCompetencyGroupWiseReportByState(
      @Bind("groupIds") String groupIds,
      @BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean);

  // Group Competency report by State And Tenant

  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM class_performance_data_reports WHERE state_id = :stateId AND"
      + " month = :month AND year = :year AND tenant = ANY(:tenantIds)")
  Double fetchAveragePerformanceByStateAndTenant(
      @BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean,
      @Bind("tenantIds") PGArray<String> tenantIds);

  @Mapper(GroupCompetencyReportByStateModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[])"
      + " AND state_id = :stateId AND month = :month AND year = :year AND tenant = ANY(:tenantIds) GROUP BY week")
  List<GroupCompetencyReportByStateModel> fetchGroupCompetencyReportByStateAndTenant(
      @Bind("groupIds") String groupIds,
      @BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean,
      @Bind("tenantIds") PGArray<String> tenantIds);

  @Mapper(GroupCompetencyGroupWiseReportByStateModelMapper.class)
  @SqlQuery("SELECT group_id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[]) AND state_id = :stateId AND month = :month AND year = :year AND tenant = ANY(:tenantIds) GROUP"
      + " BY group_id")
  List<GroupCompetencyGroupWiseReportByStateModel> fetchGroupCompetencyGroupWiseReportByStateAndTenant(
      @Bind("groupIds") String groupIds,
      @BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean,
      @Bind("tenantIds") PGArray<String> tenantIds);
}
