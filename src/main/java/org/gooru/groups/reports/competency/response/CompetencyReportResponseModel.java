package org.gooru.groups.reports.competency.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 19-Feb-2020
 *
 */
public class CompetencyReportResponseModel {

  @JsonProperty("context")
  private Context context;

  @JsonProperty("overall_stats")
  private OverallStats overallStats;

  @JsonProperty("data")
  private List<Data> data;

  @JsonProperty("drilldown")
  private List<Drilldown> drilldown;
  
  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

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

  public static class Context {

    @JsonProperty("report")
    private String report;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_type")
    private String groupType;

    @JsonProperty("month")
    private Integer month;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("frequency")
    private String frequency;

    public String getReport() {
      return report;
    }

    public void setReport(String report) {
      this.report = report;
    }

    public Long getGroupId() {
      return groupId;
    }

    public void setGroupId(Long groupId) {
      this.groupId = groupId;
    }

    public String getGroupType() {
      return groupType;
    }

    public void setGroupType(String groupType) {
      this.groupType = groupType;
    }

    public Integer getMonth() {
      return month;
    }

    public void setMonth(Integer month) {
      this.month = month;
    }

    public Integer getYear() {
      return year;
    }

    public void setYear(Integer year) {
      this.year = year;
    }

    public String getFrequency() {
      return frequency;
    }

    public void setFrequency(String frequency) {
      this.frequency = frequency;
    }

  }

  public static class OverallStats {

    @JsonProperty("total_competencies")
    private Long totalCompetencies;

    @JsonProperty("average_performance")
    private Double averagePerformance;

    public Long getTotalCompetencies() {
      return totalCompetencies;
    }

    public void setTotalCompetencies(Long totalCompetencies) {
      this.totalCompetencies = totalCompetencies;
    }

    public Double getAveragePerformance() {
      return averagePerformance;
    }

    public void setAveragePerformance(Double averagePerformance) {
      this.averagePerformance = averagePerformance;
    }

  }

  public static class Data {
    @JsonProperty("coordinate")
    private Integer coordinate;

    @JsonProperty("completed_competencies")
    private Long completedCompetencies;

    public Integer getCoordinate() {
      return coordinate;
    }

    public void setCoordinate(Integer coordinate) {
      this.coordinate = coordinate;
    }

    public Long getCompletedCompetencies() {
      return completedCompetencies;
    }

    public void setCompletedCompetencies(Long completedCompetencies) {
      this.completedCompetencies = completedCompetencies;
    }
  }

  public static class Drilldown {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("code")
    private String code;

    @JsonProperty("type")
    private String type;

    @JsonProperty("completed_competencies")
    private Long completedCompetencies;

    @JsonProperty("inprogress_competencies")
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
