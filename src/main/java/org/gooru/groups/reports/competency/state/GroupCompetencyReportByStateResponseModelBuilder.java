
package org.gooru.groups.reports.competency.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateResponseModel.Data;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateResponseModel.Drilldown;
import org.gooru.groups.reports.competency.state.GroupCompetencyReportByStateResponseModel.OverallStats;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportByStateResponseModelBuilder {

  public static GroupCompetencyReportByStateResponseModel build(
      List<GroupCompetencyReportByStateModel> competencyReportByWeek,
      List<GroupCompetencyGroupWiseReportByStateModel> competencyReportByGroup,
      Map<Long, GroupModel> groups) {
    GroupCompetencyReportByStateResponseModel responseModel =
        new GroupCompetencyReportByStateResponseModel();

    List<Data> dataList = new ArrayList<>();
    Long totalCompetencies = 0l;
    for (GroupCompetencyReportByStateModel weekReport : competencyReportByWeek) {
      dataList.add(prepareDataModel(weekReport));
      totalCompetencies = totalCompetencies + weekReport.getCompletedCompetencies();
    }

    List<Drilldown> drilldownList = new ArrayList<>();
    competencyReportByGroup.forEach(groupReport -> {
      GroupModel groupModel = groups.get(groupReport.getGroupId());
      drilldownList.add(prepareDrilldownModel(groupReport, groupModel));
    });

    OverallStats overallStats = new OverallStats();
    overallStats.setTotalCompetencies(totalCompetencies);

    responseModel.setOverallStats(overallStats);
    responseModel.setData(dataList);
    responseModel.setDrilldown(drilldownList);
    return responseModel;
  }

  private static Data prepareDataModel(GroupCompetencyReportByStateModel reportModel) {
    Data dataModel = new Data();
    dataModel.setWeek(reportModel.getWeek());
    dataModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    return dataModel;
  }

  private static Drilldown prepareDrilldownModel(
      GroupCompetencyGroupWiseReportByStateModel reportModel, GroupModel groupModel) {
    Drilldown drilldownModel = new Drilldown();
    drilldownModel.setId(reportModel.getGroupId());
    drilldownModel.setName(groupModel.getName());
    drilldownModel.setCode(groupModel.getCode());
    drilldownModel.setType(groupModel.getType());
    drilldownModel.setSubType(groupModel.getSubType());
    drilldownModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    drilldownModel.setInprogressCompetencies(reportModel.getInprogressCompetencies());
    return drilldownModel;
  }
}
