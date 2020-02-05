package org.gooru.groups.reports.perf.country;

import java.util.List;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByCountryModel;
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
}
