package org.gooru.groups.reports.competency.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 21-Apr-2020
 *
 */
public class AggregatedDataGroupReportForClassModel {

  @JsonProperty("id")
  private String groupId;
  
  @JsonProperty("name")
  private String name;
  
  @JsonProperty("code")
  private String code;
  
  @JsonProperty("type")
  private String type;
  
  @JsonProperty("completed_competencies")
  private Long completedCompetencies;
  
  @JsonProperty("inferred_competencies")
  private Long inferredCompetencies;

  @JsonProperty("inprogress_competencies")
  private Long inprogressCompetencies;
  
  @JsonProperty("notstarted_competencies")
  private Long notstartedCompetencies;

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
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

  @Override
  public String toString() {
    return "AggregatedDataGroupReportModel [groupId=" + groupId + ", completedCompetencies="
        + completedCompetencies + ", inferredCompetencies=" + inferredCompetencies
        + ", inprogressCompetencies=" + inprogressCompetencies + ", notstartedCompetencies="
        + notstartedCompetencies + "]";
  }
  
}
