
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;

/**
 * @author renuka
 */
public class ClassStudentSummaryBean {

  private String classId;
  private Date dateTill;
  private Date toDate;
  private Date fromDate;

  public ClassStudentSummaryBean(ClassStudentSummaryCommand command) {
    this.classId = command.getClassId();
    this.dateTill = command.getDateTill();
    this.fromDate = command.getFromDate();
    this.toDate = command.getToDate();
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public Date getDateTill() {
    return dateTill;
  }

  public void setDateTill(Date dateTill) {
    this.dateTill = dateTill;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

}
