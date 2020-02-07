
package org.gooru.groups.reports.perf.group;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupPerfReportByGroupCommand {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupPerfReportByGroupCommand.class);

  private Long groupId;
  private String frequency;
  private Integer week;
  private Integer month;
  private Integer year;
  private String subject;
  private String framework;
  private String tenantId;
  private String tenantRoot;

  public GroupPerfReportByGroupCommand(Long groupId, String frequency, Integer week, Integer month,
      Integer year, String subject, String framework, String tenantId, String tenantRoot) {
    this.groupId = groupId;
    this.frequency = frequency;
    this.week = week;
    this.month = month;
    this.year = year;
    this.subject = subject;
    this.framework = framework;
    this.tenantId = tenantId;
    this.tenantRoot = tenantRoot;
  }

  public Long getGroupId() {
    return groupId;
  }

  public String getFrequency() {
    return frequency;
  }

  public Integer getWeek() {
    return week;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public String getSubject() {
    return subject;
  }

  public String getFramework() {
    return framework;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getTenantRoot() {
    return tenantRoot;
  }

  public static GroupPerfReportByGroupCommand build(JsonObject request, JsonObject tenantJson) {
    GroupPerfReportByGroupCommand command = buildFromJson(request, tenantJson);
    command.validate();
    return command;
  }

  private static GroupPerfReportByGroupCommand buildFromJson(JsonObject request,
      JsonObject tenantJson) {
    String tenantId = tenantJson.getString(CommandAttributeConstants.TENANT_ID);
    String tenantRoot = tenantJson.getString(CommandAttributeConstants.TENANT_ROOT);
    Long groupId = RequestUtils.getAsLong(request, CommandAttributeConstants.GROUP_ID);
    String frequency = request.getString(CommandAttributeConstants.FREQUENCY);
    Integer week = RequestUtils.getAsInt(request, CommandAttributeConstants.WEEK);
    Integer month = RequestUtils.getAsInt(request, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributeConstants.YEAR);
    String subject = request.getString(CommandAttributeConstants.SUBJECT);
    String framework = request.getString(CommandAttributeConstants.FRAMEWORK);
    return new GroupPerfReportByGroupCommand(groupId, frequency, week, month, year, subject,
        framework, tenantId, tenantRoot);
  }

  private void validate() {
    if (frequency == null || !CommandAttributeConstants.VALID_FREQUENCY.contains(frequency)) {
      LOGGER.warn("invalid value of frequency provided in the request");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid value of frequency provided");
    }

    if (frequency.equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_WEEKLY)) {
      if (week == null) {
        LOGGER.warn("invalid value of week provided");
        throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
            "Invalid value of week provided");
      }
    }

    if (month == null || year == null) {
      LOGGER.warn("invalid month or year provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid month or year provided");
    }

    if (subject == null || framework == null) {
      LOGGER.warn("invalid value of subject or framework provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid value of subject or framework  provided");
    }
  }

  public GroupPerfReportByGroupCommandBean asBean() {
    GroupPerfReportByGroupCommandBean bean = new GroupPerfReportByGroupCommandBean();
    bean.groupId = groupId;
    bean.frequency = frequency;
    bean.week = week;
    bean.month = month;
    bean.year = year;
    bean.subject = subject;
    bean.framework = framework;
    bean.tenantId = tenantId;
    bean.tenantRoot = tenantRoot;
    return bean;
  }

  public static class GroupPerfReportByGroupCommandBean {
    private Long groupId;
    private String frequency;
    private Integer week;
    private Integer month;
    private Integer year;
    private String subject;
    private String framework;
    private String tenantId;
    private String tenantRoot;

    public Long getGroupId() {
      return groupId;
    }

    public void setGroupId(Long groupId) {
      this.groupId = groupId;
    }

    public String getFrequency() {
      return frequency;
    }

    public void setFrequency(String frequency) {
      this.frequency = frequency;
    }

    public Integer getWeek() {
      return week;
    }

    public void setWeek(Integer week) {
      this.week = week;
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

    public String getSubject() {
      return subject;
    }

    public void setSubject(String subject) {
      this.subject = subject;
    }

    public String getFramework() {
      return framework;
    }

    public void setFramework(String framework) {
      this.framework = framework;
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
