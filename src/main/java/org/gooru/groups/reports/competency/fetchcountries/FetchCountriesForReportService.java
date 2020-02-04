package org.gooru.groups.reports.competency.fetchcountries;

import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 04-Feb-2020
 *
 */
public class FetchCountriesForReportService {

  private final FetchCountriesForReportDao dao;

  public FetchCountriesForReportService(DBI dbi) {
    this.dao = dbi.onDemand(FetchCountriesForReportDao.class);
  }

  public List<FetchCountriesForReportModel> fetchCompetencyCounts(
      FetchCountriesForReportCommand.FetchCountriesForReportCommandBean bean) {
    if (bean.getMonth() == null && bean.getYear() == null) {
      return this.dao.fetchCompetencyCountsAllTime(bean);
    } else {
      return this.dao.fetchCompetencyCountsByMonthYear(bean);
    }
  }

  public List<FetchCountriesForReportModel> fetchCompetencyCountsByTenant(
      FetchCountriesForReportCommand.FetchCountriesForReportCommandBean bean) {
    if (bean.getMonth() == null && bean.getYear() == null) {
      return this.dao.fetchCompetencyCountsByTenantAllTime(bean);
    } else {
      return this.dao.fetchCompetencyCountsByTenantMonthYear(bean);
    }
  }
}
