
package org.gooru.groups.reports.auth;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public interface UserRoleDao {

  @SqlQuery("SELECT r.name FROM role r, user_role_mapping urm WHERE urm.user_id = :userId::uuid AND r.id = urm.role_id")
  String fetchUserRole(@Bind("userId") String userId);
}
