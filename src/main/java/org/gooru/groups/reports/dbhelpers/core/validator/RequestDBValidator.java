package org.gooru.groups.reports.dbhelpers.core.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.dbhelpers.core.groupacl.FetchUserGroupHierarchiesService;
import org.gooru.groups.reports.dbhelpers.core.groupacl.TenantHierarchyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru on 08-May-2020
 *
 */
public class RequestDBValidator {

  private final static Logger LOGGER = LoggerFactory.getLogger(RequestDBValidator.class);

  private final FetchUserGroupHierarchiesService USER_GROUP_SERVICE =
      new FetchUserGroupHierarchiesService(DBICreator.getDbiForDefaultDS());

  public void verifyTenantAccess(String userId, String userTenant, Set<String> inputTenants) {

    // Fetch all user accessible tenants of the user and the user's tenant because user has access
    // to the tenant which she belongs to by default.
    Set<String> userAccessibleTenants = USER_GROUP_SERVICE.fetchUserAccessibleTenants(userId);
    userAccessibleTenants.add(userTenant);

    // Fetch all subtenants of the user accessible tenants. Ideally user has access to all of sub
    // tenants of user accessible tenants.
    Set<String> subtenants = this.USER_GROUP_SERVICE.fetchSubtenants(userAccessibleTenants);
    if (subtenants != null && !subtenants.isEmpty()) {
      userAccessibleTenants.addAll(subtenants);
    }

    if (!userAccessibleTenants.containsAll(inputTenants)) {
      LOGGER
          .warn("user do not have access to the tenants for which the data has been requested for");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.FORBIDDEN,
          "user do not have access to all of the tenants for which the data has been requested for");
    }
  }

  public void validateTenantHierarchyMapping(Long hierarchyId, Set<String> inputTenants) {

    List<TenantHierarchyModel> tenantHierarhcyModels = null;
    tenantHierarhcyModels = this.USER_GROUP_SERVICE.fetchTenantHierarchies(inputTenants);

    if (tenantHierarhcyModels == null || tenantHierarhcyModels.isEmpty()) {
      LOGGER.warn(
          "looks like none of the tenant for which user has requested data has hierarchy mapping");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "none of the tenant for which user has requested data has hierarchy mapping");
    }

    Set<Long> hierarchyIds = new HashSet<>();
    tenantHierarhcyModels.forEach(model -> {
      hierarchyIds.add(model.getHierarchyId());
    });

    if (hierarchyIds.size() > 1) {
      LOGGER.warn("tenant for which user has requested data has multiple hierarchy mapping");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "tenant for which user has requested data has  multiple hierarchy mapping");
    }

    if (!hierarchyIds.contains(hierarchyId)) {
      LOGGER.warn(
          "hierarchy of the tenant for which user has requested data does not match with provided hierarchy");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "hierarchy of the tenant for which user has requested data does not match with provided hierarchy");
    }
  }
}
