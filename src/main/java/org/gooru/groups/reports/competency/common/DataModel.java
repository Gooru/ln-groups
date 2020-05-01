package org.gooru.groups.reports.competency.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 24-Apr-2020
 *
 */
public class DataModel {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;
  
  @JsonProperty("code")
  private String code;
  
  @JsonProperty("type")
  private String type;
  
  @JsonProperty("overall_stats")
  private OverallStatsModel overallStats;
  
  @JsonProperty("coordinates")
  private List<WeekDataModel> coordinates;
  
  @JsonProperty("drilldown")
  private List<AggregatedDataGroupReportModel> drilldown;

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
  
  public OverallStatsModel getOverallStats() {
    return overallStats;
  }

  public void setOverallStats(OverallStatsModel overallStats) {
    this.overallStats = overallStats;
  }

  public List<WeekDataModel> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<WeekDataModel> coordinates) {
    this.coordinates = coordinates;
  }

  public List<AggregatedDataGroupReportModel> getDrilldown() {
    return drilldown;
  }

  public void setDrilldown(List<AggregatedDataGroupReportModel> drilldown) {
    this.drilldown = drilldown;
  }
  
}
