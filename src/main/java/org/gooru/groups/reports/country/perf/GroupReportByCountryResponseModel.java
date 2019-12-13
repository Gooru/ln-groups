
package org.gooru.groups.reports.country.perf;

import java.util.List;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupReportByCountryResponseModel {
  private List<StateLevelPerf> states;

  public List<StateLevelPerf> getStates() {
    return states;
  }

  public void setStates(List<StateLevelPerf> states) {
    this.states = states;
  }

  static class StateLevelPerf {
    private Long id;
    private String name;
    private String code;
    private Long timespent;
    private Double performance;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
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
}
