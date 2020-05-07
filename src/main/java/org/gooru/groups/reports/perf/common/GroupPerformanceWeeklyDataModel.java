package org.gooru.groups.reports.perf.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceWeeklyDataModel {

  @JsonProperty("label")
  private Integer label;

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

  public Integer getLabel() {
    return label;
  }

  public void setLabel(Integer label) {
    this.label = label;
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
