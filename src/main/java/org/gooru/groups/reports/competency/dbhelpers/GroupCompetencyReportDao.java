
package org.gooru.groups.reports.competency.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.competency.group.GroupCompetencyDrillDownReportByGroupOrSchoolModel;
import org.gooru.groups.reports.competency.group.GroupCompetencyReportByGroupCommand;
import org.gooru.groups.reports.competency.group.GroupCompetencyReportByGroupModel;
import org.gooru.groups.reports.competency.group.GroupCompetencyReportBySDorClusterModelMapper;
import org.gooru.groups.reports.competency.group.GroupCompetencySchoolWiseReportBySDorClusterModelMapper;
import org.gooru.groups.reports.competency.school.GroupCompetencyClassWiseReportBySchoolModel;
import org.gooru.groups.reports.competency.school.GroupCompetencyClassWiseReportBySchoolModelMapper;
import org.gooru.groups.reports.competency.school.GroupCompetencyReportBySchoolCommand;
import org.gooru.groups.reports.competency.school.GroupCompetencyReportBySchoolModel;
import org.gooru.groups.reports.competency.school.GroupCompetencyReportBySchoolModelMapper;
import org.gooru.groups.reports.competency.state.GroupCompetencyGroupWiseReportByStateModel;
import org.gooru.groups.reports.competency.state.GroupCompetencyGroupWiseReportByStateModelMapper;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateCommand;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateModel;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateModelMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public interface GroupCompetencyReportDao {
  
  // Group Competency report by State
  
  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM class_performance_data_reports WHERE state_id = :stateId AND"
      + " month = :month AND year = :year")
  Double fetchAveragePerformanceByState(@BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean);
  
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
  
  // Group Competency report by Group
  
  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM group_performance_data_reports WHERE group_id = :groupId AND"
      + " month = :month AND year = :year")
  Double fetchAveragePerformanceByGroup(@BindBean GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean);

  // Fetch drill down report by District Or Block
  @Mapper(GroupCompetencyReportBySDorClusterModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[])"
      + " AND month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportByGroupModel> fetchGroupCompetencyReportByDistrictOrBlock(@Bind("groupIds") String groupIds,
      @BindBean GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean);
  
  @Mapper(GroupCompetencySchoolWiseReportBySDorClusterModelMapper.class)
  @SqlQuery("SELECT group_id as id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[]) AND month = :month AND year = :year GROUP BY group_id")
  List<GroupCompetencyDrillDownReportByGroupOrSchoolModel> fetchGroupCompetencyGroupWiseReportByDistrictOrBlock(
      @Bind("groupIds") String groupIds,
      @BindBean GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean);
  
  // Fetch drill down report by School District or Cluster
  
  @Mapper(GroupCompetencyReportBySDorClusterModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE school_id ="
      + " ANY(:schoolIds::bigint[]) AND month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportByGroupModel> fetchGroupCompetencyReportBySDorCluster(@Bind("schoolIds") String schoolIds,
      @BindBean GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean);
  
  @Mapper(GroupCompetencySchoolWiseReportBySDorClusterModelMapper.class)
  @SqlQuery("SELECT school_id as id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE school_id = ANY(:schoolIds::bigint[]) AND month = :month AND year = :year GROUP BY school_id")
  List<GroupCompetencyDrillDownReportByGroupOrSchoolModel> fetchGroupCompetencySchoolWiseReportBySDorCluster(
      @Bind("schoolIds") String schoolIds,
      @BindBean GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean);

  // Group Competency report by School
  
  @SqlQuery("SELECT AVG(assessment_performance) AS average_performance FROM class_performance_data_reports WHERE school_id = :schoolId AND"
      + " month = :month AND year = :year")
  Double fetchAveragePerformanceBySchool(@BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean);

  @Mapper(GroupCompetencyReportBySchoolModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE school_id = :schoolId AND"
      + " month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportBySchoolModel> fetchGroupCompetencyReportBySchool(
      @BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean);

  @Mapper(GroupCompetencyClassWiseReportBySchoolModelMapper.class)
  @SqlQuery("SELECT class_id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE school_id = :schoolId AND month = :month AND year = :year GROUP BY class_id")
  List<GroupCompetencyClassWiseReportBySchoolModel> fetchGroupCompetencyClassWiseReportBySchool(
      @BindBean GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean);
}

