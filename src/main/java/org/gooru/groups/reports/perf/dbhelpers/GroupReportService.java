
package org.gooru.groups.reports.perf.dbhelpers;

import java.util.List;
import java.util.Set;
import org.gooru.groups.reports.perf.country.GroupPerfReportByCountryCommand;
import org.gooru.groups.reports.perf.fetchsubject.country.FetchSubjectsForPerfReportByCountryCommand;
import org.gooru.groups.reports.perf.group.GroupPerfReportByGroupCommand;
import org.gooru.groups.reports.perf.school.GroupPerfReportBySchoolCommand;
import org.gooru.groups.reports.perf.state.GroupPerfReportByStateCommand;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupReportService {

  private final GroupReportsDao reportDao;

  public GroupReportService(DBI dsdbDBI) {
    this.reportDao = dsdbDBI.onDemand(GroupReportsDao.class);
  }

  public List<PerformanceAndTSReportBySchoolModel> fetchPerformanceAndTSWeekReportBySchool(
      GroupPerfReportBySchoolCommand.GroupPerfReportBySchoolCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSWeekReportBySchool(bean);
  }

  public List<PerformanceAndTSReportBySchoolModel> fetchPerformanceAndTSMonthReportBySchool(
      GroupPerfReportBySchoolCommand.GroupPerfReportBySchoolCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSMonthReportBySchool(bean);
  }

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSWeekReportByDistrictOrBlock(
      Set<Long> groupIds, GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSWeekReportByDistrictOrBlock(
        CollectionUtils.toPostgresArrayLong(groupIds), bean);
  }

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSMonthReportByDistrictOrBlock(
      Set<Long> groupIds, GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSMonthReportByDistrictOrBlock(
        CollectionUtils.toPostgresArrayLong(groupIds), bean);
  }

  public List<PerformanceAndTSReportByClusterModel> fetchPerformanceAndTSWeekReportBySDorCluster(
      Set<Long> schoolIds, GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSWeekReportBySDorCluster(
        CollectionUtils.toPostgresArrayLong(schoolIds), bean);
  }

  public List<PerformanceAndTSReportByClusterModel> fetchPerformanceAndTSMonthReportBySDorCluster(
      Set<Long> schoolIds, GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSMonthReportBySDorCluster(
        CollectionUtils.toPostgresArrayLong(schoolIds), bean);
  }

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSWeekReportByState(
      Set<Long> groupIds,
      GroupPerfReportByStateCommand.GroupPerformanceReportByStateCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSWeekReportByState(
        CollectionUtils.toPostgresArrayLong(groupIds), bean);
  }

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSMonthReportByState(
      Set<Long> groupIds,
      GroupPerfReportByStateCommand.GroupPerformanceReportByStateCommandBean bean) {
    return this.reportDao.fetchPerformanceAndTSMonthReportByState(
        CollectionUtils.toPostgresArrayLong(groupIds), bean);
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
