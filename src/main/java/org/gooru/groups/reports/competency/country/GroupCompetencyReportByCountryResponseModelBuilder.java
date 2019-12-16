
package org.gooru.groups.reports.competency.country;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryResponseModel.Data;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryResponseModel.Drilldown;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryResponseModel.OverallStats;
import org.gooru.groups.reports.dbhelpers.core.StateModel;

/**
 * @author szgooru Created On 14-Dec-2019
 */
public class GroupCompetencyReportByCountryResponseModelBuilder {

  public static GroupCompetencyReportByCountryResponseModel build(
      List<GroupCompetencyReportByCountryModel> competencyReportByWeek,
      List<GroupCompetencyStateWiseReportByCountryModel> competencyReportByState,
      Map<Long, StateModel> states) {
    GroupCompetencyReportByCountryResponseModel responseModel =
        new GroupCompetencyReportByCountryResponseModel();

    List<Data> dataList = new ArrayList<>();
    Long totalCompetencies = 0l;
    for (GroupCompetencyReportByCountryModel weekReport : competencyReportByWeek) {
      dataList.add(prepareDataModel(weekReport));
      totalCompetencies = totalCompetencies + weekReport.getCompletedCompetencies();
    }

    List<Drilldown> drilldownList = new ArrayList<>();
    competencyReportByState.forEach(stateReport -> {
      StateModel stateModel = states.get(stateReport.getStateId());
      drilldownList.add(prepareDrilldownModel(stateReport, stateModel));
    });

    OverallStats overallStats = new OverallStats();
    overallStats.setTotalCompetencies(totalCompetencies);

    responseModel.setOverallStats(overallStats);
    responseModel.setData(dataList);
    responseModel.setDrilldown(drilldownList);
    return responseModel;
  }

  private static Data prepareDataModel(GroupCompetencyReportByCountryModel reportModel) {
    Data dataModel = new Data();
    dataModel.setWeek(reportModel.getWeek());
    dataModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    return dataModel;
  }

  private static Drilldown prepareDrilldownModel(
      GroupCompetencyStateWiseReportByCountryModel reportModel, StateModel stateModel) {
    Drilldown drilldownModel = new Drilldown();
    drilldownModel.setId(reportModel.getStateId());
    drilldownModel.setName(stateModel.getName());
    drilldownModel.setCode(stateModel.getCode());
    drilldownModel.setType("state");
    drilldownModel.setSubType(null);
    drilldownModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    return drilldownModel;
  }
}
