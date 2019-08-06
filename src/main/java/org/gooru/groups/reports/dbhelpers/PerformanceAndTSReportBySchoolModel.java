
package org.gooru.groups.reports.dbhelpers;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class PerformanceAndTSReportBySchoolModel {

  private String classId;
  private Long timespent;
  private Double performance;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
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
