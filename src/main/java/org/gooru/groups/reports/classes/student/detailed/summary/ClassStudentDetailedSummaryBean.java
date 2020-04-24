
package org.gooru.groups.reports.classes.student.detailed.summary;

import java.util.Date;

/**
 * @author renuka
 */
public class ClassStudentDetailedSummaryBean {

  private String classId;
  private String userId;
  private Date fromDate;
  private Date toDate;

  public ClassStudentDetailedSummaryBean(ClassStudentDetailedSummaryCommand command) {
    this.classId = command.getClassId();
    this.userId = command.getUserId();
    this.fromDate = command.getFromDate();
    this.toDate = command.getToDate();
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
