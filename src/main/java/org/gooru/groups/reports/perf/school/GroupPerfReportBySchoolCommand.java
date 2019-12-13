
package org.gooru.groups.reports.perf.school;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupPerfReportBySchoolCommand {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerfReportBySchoolCommand.class);

  private Long schoolId;
  private String frequency;
  private Integer week;
  private Integer month;
  private Integer year;
  private String subject;
  private String framework;

  public GroupPerfReportBySchoolCommand(Long schoolId, String frequency, Integer week,
      Integer month, Integer year, String subject, String framework) {
    this.schoolId = schoolId;
    this.frequency = frequency;
    this.week = week;
    this.month = month;
    this.year = year;
    this.subject = subject;
    this.framework = framework;
  }

  public Long getSchoolId() {
    return schoolId;
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

  public static GroupPerfReportBySchoolCommand build(JsonObject request) {
    GroupPerfReportBySchoolCommand command = buildFromJson(request);
    command.validate();
    return command;
  }

  private static GroupPerfReportBySchoolCommand buildFromJson(JsonObject request) {
    Long school = RequestUtils.getAsLong(request, CommandAttributeConstants.SCHOOL_ID);
    String frequency = request.getString(CommandAttributeConstants.FREQUENCY);
    Integer week = RequestUtils.getAsInt(request, CommandAttributeConstants.WEEK);
    Integer month = RequestUtils.getAsInt(request, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributeConstants.YEAR);
    String subject = request.getString(CommandAttributeConstants.SUBJECT);
    String framework = request.getString(CommandAttributeConstants.FRAMEWORK);
    return new GroupPerfReportBySchoolCommand(school, frequency, week, month, year, subject,
        framework);
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

  public GroupPerfReportBySchoolCommandBean asBean() {
    GroupPerfReportBySchoolCommandBean bean = new GroupPerfReportBySchoolCommandBean();
    bean.schoolId = schoolId;
    bean.frequency = frequency;
    bean.week = week;
    bean.month = month;
    bean.year = year;
    bean.subject = subject;
    bean.framework = framework;
    return bean;
  }

  public static class GroupPerfReportBySchoolCommandBean {
    private Long schoolId;
    private String frequency;
    private Integer week;
    private Integer month;
    private Integer year;
    private String subject;
    private String framework;

    public Long getSchoolId() {
      return schoolId;
    }

    public void setSchoolId(Long schoolId) {
      this.schoolId = schoolId;
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

  }
}
