
package org.gooru.groups.reports.school.perf;

import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupReportBySchoolCommand {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportBySchoolCommand.class);

  private Integer schoolId;
  private Integer month;
  private Integer year;

  public GroupReportBySchoolCommand(Integer schoolId, Integer month, Integer year) {
    this.schoolId = schoolId;
    this.month = month;
    this.year = year;
  }

  public Integer getSchoolId() {
    return schoolId;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public static GroupReportBySchoolCommand build(JsonObject request) {
    GroupReportBySchoolCommand command = buildFromJson(request);
    command.validate();
    return command;
  }

  private static GroupReportBySchoolCommand buildFromJson(JsonObject request) {
    Integer school = RequestUtils.getAsInt(request, CommandAttributes.SCHOOL_ID);
    Integer month = RequestUtils.getAsInt(request, CommandAttributes.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributes.YEAR);
    return new GroupReportBySchoolCommand(school, month, year);
  }

  private void validate() {
    if (month == null || year == null) {
      LOGGER.warn("invalid month or year provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid month or year provided");
    }
  }

  public GroupReportBySchoolCommandBean asBean() {
    GroupReportBySchoolCommandBean bean = new GroupReportBySchoolCommandBean();
    bean.schoolId = schoolId;
    bean.month = month;
    bean.year = year;
    return bean;
  }

  public static class GroupReportBySchoolCommandBean {
    private Integer schoolId;
    private Integer month;
    private Integer year;
    
    public Integer getSchoolId() {
      return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
      this.schoolId = schoolId;
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

    private static final String SCHOOL_ID = "schoolId";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
  }
}
