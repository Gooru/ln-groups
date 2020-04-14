
package org.gooru.groups.reports.classes.student.detailed.summary;

import java.util.Date;

/**
 * @author renuka
 */
public class ClassStudentDetailedSummaryCommand {

  private String classId;
  private String userId;
  private Date fromDate;
  private Date toDate;

  public ClassStudentDetailedSummaryCommand(String classId, String userId, Date fromDate, Date toDate) {
    super();
    this.classId = classId;
    this.userId = userId;
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public String getClassId() {
    return classId;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public Date getToDate() {
    return toDate;
  }
  
  public String getUserId() {
    return userId;
  }
}
