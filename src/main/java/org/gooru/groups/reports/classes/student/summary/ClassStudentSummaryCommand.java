
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;

/**
 * @author renuka
 */
public class ClassStudentSummaryCommand {

  private String classId;
  private Date dateTill;

  public ClassStudentSummaryCommand(String classId, Date dateTill) {
    super();
    this.classId = classId;
    this.dateTill = dateTill;
  }

  public String getClassId() {
    return classId;
  }

  public Date getDateTill() {
    return dateTill;
  }
}
