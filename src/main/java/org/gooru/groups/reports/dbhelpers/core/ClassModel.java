
package org.gooru.groups.reports.dbhelpers.core;

/**
 * @author renuka
 */
public class ClassModel {

  private String code;
  private String title;
  private String creatorId;
  private String courseId;
  private String gradeCurrent;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public String getGradeCurrent() {
    return gradeCurrent;
  }

  public void setGradeCurrent(String gradeCurrent) {
    this.gradeCurrent = gradeCurrent;
  }

}
