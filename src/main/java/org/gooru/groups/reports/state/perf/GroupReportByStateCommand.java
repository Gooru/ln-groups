
package org.gooru.groups.reports.state.perf;

import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru
 * Created On 19-Mar-2019
 */
public class GroupReportByStateCommand {
  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportByStateCommand.class);

  private Integer stateId;
  private Integer month;
  private Integer year;

  public GroupReportByStateCommand(Integer stateId, Integer month, Integer year) {
    this.stateId = stateId;
    this.month = month;
    this.year = year;
  }

  public Integer getStateId() {
    return stateId;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public static GroupReportByStateCommand build(JsonObject request) {
    GroupReportByStateCommand command = buildFromJson(request);
    command.validate();
    return command;
  }

  private static GroupReportByStateCommand buildFromJson(JsonObject request) {
    Integer state = RequestUtils.getAsInt(request, CommandAttributes.STATE_ID);
    Integer month = RequestUtils.getAsInt(request, CommandAttributes.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributes.YEAR);
    return new GroupReportByStateCommand(state, month, year);
  }

  private void validate() {
    if (month == null || year == null) {
      LOGGER.warn("invalid month or year provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid month or year provided");
    }
  }

  public GroupReportByStateCommandBean asBean() {
    GroupReportByStateCommandBean bean = new GroupReportByStateCommandBean();
    bean.stateId = stateId;
    bean.month = month;
    bean.year = year;
    return bean;
  }

  public static class GroupReportByStateCommandBean {
    private Integer stateId;
    private Integer month;
    private Integer year;
    
    public Integer getStateId() {
      return stateId;
    }

    public void setStateId(Integer stateId) {
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
  }

  static class CommandAttributes {
    private CommandAttributes() {
      throw new AssertionError();
    }

    private static final String STATE_ID = "stateId";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
  }
}
