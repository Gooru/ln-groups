
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;

/**
 * @author renuka
 */
public class ClassStudentSummaryBean {

  private String classId;
  private String userId;
  private Date dateTill;
  private Date toDate;
  private Date fromDate;
  private String subjectCode;
  private Boolean skylineSummary;

  public ClassStudentSummaryBean(ClassStudentSummaryCommand command) {
    this.classId = command.getClassId();
    this.userId = command.getUserId();
    this.dateTill = command.getDateTill();
    this.fromDate = command.getFromDate();
    this.toDate = command.getToDate();
    this.subjectCode = command.getSubjectCode();
    this.skylineSummary = command.getSkylineSummary();
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

  public String getTxSubjectCode() {
    return subjectCode;
  }

  public void setTxSubjectCode(String txSubjectCode) {
    this.subjectCode = txSubjectCode;
  }

  public Boolean getSkylineSummary() {
    return skylineSummary;
  }

  public void setSkylineSummary(Boolean skylineSummary) {
    this.skylineSummary = skylineSummary;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
