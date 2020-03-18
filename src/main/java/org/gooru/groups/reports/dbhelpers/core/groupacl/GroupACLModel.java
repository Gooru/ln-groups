package org.gooru.groups.reports.dbhelpers.core.groupacl;

import io.vertx.core.json.JsonArray;

/**
 * @author szgooru on 18-Feb-2020
 *
 */
public class GroupACLModel {

  private String type;
  private JsonArray groups;

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

}
