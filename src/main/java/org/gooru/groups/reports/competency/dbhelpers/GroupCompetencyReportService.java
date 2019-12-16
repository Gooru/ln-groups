
package org.gooru.groups.reports.competency.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryCommand;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryModel;
import org.gooru.groups.reports.competency.country.GroupCompetencyStateWiseReportByCountryModel;
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

}
