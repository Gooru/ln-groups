
package org.gooru.groups.reports.dbhelpers;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class PerformanceAndTSReportByClusterModel {
  private Long schoolId;
  private Long timespent;
  private Double performance;

  public Long getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(Long schoolId) {
    this.schoolId = schoolId;
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
