
package org.gooru.groups.reports.competency.state;

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
public class GroupCompetencyReportByStateCommand {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportByStateCommand.class);

  private Long stateId;
  private Integer month;
  private Integer year;
  private String frequency;

  public GroupCompetencyReportByStateCommand(Long stateId, Integer month, Integer year,
      String frequency) {
    this.stateId = stateId;
    this.month = month;
    this.year = year;
    this.frequency = frequency;
  }

  public Long getStateId() {
    return stateId;
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

  public static GroupCompetencyReportByStateCommand build(JsonObject request) {
    GroupCompetencyReportByStateCommand command = buildFromJson(request);
    command.validate();
    return command;
  }

  private static GroupCompetencyReportByStateCommand buildFromJson(JsonObject request) {
    Long state = RequestUtils.getAsLong(request, CommandAttributeConstants.STATE_ID);
    String frequency = request.getString(CommandAttributeConstants.FREQUENCY);
    Integer month = RequestUtils.getAsInt(request, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributeConstants.YEAR);
    return new GroupCompetencyReportByStateCommand(state, month, year, frequency);
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

  public GroupCompetencyReportByStateCommandBean asBean() {
    GroupCompetencyReportByStateCommandBean bean = new GroupCompetencyReportByStateCommandBean();
    bean.stateId = stateId;
    bean.frequency = frequency;
    bean.month = month;
    bean.year = year;
    return bean;
  }

  public static class GroupCompetencyReportByStateCommandBean {
    private Long stateId;
    private Integer month;
    private Integer year;
    private String frequency;

    public Long getStateId() {
      return stateId;
    }

    public void setStateId(Long stateId) {
      this.stateId = stateId;
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
