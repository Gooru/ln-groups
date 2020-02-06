package org.gooru.groups.reports.competency.school;

import java.util.List;
import java.util.Set;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 06-Feb-2020
 *
 */
public class GroupCompetencyReportBySchoolService {

  private final GroupCompetencyReportBySchoolDao dao;

  public GroupCompetencyReportBySchoolService(DBI dbi) {
    this.dao = dbi.onDemand(GroupCompetencyReportBySchoolDao.class);
  }

  public Double fetchAveragePerformanceBySchool(
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean) {
    return this.dao.fetchAveragePerformanceBySchool(bean);
  }

  public List<GroupCompetencyReportBySchoolModel> fetchGroupCompetencyReportBySchool(
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean) {
    return this.dao.fetchGroupCompetencyReportBySchool(bean);
  }

  public List<GroupCompetencyClassWiseReportBySchoolModel> fetchGroupCompetencyClassWiseReportBySchool(
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean) {
    return this.dao.fetchGroupCompetencyClassWiseReportBySchool(bean);
  }

  public Double fetchAveragePerformanceBySchoolAndTenant(
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchAveragePerformanceBySchoolAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<GroupCompetencyReportBySchoolModel> fetchGroupCompetencyReportBySchoolAndTenant(
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchGroupCompetencyReportBySchoolAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<GroupCompetencyClassWiseReportBySchoolModel> fetchGroupCompetencyClassWiseReportBySchoolAndTenant(
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchGroupCompetencyClassWiseReportBySchoolAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }
}
