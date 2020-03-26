package org.gooru.groups.hierarchies.assign;

import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class AssignHierarchyService {

  private final AssignHierarchyDao dao;

  public AssignHierarchyService(DBI dbi) {
    this.dao = dbi.onDemand(AssignHierarchyDao.class);
  }

  public Boolean isHierarchyExists(Long hierarchyId) {
    return this.dao.isHierarchyExists(hierarchyId);
  }

  public Boolean isTenantExists(String tenant) {
    return this.dao.isTenantExists(tenant);
  }

  public void insertOrupdateTenantHierarchy(
      AssignHierarchyCommand.AssignHierarchyCommandBean bean) {
    this.dao.insertOrupdateTenantHierarchy(bean);
  }
}
