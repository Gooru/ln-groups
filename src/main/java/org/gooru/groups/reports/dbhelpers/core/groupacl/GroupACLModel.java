package org.gooru.groups.reports.dbhelpers.core.groupacl;

import io.vertx.core.json.JsonArray;

/**
 * @author szgooru on 18-Feb-2020
 *
 */
public class GroupACLModel {

  private Long id;
  private String type;
  private JsonArray groups;
  private Long parentRefId;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public JsonArray getGroups() {
    return groups;
  }

  public void setGroups(JsonArray groups) {
    this.groups = groups;
  }

  public Long getParentRefId() {
    return parentRefId;
  }

  public void setParentRefId(Long parentRefId) {
    this.parentRefId = parentRefId;
  }

  @Override
  public String toString() {
    return "GroupACLModel [id=" + id + ", type=" + type + ", groups=" + groups + ", parentRefId="
        + parentRefId + "]";
  }

}
