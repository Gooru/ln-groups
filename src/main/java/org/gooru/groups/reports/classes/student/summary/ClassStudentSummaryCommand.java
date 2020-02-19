
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;

/**
 * @author renuka
 */
public class ClassStudentSummaryCommand {

  private String classId;
  private Date fromDate;
  private Date toDate;
  private Date dateTill;
  private String subjectCode;
  private Boolean skylineSummary;

  public ClassStudentSummaryCommand(String classId, Date fromDate, Date toDate, Date dateTill, String subjectCode, Boolean skylineSummary) {
    super();
    this.classId = classId;
    this.dateTill = dateTill;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.subjectCode = subjectCode;
    this.skylineSummary = skylineSummary;
  }

  public String getClassId() {
    return classId;
  }

  public Date getDateTill() {
    return dateTill;
  }
  
  public Date getFromDate() {
    return fromDate;
  }
  
  public Date getToDate() {
    return toDate;
  }
  

  public Boolean getSkylineSummary() {
    return skylineSummary;
  }

  public String getSubjectCode() {
    return subjectCode;
  }
}
