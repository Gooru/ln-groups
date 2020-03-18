
package org.gooru.groups.reports.competency.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.competency.group.GroupCompetencyDrillDownReportByGroupOrSchoolModel;
import org.gooru.groups.reports.competency.group.GroupCompetencyReportByGroupCommand;
import org.gooru.groups.reports.competency.group.GroupCompetencyReportByGroupModel;
import org.gooru.groups.reports.competency.group.GroupCompetencyReportBySDorClusterModelMapper;
import org.gooru.groups.reports.competency.group.GroupCompetencySchoolWiseReportBySDorClusterModelMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public interface GroupCompetencyReportDao {
  
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

}

