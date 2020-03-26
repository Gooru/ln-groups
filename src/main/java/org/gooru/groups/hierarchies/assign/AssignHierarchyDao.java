package org.gooru.groups.hierarchies.assign;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public interface AssignHierarchyDao {

  @SqlQuery("SELECT EXISTS (SELECT * FROM group_hierarchy WHERE id = :hierarchyId::bigint)")
  Boolean isHierarchyExists(@Bind("hierarchyId") Long hierarchyId);

  @SqlQuery("SELECT EXISTS (SELECT * FROM tenant WHERE id = :tenant::uuid)")
  Boolean isTenantExists(@Bind("tenant") String tenant);

  @SqlUpdate("INSERT INTO tenant_setting (id, key, value) VALUES (:tenant::uuid, 'group_hierarchy', :hierarchyId) ON CONFLICT (id, key)"
      + " DO UPDATE SET value = :hierarchyId")
  void insertOrupdateTenantHierarchy(
      @BindBean AssignHierarchyCommand.AssignHierarchyCommandBean bean);
}
