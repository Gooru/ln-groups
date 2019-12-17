
package org.gooru.groups.reports.competency.group;

import java.util.ArrayList;
import java.util.HashMap;
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
      Map<Long, SchoolModel> schools) {
    GroupCompentencyReportByGroupReponseModel responseModel =
        new GroupCompentencyReportByGroupReponseModel();
    List<Data> dataList = new ArrayList<>();
    Long totalCompetencies = 0l;
    for (GroupCompetencyReportByGroupModel weekReport : competencyReportByWeek) {
      dataList.add(prepareDataModel(weekReport));
      totalCompetencies = totalCompetencies + weekReport.getCompletedCompetencies();
    }

    Map<Long, GroupCompetencyDrillDownReportByGroupOrSchoolModel> schoolReportMap = new HashMap<>();
    competencyReportBySchool.forEach(schoolReport -> {
      schoolReportMap.put(schoolReport.getId(), schoolReport);
    });

    List<Drilldown> drilldownList = new ArrayList<>();
    for (Map.Entry<Long, SchoolModel> entry : schools.entrySet()) {
      drilldownList.add(prepareDrilldownModelForSDorCluster(schoolReportMap.get(entry.getKey()),
          entry.getValue()));
    }

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
      Map<Long, GroupModel> groups) {
    GroupCompentencyReportByGroupReponseModel responseModel =
        new GroupCompentencyReportByGroupReponseModel();
    List<Data> dataList = new ArrayList<>();
    Long totalCompetencies = 0l;
    for (GroupCompetencyReportByGroupModel weekReport : competencyReportByWeek) {
      dataList.add(prepareDataModel(weekReport));
      totalCompetencies = totalCompetencies + weekReport.getCompletedCompetencies();
    }

    Map<Long, GroupCompetencyDrillDownReportByGroupOrSchoolModel> groupReportMap = new HashMap<>();
    competencyReportByGroup.forEach(groupReport -> {
      groupReportMap.put(groupReport.getId(), groupReport);
    });

    List<Drilldown> drilldownList = new ArrayList<>();
    for (Map.Entry<Long, GroupModel> entry : groups.entrySet()) {
      drilldownList.add(prepareDrilldownModelForDistrictorBlock(groupReportMap.get(entry.getKey()),
          entry.getValue()));
    }

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
    drilldownModel.setId(schoolModel.getId());
    drilldownModel.setName(schoolModel.getName());
    drilldownModel.setCode(schoolModel.getCode());
    drilldownModel.setType("school");
    drilldownModel.setSubType(null);
    if (reportModel != null) {
      drilldownModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
      drilldownModel.setInprogressCompetencies(reportModel.getInprogressCompetencies());
    } else {
      drilldownModel.setCompletedCompetencies(0l);
      drilldownModel.setInprogressCompetencies(0l);
    }
    return drilldownModel;
  }

  private static Drilldown prepareDrilldownModelForDistrictorBlock(
      GroupCompetencyDrillDownReportByGroupOrSchoolModel reportModel, GroupModel groupModel) {
    Drilldown drilldownModel = new Drilldown();
    drilldownModel.setId(groupModel.getId());
    drilldownModel.setName(groupModel.getName());
    drilldownModel.setCode(groupModel.getCode());
    drilldownModel.setType(groupModel.getType());
    drilldownModel.setSubType(groupModel.getSubType());
    if (reportModel != null) {
      drilldownModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
      drilldownModel.setInprogressCompetencies(reportModel.getInprogressCompetencies());
    } else {
      drilldownModel.setCompletedCompetencies(0l);
      drilldownModel.setInprogressCompetencies(0l);
    }
    return drilldownModel;
  }
}
