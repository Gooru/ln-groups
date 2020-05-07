package org.gooru.groups.reports.perf.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceAggregatedDataReportModel {

  @JsonProperty("id")
  private Long groupId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("code")
  private String code;

  @JsonProperty("type")
  private String type;

  @JsonProperty("average_performance")
  private Double averagePerformance;

  @JsonProperty("total_collection_timespent")
  private Long totalCollectionTimespent;

  @JsonProperty("total_assessment_timespent")
  private Long totalAssessmentTimespent;

  @JsonProperty("average_collection_timespent")
  private Double averageCollectionTimespent;

  @JsonProperty("average_assessment_timespent")
  private Double averageAssessmentTimespent;

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
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

  public Double getAveragePerformance() {
    return averagePerformance;
  }

  public void setAveragePerformance(Double averagePerformance) {
    this.averagePerformance = averagePerformance;
  }

  public Long getTotalCollectionTimespent() {
    return totalCollectionTimespent;
  }

  public void setTotalCollectionTimespent(Long totalCollectionTimespent) {
    this.totalCollectionTimespent = totalCollectionTimespent;
  }

  public Long getTotalAssessmentTimespent() {
    return totalAssessmentTimespent;
  }

  public void setTotalAssessmentTimespent(Long totalAssessmentTimespent) {
    this.totalAssessmentTimespent = totalAssessmentTimespent;
  }

  public Double getAverageCollectionTimespent() {
    return averageCollectionTimespent;
  }

  public void setAverageCollectionTimespent(Double averageCollectionTimespent) {
    this.averageCollectionTimespent = averageCollectionTimespent;
  }

  public Double getAverageAssessmentTimespent() {
    return averageAssessmentTimespent;
  }

  public void setAverageAssessmentTimespent(Double averageAssessmentTimespent) {
    this.averageAssessmentTimespent = averageAssessmentTimespent;
  }

}
