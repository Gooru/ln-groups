
package org.gooru.groups.reports.competency.country;

import java.util.List;

/**
 * @author szgooru Created On 14-Dec-2019
 */
public class GroupCompetencyReportByCountryResponseModel {

  private OverallStats overallStats;
  private List<Data> data;
  private List<Drilldown> drilldown;

  public OverallStats getOverallStats() {
    return overallStats;
  }

  public void setOverallStats(OverallStats overallStats) {
    this.overallStats = overallStats;
  }

  public List<Data> getData() {
    return data;
  }

  public void setData(List<Data> data) {
    this.data = data;
  }

  public List<Drilldown> getDrilldown() {
    return drilldown;
  }

  public void setDrilldown(List<Drilldown> drilldown) {
    this.drilldown = drilldown;
  }

  static class OverallStats {
    private Long totalCompetencies;

    public Long getTotalCompetencies() {
      return totalCompetencies;
    }

    public void setTotalCompetencies(Long totalCompetencies) {
      this.totalCompetencies = totalCompetencies;
    }

  }

  static class Data {
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

  static class Drilldown {
    private Long id;
    private String name;
    private String code;
    private String type;
    private String subType;
    private Long completedCompetencies;

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

    public Long getCompletedCompetencies() {
      return completedCompetencies;
    }

    public void setCompletedCompetencies(Long completedCompetencies) {
      this.completedCompetencies = completedCompetencies;
    }

  }

}
