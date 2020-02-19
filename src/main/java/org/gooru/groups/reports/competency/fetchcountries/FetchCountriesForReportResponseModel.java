package org.gooru.groups.reports.competency.fetchcountries;

import java.util.List;

/**
 * @author szgooru
 *
 */
public class FetchCountriesForReportResponseModel {

  List<Country> countries;
  
  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }

  static class Country {
    private Long id;
    private String name;
    private String code;
    private Long completedCompetencies;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public Long getCompletedCompetencies() {
      return completedCompetencies;
    }

    public void setCompletedCompetencies(Long completedCompetencies) {
      this.completedCompetencies = completedCompetencies;
    }

  }
}
