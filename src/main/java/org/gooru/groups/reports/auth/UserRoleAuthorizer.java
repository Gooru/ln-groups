package org.gooru.groups.reports.auth;

import java.util.List;
import java.util.UUID;
import org.gooru.groups.app.components.AppConfiguration;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonArray;

/**
 * @author szgooru on 07-Feb-2020
 *
 */
public class UserRoleAuthorizer implements Authorizer {

  private final static Logger LOGGER = LoggerFactory.getLogger(UserRoleAuthorizer.class);

  private final UUID userId;
  private boolean isGlobalAccess = false;
  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());

  public UserRoleAuthorizer(UUID userId) {
    this.userId = userId;
  }

  @Override
  public void authorize() {
    // Fetch user roles
    List<Integer> userRoles = this.CORE_SERVICE.fetchUserRoles(this.userId);
    if (userRoles == null || userRoles.isEmpty()) {
      LOGGER.warn("user {} does not have any role defined", userId.toString());
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.FORBIDDEN,
          "user does not have any roles defined");
    }

    // Verify that the user has global access
    JsonArray globalRoles = AppConfiguration.getInstance().getGlobalReportAccessRoles();
    for (Object role : globalRoles) {
      try {
        Integer roleId = (Integer) role;
        if (userRoles.contains(roleId)) {
          this.isGlobalAccess = true;
          break;
        }
      } catch (Exception ex) {
        LOGGER.warn("invalid role present in the configuration");
      }
    }
  }

  @Override
  public boolean isGlobalAccess() {
    return this.isGlobalAccess;
  }

}
