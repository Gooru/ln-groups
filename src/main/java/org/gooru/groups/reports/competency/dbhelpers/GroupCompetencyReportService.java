
package org.gooru.groups.reports.competency.dbhelpers;

import java.util.List;
import java.util.Set;
import org.gooru.groups.reports.competency.group.GroupCompetencyDrillDownReportByGroupOrSchoolModel;
import org.gooru.groups.reports.competency.group.GroupCompetencyReportByGroupCommand;
import org.gooru.groups.reports.competency.group.GroupCompetencyReportByGroupModel;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportService {

  private final GroupCompetencyReportDao dao;

  public GroupCompetencyReportService(DBI dbi) {
    this.dao = dbi.onDemand(GroupCompetencyReportDao.class);
  }

  public Double fetchAveragePerformanceByGroup(
      GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean) {
    return this.dao.fetchAveragePerformanceByGroup(bean);
  }

  public List<GroupCompetencyReportByGroupModel> fetchGroupCompetencyReportByDistrictOrBlock(
      Set<Long> groupIds,
      GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean) {
    return this.dao.fetchGroupCompetencyReportByDistrictOrBlock(
        CollectionUtils.toPostgresArrayLong(groupIds), bean);
  }

  public List<GroupCompetencyDrillDownReportByGroupOrSchoolModel> fetchGroupCompetencyGroupWiseReportByDistrictOrBlock(
      Set<Long> groupIds,
      GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean) {
    return this.dao.fetchGroupCompetencyGroupWiseReportByDistrictOrBlock(
        CollectionUtils.toPostgresArrayLong(groupIds), bean);
  }

  public List<GroupCompetencyReportByGroupModel> fetchGroupCompetencyReportBySDorCluster(
      Set<Long> schoolIds,
      GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean) {
    return this.dao.fetchGroupCompetencyReportBySDorCluster(
        CollectionUtils.toPostgresArrayLong(schoolIds), bean);
  }

  public List<GroupCompetencyDrillDownReportByGroupOrSchoolModel> fetchGroupCompetencySchoolWiseReportBySDorCluster(
      Set<Long> schoolIds,
      GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean) {
    return this.dao.fetchGroupCompetencySchoolWiseReportBySDorCluster(
        CollectionUtils.toPostgresArrayLong(schoolIds), bean);
  }

}
