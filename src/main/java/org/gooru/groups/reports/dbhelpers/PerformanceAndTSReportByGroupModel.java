
package org.gooru.groups.reports.dbhelpers;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class PerformanceAndTSReportByGroupModel {
  private Long groupId;
  private Long timespent;
  private Double performance;

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getTimespent() {
    return timespent;
  }

  public void setTimespent(Long timespent) {
    this.timespent = timespent;
  }

  public Double getPerformance() {
    return performance;
  }

  public void setPerformance(Double performance) {
    this.performance = performance;
  }
}
