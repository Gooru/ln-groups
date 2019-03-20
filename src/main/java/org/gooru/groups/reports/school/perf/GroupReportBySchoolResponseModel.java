
package org.gooru.groups.reports.school.perf;

import java.util.List;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupReportBySchoolResponseModel {

  private List<ClassPerformance> classes;

  public List<ClassPerformance> getClasses() {
    return classes;
  }

  public void setClasses(List<ClassPerformance> classes) {
    this.classes = classes;
  }

  static class ClassPerformance {
    private String id;
    private String name;
    private Long timespent;
    private Double performance;

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
