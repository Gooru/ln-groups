package org.gooru.groups.reports.competency.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 24-Apr-2020
 *
 */
public class WeekDataModel {

  @JsonProperty("label")
  private Integer label;
  
  @JsonProperty("completed_competencies")
  private Long completedCompetencies;
  
  @JsonProperty("inferred_competencies")
  private Long inferredCompetencies;

  @JsonProperty("inprogress_competencies")
  private Long inprogressCompetencies;
  
  @JsonProperty("notstarted_competencies")
  private Long notstartedCompetencies;

  public Integer getLabel() {
    return label;
  }

  public void setLabel(Integer label) {
    this.label = label;
  }

  public Long getCompletedCompetencies() {
    return completedCompetencies;
  }

  public void setCompletedCompetencies(Long completedCompetencies) {
    this.completedCompetencies = completedCompetencies;
  }

  public Long getInferredCompetencies() {
    return inferredCompetencies;
  }

  public void setInferredCompetencies(Long inferredCompetencies) {
    this.inferredCompetencies = inferredCompetencies;
  }

  public Long getInprogressCompetencies() {
    return inprogressCompetencies;
  }

  public void setInprogressCompetencies(Long inprogressCompetencies) {
    this.inprogressCompetencies = inprogressCompetencies;
  }

  public Long getNotstartedCompetencies() {
    return notstartedCompetencies;
  }

  public void setNotstartedCompetencies(Long notstartedCompetencies) {
    this.notstartedCompetencies = notstartedCompetencies;
  }

}
