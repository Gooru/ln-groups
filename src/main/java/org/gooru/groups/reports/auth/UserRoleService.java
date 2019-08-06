
package org.gooru.groups.reports.auth;

import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class UserRoleService {

  private final UserRoleDao dao;

  public UserRoleService(DBI coreDbi) {
    this.dao = coreDbi.onDemand(UserRoleDao.class);
  }

  public String fetchUserRole(String userId) {
    return this.dao.fetchUserRole(userId);
  }
}
