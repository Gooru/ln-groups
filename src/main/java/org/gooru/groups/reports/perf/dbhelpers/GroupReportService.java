
package org.gooru.groups.reports.perf.dbhelpers;

import java.util.List;
import java.util.Set;
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

  public List<PerformanceAndTSReportBySchoolModel> fetchPerformanceAndTSWeekReportBySchoolAndTenant(
      GroupPerfReportBySchoolCommand.GroupPerfReportBySchoolCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchPerformanceAndTSWeekReportBySchoolAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<PerformanceAndTSReportBySchoolModel> fetchPerformanceAndTSMonthReportBySchoolAndTenant(
      GroupPerfReportBySchoolCommand.GroupPerfReportBySchoolCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchPerformanceAndTSMonthReportBySchoolAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
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

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSWeekReportByDistrictOrBlockAndTenant(
      Set<Long> groupIds, GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchPerformanceAndTSWeekReportByDistrictOrBlockAndTenant(
        CollectionUtils.toPostgresArrayLong(groupIds), bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSMonthReportByDistrictOrBlockAndTenant(
      Set<Long> groupIds, GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchPerformanceAndTSMonthReportByDistrictOrBlockAndTenant(
        CollectionUtils.toPostgresArrayLong(groupIds), bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
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

  public List<PerformanceAndTSReportByClusterModel> fetchPerformanceAndTSWeekReportBySDorClusterAndTenant(
      Set<Long> schoolIds, GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchPerformanceAndTSWeekReportBySDorClusterAndTenant(
        CollectionUtils.toPostgresArrayLong(schoolIds), bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<PerformanceAndTSReportByClusterModel> fetchPerformanceAndTSMonthReportBySDorClusterAndTenant(
      Set<Long> schoolIds, GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchPerformanceAndTSMonthReportBySDorClusterAndTenant(
        CollectionUtils.toPostgresArrayLong(schoolIds), bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
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

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSWeekReportByStateAndTenant(
      Set<Long> groupIds,
      GroupPerfReportByStateCommand.GroupPerformanceReportByStateCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchPerformanceAndTSWeekReportByStateAndTenant(
        CollectionUtils.toPostgresArrayLong(groupIds), bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSMonthReportByStateAndTenant(
      Set<Long> groupIds,
      GroupPerfReportByStateCommand.GroupPerformanceReportByStateCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchPerformanceAndTSMonthReportByStateAndTenant(
        CollectionUtils.toPostgresArrayLong(groupIds), bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<SubjectFrameworkModel> fetchSubjectsForPerfReportWeekByCountry(
      FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean) {
    return this.reportDao.fetchSubectsForPerformanceReportWeekByCountry(bean);
  }

  public List<SubjectFrameworkModel> fetchSubjectsForPerfReportMonthByCountry(
      FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean) {
    return this.reportDao.fetchSubectsForPerformanceReportMonthByCountry(bean);
  }

  public List<SubjectFrameworkModel> fetchSubjectsForPerfReportWeekByCountryAndTenant(
      FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchSubectsForPerformanceReportWeekByCountryAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }

  public List<SubjectFrameworkModel> fetchSubjectsForPerfReportMonthByCountryAndTenant(
      FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean,
      Set<String> tenantIds) {
    return this.reportDao.fetchSubectsForPerformanceReportMonthByCountryAndTenant(bean,
        CollectionUtils.convertToSqlArrayOfString(tenantIds));
  }
}
