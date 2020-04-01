package org.gooru.groups.hierarchies.users;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public class FetchUserGroupHierarchiesCommand {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(FetchUserGroupHierarchiesCommand.class);

  private String userId;

  public FetchUserGroupHierarchiesCommand(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public static FetchUserGroupHierarchiesCommand build(String userId) {
    FetchUserGroupHierarchiesCommand command = buildFromData(userId);
    command.validate();
    return command;
  }

  private static FetchUserGroupHierarchiesCommand buildFromData(String userId) {
    return new FetchUserGroupHierarchiesCommand(userId);
  }

  private void validate() {
    if (this.userId == null || this.userId.isEmpty()) {
      LOGGER.warn("invalid user id found in request");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "invalid user provided in the request");
    }

    if (this.userId.equalsIgnoreCase(CommandAttributeConstants.USER_ANONYMOUS)) {
      LOGGER.warn("anonymous user does not have access");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.FORBIDDEN,
          "Unauthorized access");
    }
  }

  public FetchUserGroupHierarchiesCommandBean asBean() {
    FetchUserGroupHierarchiesCommandBean bean = new FetchUserGroupHierarchiesCommandBean();
    bean.userId = this.userId;
    return bean;
  }

  static class FetchUserGroupHierarchiesCommandBean {
    private String userId;

    public String getUserId() {
      return userId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

  }

}
