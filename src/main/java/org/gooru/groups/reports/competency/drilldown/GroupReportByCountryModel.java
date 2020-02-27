package org.gooru.groups.reports.competency.drilldown;

/**
 * @author szgooru on 20-Feb-2020
 *
 */
public class GroupReportByCountryModel {
  
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
