package org.gooru.groups.hierarchies.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.dbhelpers.core.groupacl.HierarchyModel;
import org.gooru.groups.reports.dbhelpers.core.groupacl.TenantHierarchyModel;
import org.gooru.groups.reports.dbhelpers.core.groupacl.TenantModel;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public class FetchUserGroupHierarchiesResponseModelBuilder {
  
  public static FetchUserGroupHierarchiesResponseModel build(
      List<TenantHierarchyModel> tenantHierarchyModels, Map<Long, HierarchyModel> hierarchyModels,
      Map<String, TenantModel> tenantModels) {
    FetchUserGroupHierarchiesResponseModel responseModel =
        new FetchUserGroupHierarchiesResponseModel();

    Map<Long, List<String>> tenantHierarchyMap = new HashMap<>();
    tenantHierarchyModels.forEach(model -> {
      Long hierarchyId = model.getHierarchyId();
      String tenant = model.getTenant();
      if (tenantHierarchyMap.containsKey(hierarchyId)) {
        tenantHierarchyMap.get(hierarchyId).add(tenant);
      } else {
        List<String> tenants = new ArrayList<>();
        tenants.add(tenant);
        tenantHierarchyMap.put(hierarchyId, tenants);
      }
    });

    List<FetchUserGroupHierarchiesResponseModel.Hierarchy> hierarchies = new ArrayList<>();
    for (Long id : tenantHierarchyMap.keySet()) {
      FetchUserGroupHierarchiesResponseModel.Hierarchy hierarchy =
          prepareHierarchyModel(hierarchyModels.get(id));
      List<FetchUserGroupHierarchiesResponseModel.Tenant> tenants = new ArrayList<>();
      tenantHierarchyMap.get(id).forEach(tenant -> {
        tenants.add(prepareTenantModel(tenantModels.get(tenant)));
      });
      hierarchy.setTenants(tenants);
      hierarchies.add(hierarchy);
    }

    responseModel.setHierarchies(hierarchies);
    return responseModel;
  }

  private static FetchUserGroupHierarchiesResponseModel.Hierarchy prepareHierarchyModel(
      HierarchyModel model) {
    FetchUserGroupHierarchiesResponseModel.Hierarchy hierarchy =
        new FetchUserGroupHierarchiesResponseModel.Hierarchy();
    hierarchy.setId(model.getId());
    hierarchy.setName(model.getName());
    hierarchy.setDescription(model.getDescription());
    return hierarchy;
  }

  private static FetchUserGroupHierarchiesResponseModel.Tenant prepareTenantModel(
      TenantModel model) {
    FetchUserGroupHierarchiesResponseModel.Tenant tenant =
        new FetchUserGroupHierarchiesResponseModel.Tenant();
    tenant.setId(model.getId());
    tenant.setName(model.getName());
    return tenant;
  }
}
