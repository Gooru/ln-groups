
package org.gooru.groups.reports.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.country.perf.GroupReportByCountryCommand;
import org.gooru.groups.reports.group.perf.GroupReportByGroupCommand;
import org.gooru.groups.reports.school.perf.GroupReportBySchoolCommand;
import org.gooru.groups.reports.state.perf.GroupReportByStateCommand;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupReportService {

  private final GroupReportsDao reportDao;

  public GroupReportService(DBI dsdbDBI) {
    this.reportDao = dsdbDBI.onDemand(GroupReportsDao.class);
  }

  public List<PerformanceAndTSReportBySchoolModel> fetchPerformanceAndTSReportBySchool(
      GroupReportBySchoolCommand.GroupReportBySchoolCommandBean bean, String tenant) {
    return this.reportDao.fetchPerformanceAndTSReportBySchool(bean, tenant);
  }

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSReportByGroup(
      GroupReportByGroupCommand.GroupReportByGroupCommandBean bean, String tenant) {
    return this.reportDao.fetchPerformanceAndTSReportByGroup(bean, tenant);
  }

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSReportByState(
      GroupReportByStateCommand.GroupReportByStateCommandBean bean, String tenant) {
    return this.reportDao.fetchPerformanceAndTSReportByState(bean, tenant);
  }

  public List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSReportByCountry(
      GroupReportByCountryCommand.GroupReportByCountryCommandBean bean, String tenant) {
    return this.reportDao.fetchPerformanceAndTSReportByCountry(bean, tenant);
  }
}
