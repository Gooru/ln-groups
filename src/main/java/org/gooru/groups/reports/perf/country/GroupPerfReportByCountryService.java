package org.gooru.groups.reports.perf.country;

import java.util.List;
import java.util.Set;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByCountryModel;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 04-Feb-2020
 *
 */
public class GroupPerfReportByCountryService {

  private final GroupPerfReportByCountryDao dao;

  public GroupPerfReportByCountryService(DBI dbi) {
    this.dao = dbi.onDemand(GroupPerfReportByCountryDao.class);
  }

  public List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSWeekReportByCountry(
      GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean) {
    return this.dao.fetchPerformanceAndTSWeekReportByCountry(bean);
  }

  public List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSMonthReportByCountry(
      GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean) {
    return this.dao.fetchPerformanceAndTSMonthReportByCountry(bean);
  }

  public List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSWeekReportByCountryAndTenant(
      GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean, Set<String> tenantIds) {
    return this.dao.fetchPerformanceAndTSWeekReportByCountryAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSMonthReportByCountryAndTenant(
      GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean, Set<String> tenantIds) {
    return this.dao.fetchPerformanceAndTSMonthReportByCountryAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }
}
