
package org.gooru.groups.reports.competency.school;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyClassWiseReportBySchoolModel {
  private String classId;
  private Long completedCompetencies;
  private Long inprogressCompetencies;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
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
