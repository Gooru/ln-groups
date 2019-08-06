
package org.gooru.groups.reports.group.perf;

import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupReportByGroupCommand {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportByGroupCommand.class);

  private Integer groupId;
  private Integer month;
  private Integer year;

  public GroupReportByGroupCommand(Integer groupId, Integer month, Integer year) {
    this.groupId = groupId;
    this.month = month;
    this.year = year;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public static GroupReportByGroupCommand build(JsonObject request) {
    GroupReportByGroupCommand command = buildFromJson(request);
    command.validate();
    return command;
  }

  private static GroupReportByGroupCommand buildFromJson(JsonObject request) {
    Integer groupId = RequestUtils.getAsInt(request, CommandAttributes.GROUP_ID);
    LOGGER.debug("group id:{}", groupId);
    Integer month = RequestUtils.getAsInt(request, CommandAttributes.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributes.YEAR);
    return new GroupReportByGroupCommand(groupId, month, year);
  }

  private void validate() {
    if (month == null || year == null) {
      LOGGER.warn("invalid month or year provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid month or year provided");
    }
  }

  public GroupReportByGroupCommandBean asBean() {
    GroupReportByGroupCommandBean bean = new GroupReportByGroupCommandBean();
    bean.groupId = groupId;
    bean.month = month;
    bean.year = year;
    return bean;
  }

  public static class GroupReportByGroupCommandBean {
    private Integer groupId;
    private Integer month;
    private Integer year;

    public Integer getGroupId() {
      return groupId;
    }

    public void setGroupId(Integer groupId) {
      this.groupId = groupId;
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
  }

  static class CommandAttributes {
    private CommandAttributes() {
      throw new AssertionError();
    }

    private static final String GROUP_ID = "groupId";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
  }
}
