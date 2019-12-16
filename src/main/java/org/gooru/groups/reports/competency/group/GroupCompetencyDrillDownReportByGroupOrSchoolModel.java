
package org.gooru.groups.reports.competency.group;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyDrillDownReportByGroupOrSchoolModel {

  private Long id;
  private Long completedCompetencies;
  private Long inprogressCompetencies;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
