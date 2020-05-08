package org.gooru.groups.reports.competency.initialreport;

import java.util.HashSet;
import java.util.Set;
import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.CommonUtils;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru on 14-Feb-2020
 *
 */
public class GroupCompetencyInitialReportCommand {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupCompetencyInitialReportCommand.class);

  private Integer month;
  private Integer year;
  private Long hierarchyId;
  private Set<String> tenants;
  private String tenantId;
  private String tenantRoot;

  private GroupCompetencyInitialReportCommand(Integer month, Integer year, Long hierarchyId, Set<String> tenants,
      String tenantId, String tenantRoot) {
    this.month = month;
    this.year = year;
    this.hierarchyId = hierarchyId;
    this.tenants = tenants;
    this.tenantId = tenantId;
    this.tenantRoot = tenantRoot;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public Long getHierarchyId() {
    return hierarchyId;
  }

  public Set<String> getTenants() {
    return tenants;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getTenantRoot() {
    return tenantRoot;
  }

  public static GroupCompetencyInitialReportCommand build(JsonObject requestBody, JsonObject tenantJson) {
    GroupCompetencyInitialReportCommand command = buildFromJson(requestBody, tenantJson);
    command.validate();
    return command;
  }

  private void validate() {
    if (this.hierarchyId == null) {
      LOGGER.warn("Invalid hierarchyId provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid hierarchyId provided");
    }
    
    if (month == null || year == null) {
      month = CommonUtils.currentMonth();
      year = CommonUtils.currentYear();
    }
  }

  private static GroupCompetencyInitialReportCommand buildFromJson(JsonObject requestBody, JsonObject tenantJson) {
    Long hierarchyId = RequestUtils.getAsLong(requestBody, CommandAttributeConstants.HIERARCHY_ID);
    Set<String> tenants =
        RequestUtils.getAsSet(requestBody, CommandAttributeConstants.TENANTS);
    Integer month = RequestUtils.getAsInt(requestBody, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(requestBody, CommandAttributeConstants.YEAR);
    String tenantId = tenantJson.getString(CommandAttributeConstants.TENANT_ID);
    String tenantRoot = tenantJson.getString(CommandAttributeConstants.TENANT_ROOT);
    
    // If there not tenants present in the request to filter the data, we should use the users
    // tenant as default
    if (tenants == null || tenants.isEmpty()) {
      tenants = new HashSet<>();
      tenants.add(tenantId);
    }
    return new GroupCompetencyInitialReportCommand(month, year, hierarchyId, tenants, tenantId, tenantRoot);
  }

  public GroupCompetencyInitialReportCommandBean asBean() {
    GroupCompetencyInitialReportCommandBean bean = new GroupCompetencyInitialReportCommandBean();
    bean.month = month;
    bean.year = year;
    bean.hierarchyId = hierarchyId;
    bean.tenants = tenants;
    bean.tenantId = tenantId;
    bean.tenantRoot = tenantRoot;
    return bean;
  }

  public static class GroupCompetencyInitialReportCommandBean {
    private Integer month;
    private Integer year;
    private Long hierarchyId;
    private Set<String> tenants;
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

    public Long getHierarchyId() {
      return hierarchyId;
    }

    public void setHierarchyId(Long hierarchyId) {
      this.hierarchyId = hierarchyId;
    }

    public Set<String> getTenants() {
      return tenants;
    }

    public void setTenants(Set<String> tenants) {
      this.tenants = tenants;
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
