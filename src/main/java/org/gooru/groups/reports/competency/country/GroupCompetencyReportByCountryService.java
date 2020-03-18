package org.gooru.groups.reports.competency.country;

import java.util.List;
import java.util.Set;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 06-Feb-2020
 *
 */
public class GroupCompetencyReportByCountryService {

  private final GroupCompetencyReportByCountryDao dao;

  public GroupCompetencyReportByCountryService(DBI dbi) {
    this.dao = dbi.onDemand(GroupCompetencyReportByCountryDao.class);
  }

  public Double fetchAveragePerformanceByCountry(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean) {
    return this.dao.fetchAveragePerformanceByCountry(bean);
  }

  public Double fetchAveragePerformanceByCountryAndTenant(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchAveragePerformanceByCountryAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<GroupCompetencyReportByCountryModel> fetchGroupCompetencyReportByCountry(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean) {
    return this.dao.fetchGroupCompetencyReportByCountry(bean);
  }

  public List<GroupCompetencyReportByCountryModel> fetchGroupCompetencyReportByCountryAndTenant(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchGroupCompetencyReportByCountryAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<GroupCompetencyDrilldownReportByCountryModel> fetchGroupCompetencyStateWiseReportByCountry(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean) {
    return this.dao.fetchGroupCompetencyStateWiseReportByCountry(bean);
  }

  public List<GroupCompetencyDrilldownReportByCountryModel> fetchGroupCompetencyStateWiseReportByCountryAndTenant(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchGroupCompetencyStateWiseReportByCountryAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<GroupCompetencyDrilldownReportByCountryModel> fetchGroupCompetencySchoolWiseReportByCountry(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean) {
    return this.dao.fetchGroupCompetencySchoolWiseReportByCountry(bean);
  }

  public List<GroupCompetencyDrilldownReportByCountryModel> fetchGroupCompetencySchoolWiseReportByCountryAndTenant(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean,
      Set<String> tenantIds) {
    return this.dao.fetchGroupCompetencySchoolWiseReportByCountryAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }
}
