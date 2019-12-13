
package org.gooru.groups.reports.perf.group;

import java.util.List;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupPerfReportByClusterResponseModel {
  private List<ClusterResponseModel> data;
  private OverallClusterStats overallStats;

  public List<ClusterResponseModel> getData() {
    return data;
  }

  public void setData(List<ClusterResponseModel> data) {
    this.data = data;
  }

  public OverallClusterStats getOverallStats() {
    return overallStats;
  }

  public void setOverallStats(OverallClusterStats overallStats) {
    this.overallStats = overallStats;
  }

  static class OverallClusterStats {
    private Double averagePerformance;

    public Double getAveragePerformance() {
      return averagePerformance;
    }

    public void setAveragePerformance(Double averagePerformance) {
      this.averagePerformance = averagePerformance;
    }
  }

  static class ClusterResponseModel {
    private Long id;
    private String name;
    private String code;
    private String type;
    private String subType;
    private Long timespent;
    private Double performance;
    private Long completedCompetencies;
    private Long inprogressCompetencies;

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

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getSubType() {
      return subType;
    }

    public void setSubType(String subType) {
      this.subType = subType;
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
}
