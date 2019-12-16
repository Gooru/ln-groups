
package org.gooru.groups.reports.perf.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.perf.country.GroupPerfReportByCountryCommand;
import org.gooru.groups.reports.perf.fetchsubject.country.FetchSubjectsForPerfReportByCountryCommand;
import org.gooru.groups.reports.perf.group.GroupPerfReportByGroupCommand;
import org.gooru.groups.reports.perf.school.GroupPerfReportBySchoolCommand;
import org.gooru.groups.reports.perf.state.GroupPerfReportByStateCommand;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public interface GroupReportsDao {

  @Mapper(PerformanceAndTSReportBySchoolModelMapper.class)
  @SqlQuery("SELECT class_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " class_performance_data_reports WHERE school_id = :schoolId AND week = :week AND month = :month AND year = :year AND subject = :subject"
      + " AND framework = :framework GROUP BY class_id")
  List<PerformanceAndTSReportBySchoolModel> fetchPerformanceAndTSWeekReportBySchool(
      @BindBean GroupPerfReportBySchoolCommand.GroupPerfReportBySchoolCommandBean bean);

  @Mapper(PerformanceAndTSReportBySchoolModelMapper.class)
  @SqlQuery("SELECT class_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " class_performance_data_reports WHERE school_id = :schoolId AND month = :month AND year = :year AND subject = :subject AND"
      + " framework = :framework GROUP BY class_id")
  List<PerformanceAndTSReportBySchoolModel> fetchPerformanceAndTSMonthReportBySchool(
      @BindBean GroupPerfReportBySchoolCommand.GroupPerfReportBySchoolCommandBean bean);

  // ---- Performance and Time spent report by group
  @Mapper(PerformanceAndTSReportByGroupModelMapper.class)
  @SqlQuery("SELECT group_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " group_performance_data_reports WHERE group_id = :groupId AND week = :week AND month = :month AND year = :year AND subject = :subject AND"
      + " framework = :framework GROUP BY group_id")
  List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSWeekReportByGroup(
      @BindBean GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean);

  @Mapper(PerformanceAndTSReportByGroupModelMapper.class)
  @SqlQuery("SELECT group_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " group_performance_data_reports WHERE group_id = :groupId AND month = :month AND year = :year AND subject = :subject AND"
      + " framework = :framework GROUP BY group_id")
  List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSMonthReportByGroup(
      @BindBean GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean);

  // ---- Performance and Time spent report by cluster
  @Mapper(PerformanceAndTSReportByClusterModelMapper.class)
  @SqlQuery("SELECT school_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " group_performance_data_reports WHERE group_id = :groupId AND week = :week AND month = :month AND year = :year AND subject = :subject AND"
      + " framework = :framework GROUP BY school_id")
  List<PerformanceAndTSReportByClusterModel> fetchPerformanceAndTSWeekReportByCluster(
      @BindBean GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean);

  @Mapper(PerformanceAndTSReportByClusterModelMapper.class)
  @SqlQuery("SELECT school_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " group_performance_data_reports WHERE group_id = :groupId AND month = :month AND year = :year AND subject = :subject AND"
      + " framework = :framework GROUP BY school_id")
  List<PerformanceAndTSReportByClusterModel> fetchPerformanceAndTSMonthReportByCluster(
      @BindBean GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean);

  // ---- Performance and Time spent report by state
  @Mapper(PerformanceAndTSReportByGroupModelMapper.class)
  @SqlQuery("SELECT group_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " group_performance_data_reports WHERE group_id = ANY(:groupIds::bigint[]) AND state_id = :stateId AND week = :week AND month = :month AND"
      + " year = :year AND subject = :subject AND framework = :framework GROUP BY group_id")
  List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSWeekReportByState(@Bind("groupIds") String groupIds,
      @BindBean GroupPerfReportByStateCommand.GroupPerformanceReportByStateCommandBean bean);

  @Mapper(PerformanceAndTSReportByGroupModelMapper.class)
  @SqlQuery("SELECT group_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " group_performance_data_reports WHERE group_id = ANY(:groupIds::bigint[]) AND state_id = :stateId AND month = :month AND year = :year AND"
      + " subject = :subject AND framework = :framework GROUP BY group_id")
  List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSMonthReportByState(@Bind("groupIds") String groupIds,
      @BindBean GroupPerfReportByStateCommand.GroupPerformanceReportByStateCommandBean bean);

  // ---- Performance and Time spent report by country
  @Mapper(PerformanceAndTSReportByCountryModelMapper.class)
  @SqlQuery("SELECT state_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " class_performance_data_reports WHERE country_id = :countryId AND week = :week AND month = :month AND year = :year AND subject = :subject"
      + " AND framework = :framework GROUP BY state_id")
  List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSWeekReportByCountry(
      @BindBean GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean);

  @Mapper(PerformanceAndTSReportByCountryModelMapper.class)
  @SqlQuery("SELECT state_id, SUM(collection_timespent) AS collection_ts, AVG(assessment_performance) AS assessment_perf FROM"
      + " class_performance_data_reports WHERE country_id = :countryId AND month = :month AND year = :year AND subject = :subject AND framework ="
      + " :framework GROUP BY state_id")
  List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSMonthReportByCountry(
      @BindBean GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean);

  // ---- Fetch subject and framework for the performance report
  @Mapper(SubjectFrameworkModelMapper.class)
  @SqlQuery("SELECT distinct subject, framework FROM class_performance_data_reports WHERE country_id = :countryId AND week = :week AND month = :month"
      + " AND year = :year")
  List<SubjectFrameworkModel> fetchSubectsForPerformanceReportWeekByCountry(
      @BindBean FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean);

  @Mapper(SubjectFrameworkModelMapper.class)
  @SqlQuery("SELECT distinct subject, framework FROM class_performance_data_reports WHERE country_id = :countryId AND month = :month AND year = :year")
  List<SubjectFrameworkModel> fetchSubectsForPerformanceReportMonthByCountry(
      @BindBean FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean);
}
