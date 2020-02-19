
package org.gooru.groups.reports.perf.dbhelpers;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class PerformanceAndTSReportByCountryModel {
  private Long stateId;
  private Long timespent;
  private Double performance;

  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
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
