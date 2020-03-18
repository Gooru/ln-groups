
package org.gooru.groups.reports.competency.country;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 14-Dec-2019
 */
public class GroupCompetencyReportByCountryCommand {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportByCountryCommand.class);

  private Long countryId;
  private Integer month;
  private Integer year;
  private String frequency;
  private String tenantId;
  private String tenantRoot;

  public GroupCompetencyReportByCountryCommand(Long countryId, Integer month, Integer year,
      String frequency, String tenantId, String tenantRoot) {
    this.countryId = countryId;
    this.month = month;
    this.year = year;
    this.frequency = frequency;
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

  public String getTenantId() {
    return tenantId;
  }

  public String getTenantRoot() {
    return tenantRoot;
  }

  public static GroupCompetencyReportByCountryCommand build(JsonObject request,
      JsonObject tenantJson) {
    GroupCompetencyReportByCountryCommand command = buildFromJson(request, tenantJson);
    command.validate();
    return command;
  }

  private static GroupCompetencyReportByCountryCommand buildFromJson(JsonObject request,
      JsonObject tenantJson) {
    String tenantId = tenantJson.getString(CommandAttributeConstants.TENANT_ID);
    String tenantRoot = tenantJson.getString(CommandAttributeConstants.TENANT_ROOT);
    Long country = RequestUtils.getAsLong(request, CommandAttributeConstants.COUNTRY_ID);
    String frequency = request.getString(CommandAttributeConstants.FREQUENCY);
    Integer month = RequestUtils.getAsInt(request, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributeConstants.YEAR);
    return new GroupCompetencyReportByCountryCommand(country, month, year, frequency, tenantId,
        tenantRoot);
  }

  private void validate() {
    if (this.tenantId == null) {
      LOGGER.warn("Invalid tenant provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid tenantId provided");
    }

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

  public GroupCompetencyReportByCountryCommandBean asBean() {
    GroupCompetencyReportByCountryCommandBean bean =
        new GroupCompetencyReportByCountryCommandBean();
    bean.countryId = countryId;
    bean.frequency = frequency;
    bean.month = month;
    bean.year = year;
    bean.tenantId = tenantId;
    bean.tenantRoot = tenantRoot;
    return bean;
  }

  public static class GroupCompetencyReportByCountryCommandBean {
    private Long countryId;
    private Integer month;
    private Integer year;
    private String frequency;
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
