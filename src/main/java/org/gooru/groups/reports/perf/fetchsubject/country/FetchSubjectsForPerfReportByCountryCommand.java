
package org.gooru.groups.reports.perf.fetchsubject.country;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public class FetchSubjectsForPerfReportByCountryCommand {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(FetchSubjectsForPerfReportByCountryCommand.class);

  private Long countryId;
  private Integer week;
  private Integer month;
  private Integer year;
  private String frequency;
  private String tenantId;
  private String tenantRoot;

  public FetchSubjectsForPerfReportByCountryCommand(Long countryId, String frequency, Integer week,
      Integer month, Integer year, String tenantId, String tenantRoot) {
    this.countryId = countryId;
    this.month = month;
    this.year = year;
    this.frequency = frequency;
    this.week = week;
    this.tenantId = tenantId;
    this.tenantRoot = tenantRoot;
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

  public String getTenantId() {
    return tenantId;
  }

  public String getTenantRoot() {
    return tenantRoot;
  }

  public static FetchSubjectsForPerfReportByCountryCommand build(JsonObject request, JsonObject tenantJson) {
    FetchSubjectsForPerfReportByCountryCommand command = buildFromJson(request, tenantJson);
    command.validate();
    return command;
  }

  private static FetchSubjectsForPerfReportByCountryCommand buildFromJson(JsonObject request, JsonObject tenantJson) {
    String tenantId = tenantJson.getString(CommandAttributeConstants.TENANT_ID);
    String tenantRoot = tenantJson.getString(CommandAttributeConstants.TENANT_ROOT);
    Long country = RequestUtils.getAsLong(request, CommandAttributeConstants.COUNTRY_ID);
    String frequency = request.getString(CommandAttributeConstants.FREQUENCY);
    Integer week = RequestUtils.getAsInt(request, CommandAttributeConstants.WEEK);
    Integer month = RequestUtils.getAsInt(request, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributeConstants.YEAR);
    return new FetchSubjectsForPerfReportByCountryCommand(country, frequency, week, month, year, tenantId, tenantRoot);
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
  }

  public FetchSubjectsForPerfReportByCountryCommandBean asBean() {
    FetchSubjectsForPerfReportByCountryCommandBean bean =
        new FetchSubjectsForPerfReportByCountryCommandBean();
    bean.countryId = countryId;
    bean.frequency = frequency;
    bean.week = week;
    bean.month = month;
    bean.year = year;
    bean.tenantId = tenantId;
    bean.tenantRoot = tenantRoot;
    return bean;
  }

  public static class FetchSubjectsForPerfReportByCountryCommandBean {
    private Long countryId;
    private Integer month;
    private Integer year;
    private String frequency;
    private Integer week;
    private String tenantId;
    private String tenantRoot;

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
