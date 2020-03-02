package org.gooru.groups.reports.competency.drilldown;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.competency.response.CompetencyReportResponseModel;
import org.gooru.groups.reports.dbhelpers.core.CountryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru on 18-Feb-2020
 *
 */
public class GroupReportResponseModelBuilder {
  
  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportResponseModelBuilder.class);

  public CompetencyReportResponseModel build(GroupReportCommand.GroupReportCommandBean bean,
      List<GroupReportByCountryModel> reportModels, Map<Long, CountryModel> countryModels) {
    CompetencyReportResponseModel responseModel = new CompetencyReportResponseModel();
    responseModel.setContext(populateContext(bean));

    List<CompetencyReportResponseModel.Drilldown> drilldowns = new ArrayList<>();
    reportModels.forEach(reportModel -> {
      LOGGER.debug("populating drilldown for country :{}", reportModel.getCountryId());
      CountryModel countryModel = countryModels.get(reportModel.getCountryId());
      if (countryModel != null) {
        drilldowns.add(populateDrilldown(reportModel, countryModel));
      }
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
    drilldown.setType("country");
    if (reportModel != null) {
      drilldown.setCompletedCompetencies(
          reportModel.getCompletedCompetencies() != null ? reportModel.getCompletedCompetencies()
              : 0l);
      drilldown.setInprogressCompetencies(
          reportModel.getInprogressCompetencies() != null ? reportModel.getInprogressCompetencies()
              : 0l);
    } else {
      drilldown.setCompletedCompetencies(0l);
      drilldown.setInprogressCompetencies(0l);
    }
    return drilldown;
  }
}
