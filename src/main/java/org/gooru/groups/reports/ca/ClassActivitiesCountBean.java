
package org.gooru.groups.reports.ca;

/**
 * @author szgooru Created On 08-Jan-2019
 */
public class ClassActivitiesCountBean {

  private String classId;
  private Integer month;
  private Integer year;

  public ClassActivitiesCountBean(ClassActivitiesCountCommand command) {
    this.classId = command.getClassId();
    this.month = command.getMonth();
    this.year = command.getYear();
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
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

}
