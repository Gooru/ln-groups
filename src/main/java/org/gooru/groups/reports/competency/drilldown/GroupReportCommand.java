package org.gooru.groups.reports.competency.drilldown;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru on 14-Feb-2020
 *
 */
public class GroupReportCommand {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportCommand.class);

  private Integer month;
  private Integer year;
  private String tenantId;
  private String tenantRoot;

  private GroupReportCommand(Integer month, Integer year, String tenantId, String tenantRoot) {
    this.month = month;
    this.year = year;
    this.tenantId = tenantId;
    this.tenantRoot = tenantRoot;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getTenantRoot() {
    return tenantRoot;
  }

  public static GroupReportCommand build(JsonObject requestBody, JsonObject tenantJson) {
    GroupReportCommand command = buildFromJson(requestBody, tenantJson);
    command.validate();
    return command;
  }

  private void validate() {
    if (this.tenantId == null) {
      LOGGER.warn("Invalid tenant provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid tenantId provided");
    }
  }

  private static GroupReportCommand buildFromJson(JsonObject requestBody, JsonObject tenantJson) {
    Integer month = RequestUtils.getAsInt(requestBody, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(requestBody, CommandAttributeConstants.YEAR);
    String tenantId = tenantJson.getString(CommandAttributeConstants.TENANT_ID);
    String tenantRoot = tenantJson.getString(CommandAttributeConstants.TENANT_ROOT);
    return new GroupReportCommand(month, year, tenantId, tenantRoot);
  }

  public GroupReportCommandBean asBean() {
    GroupReportCommandBean bean = new GroupReportCommandBean();
    bean.month = month;
    bean.year = year;
    bean.tenantId = tenantId;
    bean.tenantRoot = tenantRoot;
    return bean;
  }

  public static class GroupReportCommandBean {
    private Integer month;
    private Integer year;
    private String tenantId;
    private String tenantRoot;

    public Integer getMonth() {
      return month;
    }

    public void setMonth(Integer month) {
      this.month = month;
    }

    public Integer getYear() {
      return year;
    }

    public void setYear(Integer year) {
      this.year = year;
    }

    public String getTenantId() {
      return tenantId;
    }

    public void setTenantId(String tenantId) {
      this.tenantId = tenantId;
    }

    public String getTenantRoot() {
      return tenantRoot;
    }

    public void setTenantRoot(String tenantRoot) {
      this.tenantRoot = tenantRoot;
    }
    
  }
}
