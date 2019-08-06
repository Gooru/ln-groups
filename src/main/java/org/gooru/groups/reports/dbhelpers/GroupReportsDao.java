
package org.gooru.groups.reports.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.country.perf.GroupReportByCountryCommand;
import org.gooru.groups.reports.group.perf.GroupReportByGroupCommand;
import org.gooru.groups.reports.school.perf.GroupReportBySchoolCommand;
import org.gooru.groups.reports.state.perf.GroupReportByStateCommand;
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
      + " class_performance_data_reports WHERE school_id = :schoolId AND month = :month AND year = :year GROUP BY class_id")
  List<PerformanceAndTSReportBySchoolModel> fetchPerformanceAndTSReportBySchool(
      @BindBean GroupReportBySchoolCommand.GroupReportBySchoolCommandBean bean,
      @Bind("tenant") String tenant);

  @Mapper(PerformanceAndTSReportByGroupModelMapper.class)
  @SqlQuery("SELECT dr.group_id, g.name, g.code, g.sub_type, SUM(dr.collection_timespent) AS collection_ts, AVG(dr.assessment_performance) AS"
      + " assessment_perf FROM group_performance_data_reports dr, groups g WHERE dr.group_id = :groupId AND g.id = dr.group_id AND dr.month = :month AND"
      + " dr.year = :year AND dr.tenant = :tenant GROUP BY dr.group_id, g.name, g.code, g.sub_type")
  List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSReportByGroup(
      @BindBean GroupReportByGroupCommand.GroupReportByGroupCommandBean bean,
      @Bind("tenant") String tenant);

  @Mapper(PerformanceAndTSReportByGroupModelMapper.class)
  @SqlQuery("SELECT dr.group_id, g.name, g.code, g.sub_type, SUM(dr.collection_timespent) AS collection_ts, AVG(dr.assessment_performance) AS"
      + " assessment_perf FROM group_performance_data_reports dr, groups g WHERE dr.state_id = :stateId AND (dr.group_sub_type = 'school_district'"
      + " OR dr.group_sub_type = 'district') AND g.id = dr.group_id AND dr.month = :month AND dr.year = :year AND dr.tenant = :tenant GROUP BY"
      + " dr.group_id, g.name, g.code, g.sub_type")
  List<PerformanceAndTSReportByGroupModel> fetchPerformanceAndTSReportByState(
      @BindBean GroupReportByStateCommand.GroupReportByStateCommandBean bean,
      @Bind("tenant") String tenant);

  @Mapper(PerformanceAndTSReportByCountryModelMapper.class)
  @SqlQuery("SELECT dr.state_id, st.name, st.code, SUM(dr.collection_timespent) AS collection_ts, AVG(dr.assessment_performance) AS assessment_perf"
      + " FROM class_performance_data_reports dr, state st WHERE dr.country_id = :countryId AND st.id = dr.state_id AND month = :month AND year = :year AND"
      + " dr.tenant = :tenant GROUP BY dr.state_id, st.name, st.code")
  List<PerformanceAndTSReportByCountryModel> fetchPerformanceAndTSReportByCountry(
      @BindBean GroupReportByCountryCommand.GroupReportByCountryCommandBean bean,
      @Bind("tenant") String tenant);
}
