package org.gooru.groups.reports.competency.state;

import java.util.List;
import java.util.Set;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 06-Feb-2020
 *
 */
public class GroupCompetencyReportByStateService {

  private final GroupCompetencyReportByStateDao dao;

  public GroupCompetencyReportByStateService(DBI dbi) {
    this.dao = dbi.onDemand(GroupCompetencyReportByStateDao.class);
  }

  public Double fetchAveragePerformanceByState(
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean) {
    return this.dao.fetchAveragePerformanceByState(bean);
  }

  public List<GroupCompetencyReportByStateModel> fetchGroupCompetencyReportByState(
      Set<Long> groupIds,
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean) {
    return this.dao.fetchGroupCompetencyReportByState(CollectionUtils.toPostgresArrayLong(groupIds),
        bean);
  }

  public List<GroupCompetencyGroupWiseReportByStateModel> fetchGroupCompetencyGroupWiseReportByState(
      Set<Long> groupIds,
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean) {
    return this.dao.fetchGroupCompetencyGroupWiseReportByState(
        CollectionUtils.toPostgresArrayLong(groupIds), bean);
  }

  public Double fetchAveragePerformanceByStateAndTenant(
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchAveragePerformanceByStateAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<GroupCompetencyReportByStateModel> fetchGroupCompetencyReportByStateAndTenant(
      Set<Long> groupIds,
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchGroupCompetencyReportByStateAndTenant(
        CollectionUtils.toPostgresArrayLong(groupIds), bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<GroupCompetencyGroupWiseReportByStateModel> fetchGroupCompetencyGroupWiseReportByStateAndTenant(
      Set<Long> groupIds,
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchGroupCompetencyGroupWiseReportByStateAndTenant(
        CollectionUtils.toPostgresArrayLong(groupIds), bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }
}
