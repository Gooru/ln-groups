package org.gooru.groups.reports.dbhelpers.core;

/**
 * @author szgooru on 01-Feb-2020
 *
 */
public class CountryModel {
  
  private Long id;
  private String name;
  private String code;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

}
