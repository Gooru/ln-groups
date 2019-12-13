
package org.gooru.groups.reports.perf.country;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 15-Mar-2019
 */
public class GroupPerfReportByCountryCommand {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupPerfReportByCountryCommand.class);
  
  private Long countryId;
  private Integer month;
  private Integer year;
  private String frequency;
  private Integer week;
  private String subject;
  private String framework;

  public GroupPerfReportByCountryCommand(Long countryId, String frequency, Integer week,
      Integer month, Integer year, String subject, String framework) {
    this.countryId = countryId;
    this.month = month;
    this.year = year;
    this.frequency = frequency;
    this.week = week;
    this.subject = subject;
    this.framework = framework;
  }

  public Long getCountryId() {
    return countryId;
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

  public Integer getWeek() {
    return week;
  }

  public String getSubject() {
    return subject;
  }

  public String getFramework() {
    return framework;
  }

  public static GroupPerfReportByCountryCommand build(JsonObject request) {
    GroupPerfReportByCountryCommand command = buildFromJson(request);
    command.validate();
    return command;
  }

  private static GroupPerfReportByCountryCommand buildFromJson(JsonObject request) {
    Long country = RequestUtils.getAsLong(request, CommandAttributeConstants.COUNTRY_ID);
    String frequency = request.getString(CommandAttributeConstants.FREQUENCY);
    Integer week = RequestUtils.getAsInt(request, CommandAttributeConstants.WEEK);
    Integer month = RequestUtils.getAsInt(request, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributeConstants.YEAR);
    String subject = request.getString(CommandAttributeConstants.SUBJECT);
    String framework = request.getString(CommandAttributeConstants.FRAMEWORK);
    return new GroupPerfReportByCountryCommand(country, frequency, week, month, year, subject, framework);
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

  public GroupReportByCountryCommandBean asBean() {
    GroupReportByCountryCommandBean bean = new GroupReportByCountryCommandBean();
    bean.countryId = countryId;
    bean.frequency = frequency;
    bean.week = week;
    bean.month = month;
    bean.year = year;
    bean.subject = subject;
    bean.framework = framework;
    return bean;
  }

  public static class GroupReportByCountryCommandBean {
    private Long countryId;
    private Integer month;
    private Integer year;
    private String frequency;
    private Integer week;
    private String subject;
    private String framework;

    public Long getCountryId() {
      return countryId;
    }

    public void setCountryId(Long countryId) {
      this.countryId = countryId;
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

    public Integer getWeek() {
      return week;
    }

    public void setWeek(Integer week) {
      this.week = week;
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
