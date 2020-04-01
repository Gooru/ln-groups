package org.gooru.groups.hierarchies.users;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public interface FetchUserGroupHierarchiesDao {

  @SqlQuery("SELECT tenant FROM tenant_user_acl WHERE user_id = :userId::uuid")
  Set<String> fetchUserAccessibleTenants(@Bind("userId") String userId);

  @SqlQuery("SELECT id FROM tenant WHERE parent_tenant = ANY(:tenants)")
  Set<String> fetchSubtenants(@Bind("tenants") PGArray<UUID> tenants);

  @Mapper(TenantHierarchyModelMapper.class)
  @SqlQuery("SELECT id, value AS hierarchy_id FROM tenant_setting WHERE id = ANY(:tenants) AND key = 'group_hierarchy'")
  List<TenantHierarchyModel> fetchHierarchiesByTenant(@Bind("tenants") PGArray<UUID> tenants);

  @Mapper(HierarchyModelMapper.class)
  @SqlQuery("SELECT id, name, description FROM group_hierarchy WHERE id = ANY(:hierarchyIds::bigint[])")
  List<HierarchyModel> fetchHierarchyDetails(@Bind("hierarchyIds") String hierarchyIds);

  @Mapper(TenantModelMapper.class)
  @SqlQuery("SELECT id, name FROM tenant WHERE id = ANY(:tenants)")
  List<TenantModel> fetchTenantDetails(@Bind("tenants") PGArray<UUID> tenants);
}

