
package org.gooru.groups.reports.competency.state;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyGroupWiseReportByStateModel {
  
  private Long groupId;
  private Long completedCompetencies;
  private Long inprogressCompetencies;

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
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

}
