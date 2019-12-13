
package org.gooru.groups.reports.perf.group;

import java.util.List;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupReportByGroupResponseModel {
  private List<GroupResponseModel> groups;

  public List<GroupResponseModel> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupResponseModel> groups) {
    this.groups = groups;
  }

  static class GroupResponseModel {
    private Integer id;
    private String name;
    private String code;
    private String subType;
    private Long timespent;
    private Double performance;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
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

    public String getSubType() {
      return subType;
    }

    public void setSubType(String subType) {
      this.subType = subType;
    }

    public Long getTimespent() {
      return timespent;
    }

    public void setTimespent(Long timespent) {
      this.timespent = timespent;
    }

    public Double getPerformance() {
      return performance;
    }

    public void setPerformance(Double performance) {
      this.performance = performance;
    }
  }
}
