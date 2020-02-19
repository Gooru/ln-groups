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
      CountryModel countryModel = countryModelMap.get(reportModel.getCountryId());
      if (countryModel != null) {
      countries.add(
          populateCountryWithData(reportModel, countryModel));
      }
    });
    responseModel.setCountries(countries);
    return responseModel;
  }

  private Country populateCountryWithData(FetchCountriesForReportModel reportModel,
      CountryModel countryModel) {
    Country country = new Country();
    country.setId(countryModel.getId());
    country.setName(countryModel.getName());
    country.setCode(countryModel.getCode());

    if (reportModel != null) {
      country.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    } else {
      country.setCompletedCompetencies(0l);
    }
    return country;
  }
}
