package org.gooru.groups.reports.perf.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.app.jdbi.PGArray;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.reports.dbhelpers.core.groupacl.GroupACLResolver;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyDetailsModel;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceDrilldownComputationProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerformanceDrilldownComputationProcessor.class);

  private final GroupPerformanceReportService REPORT_SERVICE =
      new GroupPerformanceReportService(DBICreator.getDbiForDsdbDS());

  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());

  public DataModel processDrilldownComputation(Long groupId, String groupType, Integer month,
      Integer year, PGArray<String> tenants, GroupACLResolver aclResolver,
      Node<GroupHierarchyDetailsModel> groupHierarchy) {
    Set<Long> childNodes = aclResolver.getChildNodes(groupId);
    LOGGER.debug("{} child nodes for the group {}", childNodes.size(), groupId);

    // Fetch the group details from the actual group definition table
    Set<Long> groupIds = new HashSet<>();
    groupIds.addAll(childNodes);
    groupIds.add(groupId);
    Map<Long, GroupModel> groupModels = this.CORE_SERVICE.fetchFlexibleGroupDetails(groupIds);

    if (childNodes == null || childNodes.isEmpty()) {
      LOGGER.debug("seems that user for group {} of type {} does not have any child", groupId,
          groupType);
      return prepareEmptyDataModel(groupModels.get(groupId));
    }

    // classesByGroups
    Map<Long, Set<String>> classesByGroups = aclResolver.getClassesByGroupACL(groupHierarchy);

    Set<String> classes = classesByGroups.get(groupId);
    if (classes == null || classes.isEmpty()) {
      LOGGER.debug("seems that user for group {} of type {} does not have any class access",
          groupId, groupType);
      return prepareEmptyDataModel(groupModels.get(groupId));
    }

    Map<String, List<GroupPerformanceReportModel>> classReport =
        this.REPORT_SERVICE.fetchWeeklyClassPerformance(classes, tenants, month, year);
    
    if (classReport == null || classReport.isEmpty()) {
      LOGGER.debug("looks like there is no data present for any class");
      return prepareEmptyDataModel(groupModels.get(groupId));
    }

    Map<Integer, GroupPerformanceWeeklyDataModel> weeklyReportModels =
        prepareWeeklyDataReport(classReport);

    Map<Long, GroupPerformanceAggregatedDataReportModel> drilldownDataModels = new HashMap<>();
    for (Long childId : childNodes) {
      List<Double> averagePerformances = new ArrayList<>();
      Long totalCollectionTimespent = 0l;
      Long totalAssessmentTimespent = 0l;
      List<Double> averageCollectionTimespents = new ArrayList<>();
      List<Double> averageAssessmentTimespents = new ArrayList<>();
      LOGGER.debug("aggregating data for node {}", childId);
      Set<String> groupClasses = classesByGroups.get(childId);
      if (groupClasses != null && !groupClasses.isEmpty()) {
        for (String id : groupClasses) {
          List<GroupPerformanceReportModel> classModels = classReport.get(id);
          if (classModels != null) {
            for (GroupPerformanceReportModel classModel : classModels) {
              averagePerformances.add(classModel.getAveragePerformance());
              totalCollectionTimespent =
                  totalCollectionTimespent + classModel.getTotalCollectionTimespent();
              totalAssessmentTimespent =
                  totalAssessmentTimespent + classModel.getTotalAssessmentTimespent();
              averageCollectionTimespents.add(classModel.getAverageCollectionTimespent());
              averageAssessmentTimespents.add(classModel.getAverageAssessmentTimespent());
            }
          } else {
            LOGGER.debug("No data present for the class '{}'", id);
          }
        }
      }

      GroupModel groupModel = groupModels.get(childId);
      drilldownDataModels.put(childId,
          prepareAggregatedDataReportModel(groupModel, averagePerformances,
              totalCollectionTimespent, totalAssessmentTimespent, averageCollectionTimespents,
              averageAssessmentTimespents));
    }
    return prepareDataModel(groupId, weeklyReportModels, drilldownDataModels, groupModels);
  }

  private Map<Integer, GroupPerformanceWeeklyDataModel> prepareWeeklyDataReport(
      Map<String, List<GroupPerformanceReportModel>> classReports) {

    Map<Integer, WeekData> initialWeekData = new HashMap<>();

    for (String classId : classReports.keySet()) {
      List<GroupPerformanceReportModel> reportModels = classReports.get(classId);
      reportModels.forEach(model -> {
        Integer week = model.getWeek();
        if (initialWeekData.containsKey(week)) {
          WeekData weekData = initialWeekData.get(week);
          weekData.getAveragePerformances().add(model.getAveragePerformance());
          weekData.setTotalCollectionTimespent(
              weekData.getTotalCollectionTimespent() + model.getTotalCollectionTimespent());
          weekData.setTotalAssessmentTimespent(
              weekData.getTotalAssessmentTimespent() + model.getTotalAssessmentTimespent());
          weekData.getAverageCollectionTimespents().add(model.getAverageCollectionTimespent());
          weekData.getAverageAssessmentTimespents().add(model.getAverageAssessmentTimespent());

        } else {
          WeekData weekData = new WeekData();

          List<Double> averagePerformances = new ArrayList<>();
          averagePerformances.add(model.getAveragePerformance());
          weekData.setAveragePerformances(averagePerformances);

          List<Double> averageCollectionTimespents = new ArrayList<>();
          averageCollectionTimespents.add(model.getAverageCollectionTimespent());
          weekData.setAverageCollectionTimespents(averageCollectionTimespents);

          List<Double> averageAssessmentTimespents = new ArrayList<>();
          averageAssessmentTimespents.add(model.getAverageAssessmentTimespent());
          weekData.setAverageAssessmentTimespents(averageAssessmentTimespents);

          weekData.setTotalCollectionTimespent(model.getTotalCollectionTimespent());
          weekData.setTotalAssessmentTimespent(model.getTotalAssessmentTimespent());

          initialWeekData.put(week, weekData);
        }
      });
    }

    Map<Integer, GroupPerformanceWeeklyDataModel> weeklyDataReport = new HashMap<>();
    for (Integer week : initialWeekData.keySet()) {
      WeekData weekData = initialWeekData.get(week);

      Double averagePerformance = weekData.getAveragePerformances().stream()
          .collect(Collectors.averagingDouble(perf -> perf));
      Double averageCollectionTimespent = weekData.getAverageCollectionTimespents().stream()
          .collect(Collectors.averagingDouble(ts -> ts));
      Double averageAssessmentTimespent = weekData.getAverageAssessmentTimespents().stream()
          .collect(Collectors.averagingDouble(ts -> ts));

      GroupPerformanceWeeklyDataModel dataModel = new GroupPerformanceWeeklyDataModel();
      dataModel.setLabel(week);
      dataModel.setAveragePerformance(averagePerformance);
      dataModel.setTotalCollectionTimespent(weekData.getTotalCollectionTimespent());
      dataModel.setTotalAssessmentTimespent(weekData.getTotalAssessmentTimespent());
      dataModel.setAverageCollectionTimespent(averageCollectionTimespent);
      dataModel.setAverageAssessmentTimespent(averageAssessmentTimespent);
      weeklyDataReport.put(week, dataModel);
    }
    return weeklyDataReport;
  }

  static class WeekData {
    List<Double> averagePerformances;
    Long totalCollectionTimespent;
    Long totalAssessmentTimespent;
    List<Double> averageCollectionTimespents;
    List<Double> averageAssessmentTimespents;

    public List<Double> getAveragePerformances() {
      return averagePerformances;
    }

    public void setAveragePerformances(List<Double> averagePerformances) {
      this.averagePerformances = averagePerformances;
    }

    public Long getTotalCollectionTimespent() {
      return totalCollectionTimespent;
    }

    public void setTotalCollectionTimespent(Long totalCollectionTimespent) {
      this.totalCollectionTimespent = totalCollectionTimespent;
    }

    public Long getTotalAssessmentTimespent() {
      return totalAssessmentTimespent;
    }

    public void setTotalAssessmentTimespent(Long totalAssessmentTimespent) {
      this.totalAssessmentTimespent = totalAssessmentTimespent;
    }

    public List<Double> getAverageCollectionTimespents() {
      return averageCollectionTimespents;
    }

    public void setAverageCollectionTimespents(List<Double> averageCollectionTimespents) {
      this.averageCollectionTimespents = averageCollectionTimespents;
    }

    public List<Double> getAverageAssessmentTimespents() {
      return averageAssessmentTimespents;
    }

    public void setAverageAssessmentTimespents(List<Double> averageAssessmentTimespents) {
      this.averageAssessmentTimespents = averageAssessmentTimespents;
    }

  }

  private DataModel prepareDataModel(Long groupId,
      Map<Integer, GroupPerformanceWeeklyDataModel> weeklyReportModels,
      Map<Long, GroupPerformanceAggregatedDataReportModel> drilldownDataModels,
      Map<Long, GroupModel> groupModels) {
    DataModel dataModel = new DataModel();
    GroupModel groupModel = groupModels.get(groupId);
    if (groupModel != null) {
      dataModel.setId(groupId);
      dataModel.setCode(groupModel.getCode());
      dataModel.setName(groupModel.getName());
      dataModel.setType(groupModel.getType());
    }
    dataModel.setOverallStats(prepareOverallStatsModel(drilldownDataModels));
    dataModel.setCoordinates(
        new ArrayList<GroupPerformanceWeeklyDataModel>(weeklyReportModels.values()));
    dataModel.setDrilldown(
        new ArrayList<GroupPerformanceAggregatedDataReportModel>(drilldownDataModels.values()));
    return dataModel;
  }

  private GroupPerformanceAggregatedDataReportModel prepareAggregatedDataReportModel(
      GroupModel groupModel, List<Double> averagePerformances, Long totalCollectionTimespent,
      Long totalAssessmentTimespent, List<Double> averageCollectionTimespents,
      List<Double> averageAssessmentTimespents) {

    Double averagePerformance =
        averagePerformances.stream().collect(Collectors.averagingDouble(perf -> perf));
    Double averageCollectionTimespent =
        averageCollectionTimespents.stream().collect(Collectors.averagingDouble(ts -> ts));
    Double averageAssessmentTimespent =
        averageAssessmentTimespents.stream().collect(Collectors.averagingDouble(ts -> ts));
    GroupPerformanceAggregatedDataReportModel aggregatedModel =
        new GroupPerformanceAggregatedDataReportModel();

    aggregatedModel.setGroupId(groupModel.getId());
    aggregatedModel.setName(groupModel.getName());
    aggregatedModel.setCode(groupModel.getCode());
    aggregatedModel.setType(groupModel.getType());
    aggregatedModel.setAveragePerformance(averagePerformance);
    aggregatedModel.setTotalCollectionTimespent(totalCollectionTimespent);
    aggregatedModel.setTotalAssessmentTimespent(totalAssessmentTimespent);
    aggregatedModel.setAverageCollectionTimespent(averageCollectionTimespent);
    aggregatedModel.setAverageAssessmentTimespent(averageAssessmentTimespent);

    return aggregatedModel;
  }

  private GroupPerformanceOverallStatsModel prepareOverallStatsModel(
      Map<Long, GroupPerformanceAggregatedDataReportModel> drilldownDataModels) {

    if (drilldownDataModels == null || drilldownDataModels.isEmpty()) {
      return new GroupPerformanceOverallStatsModel();
    }

    GroupPerformanceOverallStatsModel overallStatsModel = new GroupPerformanceOverallStatsModel();
    List<Double> averagePerformances = new ArrayList<>();
    Long totalCollectionTimespent = 0l;
    Long totalAssessmentTimespent = 0l;
    List<Double> averageCollectionTimespents = new ArrayList<>();
    List<Double> averageAssessmentTimespents = new ArrayList<>();

    for (Long id : drilldownDataModels.keySet()) {
      GroupPerformanceAggregatedDataReportModel aggregatedModel = drilldownDataModels.get(id);
      averagePerformances.add(aggregatedModel.getAveragePerformance());
      totalCollectionTimespent =
          totalCollectionTimespent + aggregatedModel.getTotalCollectionTimespent();
      totalAssessmentTimespent =
          totalAssessmentTimespent + aggregatedModel.getTotalAssessmentTimespent();
      averageCollectionTimespents.add(aggregatedModel.getAverageCollectionTimespent());
      averageAssessmentTimespents.add(aggregatedModel.getAverageAssessmentTimespent());
    }

    Double averagePerformance =
        averagePerformances.stream().collect(Collectors.averagingDouble(perf -> perf));
    Double averageCollectionTimespent =
        averageCollectionTimespents.stream().collect(Collectors.averagingDouble(ts -> ts));
    Double averageAssessmentTimespent =
        averageAssessmentTimespents.stream().collect(Collectors.averagingDouble(ts -> ts));

    overallStatsModel.setAveragePerformance(averagePerformance);
    overallStatsModel.setTotalCollectionTimespent(totalCollectionTimespent);
    overallStatsModel.setTotalAssessmentTimespent(totalAssessmentTimespent);
    overallStatsModel.setAverageCollectionTimespent(averageCollectionTimespent);
    overallStatsModel.setAverageAssessmentTimespent(averageAssessmentTimespent);

    return overallStatsModel;
  }

  private DataModel prepareEmptyDataModel(GroupModel groupModel) {
    DataModel dataModel = new DataModel();
    dataModel.setId(groupModel.getId());
    dataModel.setCode(groupModel.getCode());
    dataModel.setName(groupModel.getName());
    dataModel.setType(groupModel.getType());
    dataModel.setCoordinates(new ArrayList<GroupPerformanceWeeklyDataModel>());
    dataModel.setDrilldown(new ArrayList<GroupPerformanceAggregatedDataReportModel>());
    return dataModel;
  }
}