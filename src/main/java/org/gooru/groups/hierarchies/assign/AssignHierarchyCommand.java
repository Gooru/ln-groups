package org.gooru.groups.hierarchies.assign;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class AssignHierarchyCommand {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AssignHierarchyCommand.class);

  private Long hierarchyId;
  private String tenant;

  private AssignHierarchyCommand(Long hierarchyId, String tenant) {
    this.hierarchyId = hierarchyId;
    this.tenant = tenant;
  }

  public Long getHierarchyId() {
    return hierarchyId;
  }

  public String getTenant() {
    return tenant;
  }

  public static AssignHierarchyCommand build(JsonObject requestBody) {
    AssignHierarchyCommand command = buildFromJson(requestBody);
    command.validate();
    return command;
  }

  private static AssignHierarchyCommand buildFromJson(JsonObject requestBody) {
    String tenantId = requestBody.getString(CommandAttributeConstants.TENANT_ID_FROM_URL);
    Long hierarchyId = RequestUtils.getAsLong(requestBody, CommandAttributeConstants.HIERARCHY_ID);
    AssignHierarchyCommand command = new AssignHierarchyCommand(hierarchyId, tenantId);
    return command;
  }

  private void validate() {
    if (hierarchyId == null) {
      LOGGER.warn("invalid hierarchy provided in the request");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "invalid hierarchy provided in the request");
    }
    
    if (tenant == null) {
      LOGGER.warn("invalid tenant provided in the request");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "invalid tenant provided in the request");
    }
  }

  public AssignHierarchyCommandBean asBean() {
    AssignHierarchyCommandBean bean = new AssignHierarchyCommandBean();
    bean.hierarchyId = this.hierarchyId;
    bean.tenant = this.tenant;
    return bean;
  }

  public static class AssignHierarchyCommandBean {
    private Long hierarchyId;
    private String tenant;

    public Long getHierarchyId() {
      return hierarchyId;
    }

    public void setHierarchyId(Long hierarchyId) {
      this.hierarchyId = hierarchyId;
    }

    public String getTenant() {
      return tenant;
    }

    public void setTenant(String tenant) {
      this.tenant = tenant;
    }

  }
}
