
package org.gooru.groups.reports.competency.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.competency.group.GroupCompentencyReportByGroupReponseModel.Data;
import org.gooru.groups.reports.competency.group.GroupCompentencyReportByGroupReponseModel.Drilldown;
import org.gooru.groups.reports.competency.group.GroupCompentencyReportByGroupReponseModel.OverallStats;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.reports.dbhelpers.core.SchoolModel;

/**
 * @author szgooru Created On 17-Dec-2019
 */
public class GroupCompentencyReportByGroupReponseModelBuilder {

  public static GroupCompentencyReportByGroupReponseModel buildReponseForSDorCluster(
      List<GroupCompetencyReportByGroupModel> competencyReportByWeek,
      List<GroupCompetencyDrillDownReportByGroupOrSchoolModel> competencyReportBySchool,
      Map<Long, SchoolModel> schoolModels) {
    GroupCompentencyReportByGroupReponseModel responseModel =
        new GroupCompentencyReportByGroupReponseModel();
    List<Data> dataList = new ArrayList<>();
    Long totalCompetencies = 0l;
    for (GroupCompetencyReportByGroupModel weekReport : competencyReportByWeek) {
      dataList.add(prepareDataModel(weekReport));
      totalCompetencies = totalCompetencies + weekReport.getCompletedCompetencies();
    }

    List<Drilldown> drilldownList = new ArrayList<>();
    competencyReportBySchool.forEach(schoolReport -> {
      SchoolModel schoolModel = schoolModels.get(schoolReport.getId());
      drilldownList.add(prepareDrilldownModelForSDorCluster(schoolReport, schoolModel));
    });

    OverallStats overallStats = new OverallStats();
    overallStats.setTotalCompetencies(totalCompetencies);

    responseModel.setOverallStats(overallStats);
    responseModel.setData(dataList);
    responseModel.setDrilldown(drilldownList);
    return responseModel;
  }

  public static GroupCompentencyReportByGroupReponseModel buildReponseForDistrictorBlock(
      List<GroupCompetencyReportByGroupModel> competencyReportByWeek,
      List<GroupCompetencyDrillDownReportByGroupOrSchoolModel> competencyReportByGroup,
      Map<Long, GroupModel> groupModels) {
    GroupCompentencyReportByGroupReponseModel responseModel =
        new GroupCompentencyReportByGroupReponseModel();
    List<Data> dataList = new ArrayList<>();
    Long totalCompetencies = 0l;
    for (GroupCompetencyReportByGroupModel weekReport : competencyReportByWeek) {
      dataList.add(prepareDataModel(weekReport));
      totalCompetencies = totalCompetencies + weekReport.getCompletedCompetencies();
    }

    List<Drilldown> drilldownList = new ArrayList<>();
    competencyReportByGroup.forEach(groupReport -> {
      GroupModel groupModel = groupModels.get(groupReport.getId());
      drilldownList.add(prepareDrilldownModelForDistrictorBlock(groupReport, groupModel));
    });

    OverallStats overallStats = new OverallStats();
    overallStats.setTotalCompetencies(totalCompetencies);

    responseModel.setOverallStats(overallStats);
    responseModel.setData(dataList);
    responseModel.setDrilldown(drilldownList);
    return responseModel;
  }

  private static Data prepareDataModel(GroupCompetencyReportByGroupModel reportModel) {
    Data dataModel = new Data();
    dataModel.setWeek(reportModel.getWeek());
    dataModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    return dataModel;
  }

  private static Drilldown prepareDrilldownModelForSDorCluster(
      GroupCompetencyDrillDownReportByGroupOrSchoolModel reportModel, SchoolModel schoolModel) {
    Drilldown drilldownModel = new Drilldown();
    drilldownModel.setId(reportModel.getId());
    drilldownModel.setName(schoolModel.getName());
    drilldownModel.setCode(schoolModel.getCode());
    drilldownModel.setType("school");
    drilldownModel.setSubType(null);
    drilldownModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    drilldownModel.setInprogressCompetencies(reportModel.getInprogressCompetencies());
    return drilldownModel;
  }

  private static Drilldown prepareDrilldownModelForDistrictorBlock(
      GroupCompetencyDrillDownReportByGroupOrSchoolModel reportModel, GroupModel groupModel) {
    Drilldown drilldownModel = new Drilldown();
    drilldownModel.setId(reportModel.getId());
    drilldownModel.setName(groupModel.getName());
    drilldownModel.setCode(groupModel.getCode());
    drilldownModel.setType(groupModel.getType());
    drilldownModel.setSubType(groupModel.getSubType());
    drilldownModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    drilldownModel.setInprogressCompetencies(reportModel.getInprogressCompetencies());
    return drilldownModel;
  }
}
