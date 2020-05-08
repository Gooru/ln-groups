package org.gooru.groups.reports.competency.drilldownreport;

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
 * @author szgooru on 24-Apr-2020
 *
 */
public class GroupCompetencyDrilldownReportCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupCompetencyDrilldownReportCommand.class);

  private Long hierarchyId;
  private Set<String> tenants;
  private Long groupId;
  private String groupType;
  private Integer month;
  private Integer year;
  private String frequency;
  private String tenantId;
  private String tenantRoot;

  public GroupCompetencyDrilldownReportCommand(Long hierarchyId, Set<String> tenants, Long groupId,
      String groupType, Integer month, Integer year, String frequency, String tenantId,
      String tenantRoot) {
    super();
    this.hierarchyId = hierarchyId;
    this.tenants = tenants;
    this.groupId = groupId;
    this.groupType = groupType;
    this.month = month;
    this.year = year;
    this.frequency = frequency;
    this.tenantId = tenantId;
    this.tenantRoot = tenantRoot;
  }

  public Long getHierarchyId() {
    return hierarchyId;
  }

  public Set<String> getTenants() {
    return tenants;
  }

  public Long getGroupId() {
    return groupId;
  }

  public String getGroupType() {
    return groupType;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public String getFrequency() {
    return frequency;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getTenantRoot() {
    return tenantRoot;
  }

  public static GroupCompetencyDrilldownReportCommand build(JsonObject requestBody, JsonObject tenantJson) {
    GroupCompetencyDrilldownReportCommand command = buildFromJson(requestBody, tenantJson);
    command.validate();
    return command;
  }

  private void validate() {
    if (this.hierarchyId == null) {
      LOGGER.warn("Invalid hierarchyId provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid hierarchyId provided");
    }

    if (this.groupId == null) {
      LOGGER.warn("Invalid groupId provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid groupId provided");
    }

    if (this.groupType == null) {
      LOGGER.warn("Invalid groupType provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid groupType provided");
    }

    if (frequency == null
        || !(CommandAttributeConstants.VALID_FREQUENCY.contains(this.frequency))) {
      this.frequency = CommandAttributeConstants.FREQUENCY_WEEKLY;
    }

    if (month == null || year == null) {
      month = CommonUtils.currentMonth();
      year = CommonUtils.currentYear();
    }
  }

  private static GroupCompetencyDrilldownReportCommand buildFromJson(JsonObject requestBody,
      JsonObject tenantJson) {
    Long hierarchyId = RequestUtils.getAsLong(requestBody, CommandAttributeConstants.HIERARCHY_ID);
    Set<String> tenants = RequestUtils.getAsSet(requestBody, CommandAttributeConstants.TENANTS);
    Long groupId = RequestUtils.getAsLong(requestBody, CommandAttributeConstants.GROUP_ID);
    String groupType = requestBody.getString(CommandAttributeConstants.GROUP_TYPE);
    Integer month = RequestUtils.getAsInt(requestBody, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(requestBody, CommandAttributeConstants.YEAR);
    String frequency = requestBody.getString(CommandAttributeConstants.FREQUENCY);
    String tenantId = tenantJson.getString(CommandAttributeConstants.TENANT_ID);
    String tenantRoot = tenantJson.getString(CommandAttributeConstants.TENANT_ROOT);

    // If there not tenants present in the request to filter the data, we should use the users
    // tenant as default
    if (tenants == null || tenants.isEmpty()) {
      tenants = new HashSet<>();
      tenants.add(tenantId);
    }
    return new GroupCompetencyDrilldownReportCommand(hierarchyId, tenants, groupId, groupType, month, year,
        frequency, tenantId, tenantRoot);
  }

  public GroupCompetencyDrilldownReportCommandBean asBean() {
    GroupCompetencyDrilldownReportCommandBean bean = new GroupCompetencyDrilldownReportCommandBean();
    bean.hierarchyId = hierarchyId;
    bean.tenants = tenants;
    bean.groupId = groupId;
    bean.groupType = groupType;
    bean.month = month;
    bean.year = year;
    bean.frequency = frequency;
    bean.tenantId = tenantId;
    bean.tenantRoot = tenantRoot;
    return bean;
  }

  static class GroupCompetencyDrilldownReportCommandBean {
    private Long hierarchyId;
    private Set<String> tenants;
    private Long groupId;
    private String groupType;
    private Integer month;
    private Integer year;
    private String frequency;
    private String tenantId;
    private String tenantRoot;

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

    public Long getGroupId() {
      return groupId;
    }

    public void setGroupId(Long groupId) {
      this.groupId = groupId;
    }

    public String getGroupType() {
      return groupType;
    }

    public void setGroupType(String groupType) {
      this.groupType = groupType;
    }

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

    public String getFrequency() {
      return frequency;
    }

    public void setFrequency(String frequency) {
      this.frequency = frequency;
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
