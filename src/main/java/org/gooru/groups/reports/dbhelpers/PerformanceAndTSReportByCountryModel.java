
package org.gooru.groups.reports.dbhelpers;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class PerformanceAndTSReportByCountryModel {
  private Integer stateId;
  private String name;
  private String code;
  private Long timespent;
  private Double performance;

  public Integer getStateId() {
    return stateId;
  }

  public void setStateId(Integer stateId) {
    this.stateId = stateId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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
