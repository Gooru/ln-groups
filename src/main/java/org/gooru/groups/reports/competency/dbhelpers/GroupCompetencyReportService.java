
package org.gooru.groups.reports.competency.dbhelpers;

import java.util.List;
import java.util.Set;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryCommand;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryModel;
import org.gooru.groups.reports.competency.country.GroupCompetencyStateWiseReportByCountryModel;
import org.gooru.groups.reports.competency.school.GroupCompetencyClassWiseReportBySchoolModel;
import org.gooru.groups.reports.competency.school.GroupCompetencyReportBySchoolCommand;
import org.gooru.groups.reports.competency.school.GroupCompetencyReportBySchoolModel;
import org.gooru.groups.reports.competency.state.GroupCompetencyGroupWiseReportByStateModel;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateCommand;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateModel;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportService {

  private final GroupCompetencyReportDao dao;

  public GroupCompetencyReportService(DBI dbi) {
    this.dao = dbi.onDemand(GroupCompetencyReportDao.class);
  }

  public List<GroupCompetencyReportByCountryModel> fetchGroupCompetencyReportByCountry(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean) {
    return this.dao.fetchGroupCompetencyReportByCountry(bean);
  }

  public List<GroupCompetencyStateWiseReportByCountryModel> fethcGroupCompetencyStateWiseReportByCountry(
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean) {
    return this.dao.fetchGroupCompetencyStateWiseReportByCountry(bean);
  }

  public List<GroupCompetencyReportByStateModel> fetchGroupCompetencyReportByState(
      Set<Long> groupIds,
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean) {
    return this.dao.fetchGroupCompetencyReportByState(CollectionUtils.toPostgresArrayLong(groupIds),
        bean);
  }

  public List<GroupCompetencyGroupWiseReportByStateModel> fetchGroupCompetencyGroupWiseReportByState(
      Set<Long> groupIds,
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean) {
    return this.dao.fetchGroupCompetencyGroupWiseReportByState(
        CollectionUtils.toPostgresArrayLong(groupIds), bean);
  }

  public List<GroupCompetencyReportBySchoolModel> fetchGroupCompetencyReportBySchool(
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean) {
    return this.dao.fetchGroupCompetencyReportBySchool(bean);
  }

  public List<GroupCompetencyClassWiseReportBySchoolModel> fetchGroupCompetencyClassWiseReportBySchool(
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean) {
    return this.dao.fetchGroupCompetencyClassWiseReportBySchool(bean);
  }

}
