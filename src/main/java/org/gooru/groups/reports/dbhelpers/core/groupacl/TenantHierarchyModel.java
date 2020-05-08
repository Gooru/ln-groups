package org.gooru.groups.reports.dbhelpers.core.groupacl;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public class TenantHierarchyModel {
  String tenant;
  Long hierarchyId;

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public Long getHierarchyId() {
    return hierarchyId;
  }

  public void setHierarchyId(Long hierarchyId) {
    this.hierarchyId = hierarchyId;
  }

}
