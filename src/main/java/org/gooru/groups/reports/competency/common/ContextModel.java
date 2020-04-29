package org.gooru.groups.reports.competency.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 24-Apr-2020
 *
 */
public class ContextModel {

  @JsonProperty("report")
  public String report;
  
  @JsonProperty("hierarchy")
  public Long hierarchy;
  
  @JsonProperty("tenants")
  public List<String> tenants;
  
  @JsonProperty("group_id")
  public Long groupId;
  
  @JsonProperty("group_type")
  public String groupType;
  
  @JsonProperty("month")
  public Integer month;
  
  @JsonProperty("year")
  public Integer year;
  
  @JsonProperty("frequency")
  public String frequency;

  public String getReport() {
    return report;
  }

  public void setReport(String report) {
    this.report = report;
  }

  public Long getHierarchy() {
    return hierarchy;
  }

  public void setHierarchy(Long hierarchy) {
    this.hierarchy = hierarchy;
  }

  public List<String> getTenants() {
    return tenants;
  }

  public void setTenants(List<String> tenants) {
    this.tenants = tenants;
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
