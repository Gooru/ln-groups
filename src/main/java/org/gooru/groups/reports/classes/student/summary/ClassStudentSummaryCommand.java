
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

  public ClassStudentSummaryCommand(String classId, Date fromDate, Date toDate, Date dateTill) {
    super();
    this.classId = classId;
    this.dateTill = dateTill;
    this.fromDate = fromDate;
    this.toDate = toDate;
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
}
