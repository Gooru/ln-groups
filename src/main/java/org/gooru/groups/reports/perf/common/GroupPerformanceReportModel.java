package org.gooru.groups.reports.perf.common;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceReportModel {

  private Long id;
  private String classId;
  private Double averagePerformance;
  private Long totalCollectionTimespent;
  private Long totalAssessmentTimespent;
  private Double averageCollectionTimespent;
  private Double averageAssessmentTimespent;
  private Integer week;
  private Integer month;
  private Integer year;
  private String tenant;

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

  public Double getAveragePerformance() {
    return averagePerformance;
  }

  public void setAveragePerformance(Double averagePerformance) {
    this.averagePerformance = averagePerformance;
  }

  public Long getTotalCollectionTimespent() {
    return totalCollectionTimespent;
  }

  public void setTotalCollectionTimespent(Long totalCollectionTimespent) {
    this.totalCollectionTimespent = totalCollectionTimespent;
  }

  public Long getTotalAssessmentTimespent() {
    return totalAssessmentTimespent;
  }

  public void setTotalAssessmentTimespent(Long totalAssessmentTimespent) {
    this.totalAssessmentTimespent = totalAssessmentTimespent;
  }

  public Double getAverageCollectionTimespent() {
    return averageCollectionTimespent;
  }

  public void setAverageCollectionTimespent(Double averageCollectionTimespent) {
    this.averageCollectionTimespent = averageCollectionTimespent;
  }

  public Double getAverageAssessmentTimespent() {
    return averageAssessmentTimespent;
  }

  public void setAverageAssessmentTimespent(Double averageAssessmentTimespent) {
    this.averageAssessmentTimespent = averageAssessmentTimespent;
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

}
