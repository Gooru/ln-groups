
package org.gooru.groups.reports.competency.group;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportByGroupCommand {
  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportByGroupCommand.class);

  private Long groupId;
  private Integer month;
  private Integer year;
  private String frequency;

  public GroupCompetencyReportByGroupCommand(Long groupId, Integer month, Integer year,
      String frequency) {
    this.groupId = groupId;
    this.month = month;
    this.year = year;
    this.frequency = frequency;
  }

  public Long getGroupId() {
    return groupId;
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

  public static GroupCompetencyReportByGroupCommand build(JsonObject request) {
    GroupCompetencyReportByGroupCommand command = buildFromJson(request);
    command.validate();
    return command;
  }

  private static GroupCompetencyReportByGroupCommand buildFromJson(JsonObject request) {
    Long group = RequestUtils.getAsLong(request, CommandAttributeConstants.GROUP_ID);
    String frequency = request.getString(CommandAttributeConstants.FREQUENCY);
    Integer month = RequestUtils.getAsInt(request, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributeConstants.YEAR);
    return new GroupCompetencyReportByGroupCommand(group, month, year, frequency);
  }

  private void validate() {
    if (frequency == null
        || !frequency.equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_MONTHLY)) {
      LOGGER.warn("invalid value of frequency provided in the request");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid value of frequency provided");
    }

    if (month == null || year == null) {
      LOGGER.warn("invalid month or year provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid month or year provided");
    }
  }

  public GroupCompetencyReportByGroupCommandBean asBean() {
    GroupCompetencyReportByGroupCommandBean bean = new GroupCompetencyReportByGroupCommandBean();
    bean.groupId = groupId;
    bean.frequency = frequency;
    bean.month = month;
    bean.year = year;
    return bean;
  }

  public static class GroupCompetencyReportByGroupCommandBean {
    private Long groupId;
    private Integer month;
    private Integer year;
    private String frequency;

    public Long getGroupId() {
      return groupId;
    }

    public void setGroupId(Long groupId) {
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

    public String getFrequency() {
      return frequency;
    }

    public void setFrequency(String frequency) {
      this.frequency = frequency;
    }

  }
}
