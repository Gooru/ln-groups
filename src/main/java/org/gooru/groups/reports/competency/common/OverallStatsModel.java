package org.gooru.groups.reports.competency.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 24-Apr-2020
 *
 */
public class OverallStatsModel {

  @JsonProperty("total_completed_competencies")
  private Long totalCompletedCompetencies;
  
  @JsonProperty("total_inferred_competencies")
  private Long totalInferredCompetencies;
  
  @JsonProperty("total_inprogress_competencies")
  private Long totalInprogressCompetencies;
  
  @JsonProperty("total_notstarted_competencies")
  private Long totalNotstartedCompetencies;

  public Long getTotalCompletedCompetencies() {
    return totalCompletedCompetencies;
  }

  public void setTotalCompletedCompetencies(Long totalCompletedCompetencies) {
    this.totalCompletedCompetencies = totalCompletedCompetencies;
  }

  public Long getTotalInferredCompetencies() {
    return totalInferredCompetencies;
  }

  public void setTotalInferredCompetencies(Long totalInferredCompetencies) {
    this.totalInferredCompetencies = totalInferredCompetencies;
  }

  public Long getTotalInprogressCompetencies() {
    return totalInprogressCompetencies;
  }

  public void setTotalInprogressCompetencies(Long totalInprogressCompetencies) {
    this.totalInprogressCompetencies = totalInprogressCompetencies;
  }

  public Long getTotalNotstartedCompetencies() {
    return totalNotstartedCompetencies;
  }

  public void setTotalNotstartedCompetencies(Long totalNotstartedCompetencies) {
    this.totalNotstartedCompetencies = totalNotstartedCompetencies;
  }

}
