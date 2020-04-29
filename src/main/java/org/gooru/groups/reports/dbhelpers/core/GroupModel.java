
package org.gooru.groups.reports.dbhelpers.core;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public class GroupModel {
  private Long id;
  private String name;
  private String code;
  private String type;
  private String subType;

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSubType() {
    return subType;
  }

  public void setSubType(String subType) {
    this.subType = subType;
  }

  @Override
  public String toString() {
    return "GroupModel [id=" + id + ", name=" + name + ", code=" + code + ", type=" + type
        + ", subType=" + subType + "]";
  }

}
