package org.gooru.groups.reports.classes.student.summary;

/**
 * @author renuka
 */
class CompetencyStatusModel {
  private String domainCode;
  private String competencyCode;
  private String competencyName;
  private String competencyDesc;
  private String competencyStudentDesc;
  private Integer competencySeq;
  private String competencyDisplayCode;
  private Integer status;

  public String getDomainCode() {
    return domainCode;
  }

  public void setDomainCode(String domainCode) {
    this.domainCode = domainCode;
  }

  public String getCompetencyCode() {
    return competencyCode;
  }

  public void setCompetencyCode(String competencyCode) {
    this.competencyCode = competencyCode;
  }

  public String getCompetencyName() {
    return competencyName;
  }

  public void setCompetencyName(String competencyName) {
    this.competencyName = competencyName;
  }

  public String getCompetencyDesc() {
    return competencyDesc;
  }

  public void setCompetencyDesc(String competencyDesc) {
    this.competencyDesc = competencyDesc;
  }

  public String getCompetencyStudentDesc() {
    return competencyStudentDesc;
  }

  public void setCompetencyStudentDesc(String competencyStudentDesc) {
    this.competencyStudentDesc = competencyStudentDesc;
  }

  public Integer getCompetencySeq() {
    return competencySeq;
  }

  public void setCompetencySeq(Integer competencySeq) {
    this.competencySeq = competencySeq;
  }

  public String getCompetencyDisplayCode() {
    return competencyDisplayCode;
  }

  public void setCompetencyDisplayCode(String competencyDisplayCode) {
    this.competencyDisplayCode = competencyDisplayCode;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
