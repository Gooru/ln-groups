
package org.gooru.groups.reports.classes.student.detailed.summary;

/**
 * @author renuka
 */
public class StudentPerformanceModel {

  private Long timespent;
  private Integer score;
  private String collectionId;
  private String collectionType;
  private String lessonId;
  private String unitId;
  private String courseId;
  private String dateOfActivity;

  public Long getTimespent() {
    return timespent;
  }

  public void setTimespent(Long timespent) {
    this.timespent = timespent;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public String getCollectionType() {
    return collectionType;
  }

  public void setCollectionType(String collectionType) {
    this.collectionType = collectionType;
  }

  public String getLessonId() {
    return lessonId;
  }

  public void setLessonId(String lessonId) {
    this.lessonId = lessonId;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public String getDateOfActivity() {
    return dateOfActivity;
  }

  public void setDateOfActivity(String dateOfActivity) {
    this.dateOfActivity = dateOfActivity;
  }

}
