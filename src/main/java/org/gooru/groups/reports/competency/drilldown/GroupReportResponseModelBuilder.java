package org.gooru.groups.reports.competency.drilldown;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.competency.response.CompetencyReportResponseModel;
import org.gooru.groups.reports.dbhelpers.core.CountryModel;

/**
 * @author szgooru on 18-Feb-2020
 *
 */
public class GroupReportResponseModelBuilder {

  public CompetencyReportResponseModel build(GroupReportCommand.GroupReportCommandBean bean,
      List<GroupReportByCountryModel> reportModels, Map<Long, CountryModel> countryModels) {
    CompetencyReportResponseModel responseModel = new CompetencyReportResponseModel();
    responseModel.setContext(populateContext(bean));

    List<CompetencyReportResponseModel.Drilldown> drilldowns = new ArrayList<>();
    reportModels.forEach(reportModel -> {
      drilldowns.add(populateDrilldown(reportModel, countryModels.get(reportModel.getCountryId())));
    });
    responseModel.setDrilldown(drilldowns);
    return responseModel;
  }

  private CompetencyReportResponseModel.Context populateContext(
      GroupReportCommand.GroupReportCommandBean bean) {
    CompetencyReportResponseModel.Context context = new CompetencyReportResponseModel.Context();
    context.setFrequency(null);
    context.setMonth(bean.getMonth());
    context.setYear(bean.getYear());
    context.setReport("competency");
    context.setGroupType("country");
    return context;
  }

  private CompetencyReportResponseModel.Drilldown populateDrilldown(
      GroupReportByCountryModel reportModel, CountryModel countryModel) {
    CompetencyReportResponseModel.Drilldown drilldown =
        new CompetencyReportResponseModel.Drilldown();
    drilldown.setId(countryModel.getId());
    drilldown.setCode(countryModel.getCode());
    drilldown.setName(countryModel.getName());
    drilldown.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    return drilldown;
  }
}
