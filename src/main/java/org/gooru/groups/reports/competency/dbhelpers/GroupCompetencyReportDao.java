
package org.gooru.groups.reports.competency.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryCommand;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryModel;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryModelMapper;
import org.gooru.groups.reports.competency.country.GroupCompetencyStateWiseReportByCountryModel;
import org.gooru.groups.reports.competency.country.GroupCompetencyStateWiseReportByCountryModelMapper;
import org.gooru.groups.reports.competency.state.GroupCompetencyGroupWiseReportByStateModel;
import org.gooru.groups.reports.competency.state.GroupCompetencyGroupWiseReportByStateModelMapper;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateCommand;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateModel;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateModelMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public interface GroupCompetencyReportDao {

  @Mapper(GroupCompetencyReportByCountryModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE country_id = :countryId AND"
      + " month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportByCountryModel> fetchGroupCompetencyReportByCountry(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean);

  @Mapper(GroupCompetencyStateWiseReportByCountryModelMapper.class)
  @SqlQuery("SELECT state_id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " class_competency_data_reports WHERE country_id = :countryId AND month = :month AND year = :year GROUP BY state_id")
  List<GroupCompetencyStateWiseReportByCountryModel> fetchGroupCompetencyStateWiseReportByCountry(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean);

  @Mapper(GroupCompetencyReportByStateModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[])"
      + " AND state_id = :stateId AND month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportByStateModel> fetchGroupCompetencyReportByState(
      @Bind("groupIds") String groupIds,
      @BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean);

  @Mapper(GroupCompetencyGroupWiseReportByStateModelMapper.class)
  @SqlQuery("SELECT group_id, SUM(completed_count) as completed_competencies, SUM(inprogress_count) as inprogress_competencies FROM"
      + " group_competency_data_reports WHERE group_id = ANY(:groupIds::bigint[]) AND state_id = :stateId AND month = :month AND year = :year GROUP"
      + " BY group_id")
  List<GroupCompetencyGroupWiseReportByStateModel> fetchGroupCompetencyGroupWiseReportByState(
      @Bind("groupIds") String groupIds,
      @BindBean GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean);

}

