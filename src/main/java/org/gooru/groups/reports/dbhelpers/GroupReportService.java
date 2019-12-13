
package org.gooru.groups.reports.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.perf.fetchsubject.country.FetchSubjectsForPerfReportByCountryCommand;
import org.gooru.groups.reports.perf.group.GroupReportByGroupCommand;
import org.gooru.groups.reports.perf.school.GroupReportBySchoolCommand;
import org.gooru.groups.reports.perf.state.GroupReportByStateCommand;
import org.gooru.groups.reports.perfcountry.GroupPerfReportByCountryCommand;
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

  public List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSMonthReportByCountry(
      GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSMonthReportByCountry(bean);
  }

  public List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSWeekReportByCountry(
      GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSWeekReportByCountry(bean);
  }

  public List<SubjectFrameworkModel> fetchSubjectsForPerfReportWeekByCountry(
      FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean) {
    return this.reportDao.fetchSubectsForPerformanceReportWeekByCountry(bean);
  }

  public List<SubjectFrameworkModel> fetchSubjectsForPerfReportMonthByCountry(
      FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean) {
    return this.reportDao.fetchSubectsForPerformanceReportMonthByCountry(bean);
  }
}
