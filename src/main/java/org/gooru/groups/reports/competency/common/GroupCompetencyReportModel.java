package org.gooru.groups.reports.competency.common;

/**
 * @author szgooru on 21-Apr-2020
 *
 */
public class GroupCompetencyReportModel {

  private Long id;
  private String classId;
  private Long completedCompetencies;
  private Long inferredCompetencies;
  private Long inprogressCompetencies;
  private Long notstartedCompetencies;
  private Integer week;
  private Integer month;
  private Integer year;
  private String tenant;
  private String subject;
  private String framework;
  private Long grade;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public Long getCompletedCompetencies() {
    return completedCompetencies;
  }

  public void setCompletedCompetencies(Long completedCompetencies) {
    this.completedCompetencies = completedCompetencies;
  }

  public Long getInferredCompetencies() {
    return inferredCompetencies;
  }

  public void setInferredCompetencies(Long inferredCompetencies) {
    this.inferredCompetencies = inferredCompetencies;
  }

  public Long getInprogressCompetencies() {
    return inprogressCompetencies;
  }

  public void setInprogressCompetencies(Long inprogressCompetencies) {
    this.inprogressCompetencies = inprogressCompetencies;
  }

  public Long getNotstartedCompetencies() {
    return notstartedCompetencies;
  }

  public void setNotstartedCompetencies(Long notstartedCompetencies) {
    this.notstartedCompetencies = notstartedCompetencies;
  }

  public Integer getWeek() {
    return week;
  }

  public void setWeek(Integer week) {
    this.week = week;
  }

  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getFramework() {
    return framework;
  }

  public void setFramework(String framework) {
    this.framework = framework;
  }

  public Long getGrade() {
    return grade;
  }

  public void setGrade(Long grade) {
    this.grade = grade;
  }

  @Override
  public String toString() {
    return "GroupReportModel [id=" + id + ", classId=" + classId + ", completedCompetencies="
        + completedCompetencies + ", inferredCompetencies=" + inferredCompetencies
        + ", inprogressCompetencies=" + inprogressCompetencies + ", notstartedCompetencies="
        + notstartedCompetencies + ", week=" + week + ", month=" + month + ", year=" + year
        + ", tenant=" + tenant + ", subject=" + subject + ", framework=" + framework + ", grade="
        + grade + "]";
  }

}
