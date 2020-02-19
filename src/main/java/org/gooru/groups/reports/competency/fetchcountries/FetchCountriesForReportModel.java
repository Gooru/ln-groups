package org.gooru.groups.reports.competency.fetchcountries;

/**
 * @author szgooru on 31-Jan-2020
 *
 */
public class FetchCountriesForReportModel {

  private Long completedCompetencies;
  private Long countryId;

  public Long getCompletedCompetencies() {
    return completedCompetencies;
  }

  public void setCompletedCompetencies(Long completedCompetencies) {
    this.completedCompetencies = completedCompetencies;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

}
