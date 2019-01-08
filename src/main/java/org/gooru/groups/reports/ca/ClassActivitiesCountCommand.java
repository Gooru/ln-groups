
package org.gooru.groups.reports.ca;

/**
 * @author szgooru Created On 08-Jan-2019
 */
public class ClassActivitiesCountCommand {

  private String classId;
  private Integer month;
  private Integer year;

  public ClassActivitiesCountCommand(String classId, Integer month, Integer year) {
    super();
    this.classId = classId;
    this.month = month;
    this.year = year;
  }

  public String getClassId() {
    return classId;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }
}
