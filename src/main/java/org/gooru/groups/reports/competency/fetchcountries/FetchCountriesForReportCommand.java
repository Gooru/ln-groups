package org.gooru.groups.reports.competency.fetchcountries;

import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru
 *
 */
public class FetchCountriesForReportCommand {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(FetchCountriesForReportCommand.class);

  private String tenantId;
  private String tenantRoot;
  private Integer month;
  private Integer year;

  public String getTenantId() {
    return tenantId;
  }

  public String getTenantRoot() {
    return tenantRoot;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public FetchCountriesForReportCommand(String tenantId, String tenantRoot, Integer month,
      Integer year) {
    this.tenantId = tenantId;
    this.tenantRoot = tenantRoot;
    this.month = month;
    this.year = year;
  }

  public static FetchCountriesForReportCommand build(JsonObject request) {
    FetchCountriesForReportCommand command = buildFromJson(request);
    command.validate();
    return command;
  }

  private void validate() {
    if (this.tenantId == null) {
      LOGGER.warn("Invalid tenant provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid tenantId provided");
    }

    if (month == null || year == null) {
      LOGGER.warn("invalid month or year provided");
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid month or year provided");
    }
  }

  private static FetchCountriesForReportCommand buildFromJson(JsonObject request) {
    String tenantId = request.getString(CommandAttributeConstants.TENANT_ID);
    String tenantRoot = request.getString(CommandAttributeConstants.TENANT_ROOT);
    Integer month = RequestUtils.getAsInt(request, CommandAttributeConstants.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributeConstants.YEAR);
    return new FetchCountriesForReportCommand(tenantId, tenantRoot, month, year);
  }

  public FetchCountriesForReportCommandBean asBean() {
    FetchCountriesForReportCommandBean bean = new FetchCountriesForReportCommandBean();
    bean.tenantId = tenantId;
    bean.tenantRoot = tenantRoot;
    bean.month = month;
    bean.year = year;
    return bean;
  }

  public static class FetchCountriesForReportCommandBean {

    private String tenantId;
    private String tenantRoot;
    private Integer month;
    private Integer year;

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
}
