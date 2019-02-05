
package org.gooru.groups.reports.classes.student.summary;

/**
 * @author renuka
 */
public class StudentItemInteraction {

  private String collectionType;
  private Integer interactionCount;
  private Long timespent;
  private Integer score;
  private Integer maxScore;

  public String getCollectionType() {
    return collectionType;
  }

  public void setCollectionType(String collectionType) {
    this.collectionType = collectionType;
  }

  public Integer getInteractionCount() {
    return interactionCount;
  }

  public void setInteractionCount(Integer interactionCount) {
    this.interactionCount = interactionCount;
  }

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

  public Integer getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(Integer maxScore) {
    this.maxScore = maxScore;
  }


}
