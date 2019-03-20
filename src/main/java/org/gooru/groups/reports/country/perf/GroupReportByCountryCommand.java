
package org.gooru.groups.reports.country.perf;

import org.gooru.groups.reports.utils.RequestUtils;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 15-Mar-2019
 */
public class GroupReportByCountryCommand {

  private Integer countryId;
  private Integer month;
  private Integer year;

  public GroupReportByCountryCommand(Integer countryId, Integer month, Integer year) {
    this.countryId = countryId;
    this.month = month;
    this.year = year;
  }

  public Integer getCountryId() {
    return countryId;
  }
  
  public Integer getMonth() {
    return month;
  }

  public Integer getYear() {
    return year;
  }

  public static GroupReportByCountryCommand build(JsonObject request) {
    GroupReportByCountryCommand command = buildFromJson(request);
    command.validate();
    return command;
  }
  
  private static GroupReportByCountryCommand buildFromJson(JsonObject request) {
    Integer country = RequestUtils.getAsInt(request, CommandAttributes.COUNTRY_ID);
    Integer month = RequestUtils.getAsInt(request, CommandAttributes.MONTH);
    Integer year = RequestUtils.getAsInt(request, CommandAttributes.YEAR);
    return new GroupReportByCountryCommand(country, month, year);
  }
  
  private void validate() {
    // TODO: validation
  }

  public GroupReportByCountryCommandBean asBean() {
    GroupReportByCountryCommandBean bean = new GroupReportByCountryCommandBean();
    bean.countryId = countryId;
    bean.month = month;
    bean.year = year;
    return bean;
  }

  public static class GroupReportByCountryCommandBean {
    private Integer countryId;
    private Integer month;
    private Integer year;
    
    public Integer getCountryId() {
      return countryId;
    }

    public void setCountryId(Integer countryId) {
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
  }
  
  static class CommandAttributes {
    private CommandAttributes() {
      throw new AssertionError();
    }
    
    private static final String COUNTRY_ID = "countryId";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
  }
}
