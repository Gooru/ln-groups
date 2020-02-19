
package org.gooru.groups.reports.competency.country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryResponseModel.Data;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryResponseModel.Drilldown;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryResponseModel.OverallStats;
import org.gooru.groups.reports.dbhelpers.core.DrilldownModel;

/**
 * @author szgooru Created On 14-Dec-2019
 */
public class GroupCompetencyReportByCountryResponseModelBuilder {

  public GroupCompetencyReportByCountryResponseModel build(
      List<GroupCompetencyReportByCountryModel> competencyReportByWeek,
      List<GroupCompetencyDrilldownReportByCountryModel> competencyReportByState,
      Map<Long, DrilldownModel> states, Double averagePerformance, String type) {
    GroupCompetencyReportByCountryResponseModel responseModel =
        new GroupCompetencyReportByCountryResponseModel();

    List<Data> dataList = new ArrayList<>();
    Long totalCompetencies = 0l;
    for (GroupCompetencyReportByCountryModel weekReport : competencyReportByWeek) {
      dataList.add(prepareDataModel(weekReport));
      totalCompetencies = totalCompetencies + weekReport.getCompletedCompetencies();
    }

    Map<Long, GroupCompetencyDrilldownReportByCountryModel> stateReportMap = new HashMap<>();
    competencyReportByState.forEach(stateReport -> {
      stateReportMap.put(stateReport.getId(), stateReport);
    });

    List<Drilldown> drilldownList = new ArrayList<>();
    for (Map.Entry<Long, DrilldownModel> entry : states.entrySet()) {
      drilldownList
          .add(prepareDrilldownModel(stateReportMap.get(entry.getKey()), entry.getValue(), type));
    }

    OverallStats overallStats = new OverallStats();
    overallStats.setTotalCompetencies(totalCompetencies);
    overallStats.setAveragePerformance(averagePerformance != null ? averagePerformance : 0d);

    responseModel.setOverallStats(overallStats);
    responseModel.setData(dataList);
    responseModel.setDrilldown(drilldownList);
    return responseModel;
  }

  private Data prepareDataModel(GroupCompetencyReportByCountryModel reportModel) {
    Data dataModel = new Data();
    dataModel.setWeek(reportModel.getWeek());
    dataModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    return dataModel;
  }

  private Drilldown prepareDrilldownModel(GroupCompetencyDrilldownReportByCountryModel reportModel,
      DrilldownModel stateModel, String type) {
    Drilldown drilldownModel = new Drilldown();
    if (reportModel != null) {
      drilldownModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
      drilldownModel.setInprogressCompetencies(reportModel.getInprogressCompetencies());
    } else {
      drilldownModel.setCompletedCompetencies(0l);
      drilldownModel.setInprogressCompetencies(0l);
    }

    drilldownModel.setId(stateModel.getId());
    drilldownModel.setName(stateModel.getName());
    drilldownModel.setCode(stateModel.getCode());
    drilldownModel.setType(type);
    drilldownModel.setSubType(null);

    return drilldownModel;
  }
}
