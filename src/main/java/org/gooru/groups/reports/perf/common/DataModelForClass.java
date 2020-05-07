package org.gooru.groups.reports.perf.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class DataModelForClass {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("code")
  private String code;

  @JsonProperty("type")
  private String type;

  @JsonProperty("overall_stats")
  private GroupPerformanceOverallStatsModel overallStats;

  @JsonProperty("coordinates")
  private List<GroupPerformanceWeeklyDataModel> coordinates;

  @JsonProperty("drilldown")
  private List<GroupPerformanceAggregatedDataReportForClassModel> drilldown;

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

  public GroupPerformanceOverallStatsModel getOverallStats() {
    return overallStats;
  }

  public void setOverallStats(GroupPerformanceOverallStatsModel overallStats) {
    this.overallStats = overallStats;
  }

  public List<GroupPerformanceWeeklyDataModel> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<GroupPerformanceWeeklyDataModel> coordinates) {
    this.coordinates = coordinates;
  }

  public List<GroupPerformanceAggregatedDataReportForClassModel> getDrilldown() {
    return drilldown;
  }

  public void setDrilldown(List<GroupPerformanceAggregatedDataReportForClassModel> drilldown) {
    this.drilldown = drilldown;
  }

}
