package org.gooru.groups.reports.competency.fetchcountries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.competency.fetchcountries.FetchCountriesForReportResponseModel.Country;
import org.gooru.groups.reports.dbhelpers.core.CountryModel;

/**
 * @author szgooru
 *
 */
public class FetchCountriesForReportResponseModelBuilder {

  public FetchCountriesForReportResponseModel build(List<FetchCountriesForReportModel> compCounts,
      Map<Long, CountryModel> countryModelMap) {
    FetchCountriesForReportResponseModel responseModel = new FetchCountriesForReportResponseModel();
    List<Country> countries = new ArrayList<>();
    compCounts.forEach(reportModel -> {
      countries.add(
          populateCountryWithData(reportModel, countryModelMap.get(reportModel.getCountryId())));
    });
    responseModel.setCountries(countries);
    return responseModel;
  }

  private Country populateCountryWithData(FetchCountriesForReportModel reportModel,
      CountryModel countryModel) {
    Country country = new Country();
    country.setId(countryModel.getId());
    country.setName(country.getName());
    country.setCode(country.getCode());

    if (reportModel != null) {
      country.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    } else {
      country.setCompletedCompetencies(0l);
    }
    return country;
  }
}
