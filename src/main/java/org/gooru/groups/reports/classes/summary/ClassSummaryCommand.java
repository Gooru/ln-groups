
package org.gooru.groups.reports.classes.summary;

import java.util.Date;

/**
 * @author renuka
 */
public class ClassSummaryCommand {

  private String classId;
  private Date fromDate;
  private Date toDate;

  public ClassSummaryCommand(String classId, Date fromDate, Date toDate) {
    super();
    this.classId = classId;
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
}
