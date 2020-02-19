
package org.gooru.groups.reports.competency.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.competency.school.GroupCompetencyReportBySchoolResponseModel.Data;
import org.gooru.groups.reports.competency.school.GroupCompetencyReportBySchoolResponseModel.Drilldown;
import org.gooru.groups.reports.competency.school.GroupCompetencyReportBySchoolResponseModel.OverallStats;
import org.gooru.groups.reports.dbhelpers.core.ClassModel;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportBySchoolResponseModelBuilder {

  public static GroupCompetencyReportBySchoolResponseModel build(
      List<GroupCompetencyReportBySchoolModel> competencyReportByWeek,
      List<GroupCompetencyClassWiseReportBySchoolModel> competencyReportByClass,
      Map<String, ClassModel> classes, Double averagePerformance) {
    GroupCompetencyReportBySchoolResponseModel responseModel =
        new GroupCompetencyReportBySchoolResponseModel();

    List<Data> dataList = new ArrayList<>();
    Long totalCompetencies = 0l;
    for (GroupCompetencyReportBySchoolModel weekReport : competencyReportByWeek) {
      dataList.add(prepareDataModel(weekReport));
      totalCompetencies = totalCompetencies + weekReport.getCompletedCompetencies();
    }

    Map<String, GroupCompetencyClassWiseReportBySchoolModel> groupReportByClass = new HashMap<>();
    competencyReportByClass.forEach(classReport -> {
      groupReportByClass.put(classReport.getClassId(), classReport);
    });

    List<Drilldown> drilldownList = new ArrayList<>();
    for (Map.Entry<String, ClassModel> entry : classes.entrySet()) {
      drilldownList
          .add(prepareDrilldownModel(groupReportByClass.get(entry.getKey()), entry.getValue()));
    }

    OverallStats overallStats = new OverallStats();
    overallStats.setTotalCompetencies(totalCompetencies);
    overallStats.setAveragePerformance(averagePerformance != null ? averagePerformance : 0d);
    
    responseModel.setOverallStats(overallStats);
    responseModel.setData(dataList);
    responseModel.setDrilldown(drilldownList);
    return responseModel;
  }

  private static Data prepareDataModel(GroupCompetencyReportBySchoolModel reportModel) {
    Data dataModel = new Data();
    dataModel.setWeek(reportModel.getWeek());
    dataModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
    return dataModel;
  }

  private static Drilldown prepareDrilldownModel(
      GroupCompetencyClassWiseReportBySchoolModel reportModel, ClassModel classModel) {
    Drilldown drilldownModel = new Drilldown();
    drilldownModel.setId(classModel.getId());
    drilldownModel.setName(classModel.getTitle());
    drilldownModel.setCode(classModel.getCode());
    drilldownModel.setClassId(classModel.getCourseId());
    drilldownModel.setType("class");
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
}
