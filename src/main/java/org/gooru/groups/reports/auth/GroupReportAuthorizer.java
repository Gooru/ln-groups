
package org.gooru.groups.reports.auth;

import java.util.ResourceBundle;
import org.gooru.groups.app.components.AppConfiguration;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonArray;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupReportAuthorizer implements Authorizer {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportAuthorizer.class);
  private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private final String userId;
  private final UserRoleService service = new UserRoleService(DBICreator.getDbiForDefaultDS());

  public GroupReportAuthorizer(String userId) {
    this.userId = userId;
  }

  @Override
  public void authorize() {
    String userRole = this.service.fetchUserRole(this.userId);
    if (userRole == null || !userRole.isEmpty()) {
      LOGGER.warn("user '{}' does not have role associated", this.userId);
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.UNAUTHORIZED,
          RESOURCE_BUNDLE.getString("no.user.role"));
    }

    JsonArray reportAccessRoles = AppConfiguration.getInstance().getReportAccessRoles();
    if (!reportAccessRoles.contains(userRole)) {
      LOGGER.warn("user '{}' do not have rights to access", this.userId);
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.UNAUTHORIZED,
          RESOURCE_BUNDLE.getString("unauthorized.access"));
    }
  }

}
