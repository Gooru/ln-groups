
package org.gooru.groups.reports.competency.country;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyStateWiseReportByCountryModel {

  private Long stateId;
  private Long completedCompetencies;
  private Long inprogressCompetencies;

  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public Long getCompletedCompetencies() {
    return completedCompetencies;
  }

  public void setCompletedCompetencies(Long completedCompetencies) {
    this.completedCompetencies = completedCompetencies;
  }

  public Long getInprogressCompetencies() {
    return inprogressCompetencies;
  }

  public void setInprogressCompetencies(Long inprogressCompetencies) {
    this.inprogressCompetencies = inprogressCompetencies;
  }

  @Override
  public String toString() {
    return "GroupCompetencyStateWiseReportByCountryModel [stateId=" + stateId
        + ", completedCompetencies=" + completedCompetencies + ", inprogressCompetencies="
        + inprogressCompetencies + "]";
  }
  
}
