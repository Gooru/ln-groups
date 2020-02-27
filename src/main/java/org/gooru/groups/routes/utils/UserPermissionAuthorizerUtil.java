package org.gooru.groups.routes.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.gooru.groups.constants.Constants.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * @author szgooru on 12-Feb-2020
 *
 */
public final class UserPermissionAuthorizerUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserPermissionAuthorizerUtil.class);

  private static final Map<String, String> ROUTE_PERMISSIONS;
  private static final String SUPER_USER_PERMISSION = "GROUP_SUPER_ADMIN";

  static {
    Map<String, String> routePermissions = new HashMap<>();
    routePermissions.put(Route.API_FETCH_GROUP_REPORT, "REPORT_GROUP_GET");
    routePermissions.put(Route.API_FETCH_COMPETENCY_GROUP_REPORT, "REPORT_GROUP_COMPETENCY_DRILLDOWN");
    routePermissions.put(Route.API_FETCH_PERFORMANCE_GROUP_REPORT, "REPORT_GROUP_PERFORMANCE_DRILLDOWN");

    ROUTE_PERMISSIONS = Collections.unmodifiableMap(routePermissions);
  }

  private UserPermissionAuthorizerUtil() {
    throw new AssertionError();
  }

  public static boolean authorize(RoutingContext routingContext, JsonObject session) {
    // Read permissions from the sessions and check for null or empty. If user does not have any
    // permissions set, return false
    JsonArray permissions = session.getJsonArray("permissions");
    if (permissions == null || permissions.isEmpty()) {
      return false;
    }
    
    if (hasSuperUserPermission(permissions)) {
      LOGGER.debug("user has super admin permission");
      return true;
    } else {
      String requestPath = routingContext.request().uri();
      LOGGER.debug("requestPath: {}", requestPath);
      return hasRoutePermission(requestPath, permissions);
    }
  }

  private static boolean hasSuperUserPermission(JsonArray permissions) {
    return permissions.contains(SUPER_USER_PERMISSION);
  }

  private static boolean hasRoutePermission(String requestPath, JsonArray permissions) {
    return permissions.contains(ROUTE_PERMISSIONS.get(requestPath));
  }
}
