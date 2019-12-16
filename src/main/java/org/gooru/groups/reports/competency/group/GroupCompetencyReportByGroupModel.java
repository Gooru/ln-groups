
package org.gooru.groups.reports.competency.group;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportByGroupModel {

  private Integer week;
  private Long completedCompetencies;

  public Integer getWeek() {
    return week;
  }

  public void setWeek(Integer week) {
    this.week = week;
  }

  public Long getCompletedCompetencies() {
    return completedCompetencies;
  }

  public void setCompletedCompetencies(Long completedCompetencies) {
    this.completedCompetencies = completedCompetencies;
  }

}
