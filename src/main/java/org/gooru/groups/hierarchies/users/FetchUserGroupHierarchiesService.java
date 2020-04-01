package org.gooru.groups.hierarchies.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public class FetchUserGroupHierarchiesService {

  private final FetchUserGroupHierarchiesDao dao;

  public FetchUserGroupHierarchiesService(DBI dbi) {
    this.dao = dbi.onDemand(FetchUserGroupHierarchiesDao.class);
  }

  public Set<String> fetchUserAccessibleTenants(String userId) {
    return this.dao.fetchUserAccessibleTenants(userId);
  }

  public Set<String> fetchSubtenants(Set<String> tenants) {
    return this.dao.fetchSubtenants(CollectionUtils.convertToSqlArrayOfUUID(tenants));
  }

  public List<TenantHierarchyModel> fetchTenantHierarchies(Set<String> tenants) {
    return this.dao.fetchHierarchiesByTenant(CollectionUtils.convertToSqlArrayOfUUID(tenants));
  }

  public Map<Long, HierarchyModel> fetchHierarchyDetails(Set<Long> hierarchyIds) {
    List<HierarchyModel> models =
        this.dao.fetchHierarchyDetails(CollectionUtils.toPostgresArrayLong(hierarchyIds));
    Map<Long, HierarchyModel> modelMap = new HashMap<>();
    models.forEach(model -> {
      modelMap.put(model.getId(), model);
    });

    return modelMap;
  }

  public Map<String, TenantModel> fetchTenantDetails(Set<String> tenants) {
    List<TenantModel> models =
        this.dao.fetchTenantDetails(CollectionUtils.convertToSqlArrayOfUUID(tenants));
    Map<String, TenantModel> modelMap = new HashMap<>();
    models.forEach(model -> {
      modelMap.put(model.getId(), model);
    });
    return modelMap;
  }
}
