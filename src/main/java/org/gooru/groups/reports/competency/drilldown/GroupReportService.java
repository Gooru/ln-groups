package org.gooru.groups.reports.competency.drilldown;

import java.util.List;
import java.util.Set;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 17-Feb-2020
 *
 */
public class GroupReportService {

  private final GroupReportDao dao;

  public GroupReportService(DBI dbi) {
    this.dao = dbi.onDemand(GroupReportDao.class);
  }

  public List<GroupReportByCountryModel> fetchCompetencyCounts(
      GroupReportCommand.GroupReportCommandBean bean) {
    if (bean.getMonth() == null && bean.getYear() == null) {
      return this.dao.fetchCompetencyCountsAllTime();
    } else {
      return this.dao.fetchCompetencyCountsByMonthYear(bean);
    }
  }

  public List<GroupReportByCountryModel> fetchCompetencyCountsByCountry(
      GroupReportCommand.GroupReportCommandBean bean,
      Set<Long> countryIds) {
    if (bean.getMonth() == null && bean.getYear() == null) {
      return this.dao.fetchCompetencyCountsByCountryAllTime(bean,
          CollectionUtils.convertToSqlArrayOfLong(countryIds));
    } else {
      return this.dao.fetchCompetencyCountsByMonthYearAndCountry(bean,
          CollectionUtils.convertToSqlArrayOfLong(countryIds));
    }
  }
}
