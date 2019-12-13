
package org.gooru.groups.reports.perf.school;

import java.util.List;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupPerfReportBySchoolResponseModel {

  private List<ClassPerformance> data;

  public List<ClassPerformance> getData() {
    return data;
  }

  public void setData(List<ClassPerformance> data) {
    this.data = data;
  }

  static class ClassPerformance {
    private String id;
    private String name;
    private String code;
    private String type;
    private String subType;
    private Long timespent;
    private Double performance;
    private Long completedCompetencies;
    private Long inprogressCompetencies;

    public String getId() {
      return id;
    }

    public void setId(String id) {
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
