
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;

/**
 * @author renuka
 */
public class ClassStudentSummaryBean {

  private String classId;
  private Date dateTill;

  public ClassStudentSummaryBean(ClassStudentSummaryCommand command) {
    this.classId = command.getClassId();
    this.dateTill = command.getDateTill();
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

}
