package org.gooru.groups.reports.competency.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.reports.dbhelpers.core.groupacl.GroupACLResolver;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyDetailsModel;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru on 29-Apr-2020
 *
 */
public class DrilldownDataComputationProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(DrilldownDataComputationProcessor.class);
  
  private final GroupCompetencyReportService REPORT_SERVICE =
      new GroupCompetencyReportService(DBICreator.getDbiForDsdbDS());

  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());

  public DataModel processDrilldownComputation(
      Long groupId, String groupType, Integer month, Integer year, Set<String> tenants,
      GroupACLResolver aclResolver, Node<GroupHierarchyDetailsModel> groupHierarchy) {
    
    Set<Long> childNodes = aclResolver.getChildNodes(groupId);
    LOGGER.debug("{} child nodes for the group {}", childNodes.size(), groupId);
    
    // Fetch the group details from the actual group definition table
    Set<Long> groupIds = new HashSet<>();
    groupIds.addAll(childNodes);
    groupIds.add(groupId);
    Map<Long, GroupModel> groupModels = this.CORE_SERVICE.fetchFlexibleGroupDetails(groupIds);
    
    if (childNodes == null || childNodes.isEmpty()) {
      LOGGER.debug("seems that user for group {} of type {} does not have any child",
          groupId, groupType);
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

    // Fetch the competency report for all the classes we resolved above using the user acl. This
    // will return the data for all the classes without grouping them by the specific parent in
    // which the classes fall under.
    Map<String, List<GroupCompetencyReportModel>> classReport =
        this.REPORT_SERVICE.fetchCompetencyReportByMonthYear(classes, tenants, month, year);

    Map<Integer, WeekDataModel> weeklyDataModels = prepareWeeeklyDataReport(classReport);

    Map<Long, AggregatedDataGroupReportModel> drilldownDataModels = new HashMap<>();
    for (Long childId : childNodes) {
      Long completedCompetencies = 0l;
      Long inferredCompetencies = 0l;
      Long inprogressCompetencies = 0l;
      Long notstartedCompetencies = 0l;
      LOGGER.debug("aggregating data for node {}", childId);
      Set<String> groupClasses = classesByGroups.get(childId);
      if (groupClasses != null && !groupClasses.isEmpty()) {
        for (String id : groupClasses) {
          List<GroupCompetencyReportModel> classModels = classReport.get(id);
          if (classModels != null) {
            for (GroupCompetencyReportModel classModel : classModels) {
              completedCompetencies = completedCompetencies + classModel.getCompletedCompetencies();
              inferredCompetencies = inferredCompetencies + classModel.getInferredCompetencies();
              inprogressCompetencies =
                  inprogressCompetencies + classModel.getInprogressCompetencies();
              notstartedCompetencies =
                  notstartedCompetencies + classModel.getNotstartedCompetencies();
            } 
          } else {
            LOGGER.debug("No data present for the class '{}'", id);
          }
        }
      }

      GroupModel groupModel = groupModels.get(childId);

      drilldownDataModels.put(childId,
          prepareAggregatedDataReportModel(groupModel, completedCompetencies, inferredCompetencies,
              inprogressCompetencies, notstartedCompetencies));
    }

    return prepareDataModel(groupId, weeklyDataModels, drilldownDataModels, groupModels);
  }
  
  private static OverallStatsModel prepareOverallStatsModel(
      Map<Long, AggregatedDataGroupReportModel> drilldownDataModels) {

    if (drilldownDataModels == null || drilldownDataModels.isEmpty()) {
      return new OverallStatsModel();
    }

    // Overall stats
    Long totalCompletedCompetencies = 0l;
    Long totalInferredCompetencies = 0l;
    Long totalInprogressCompetencies = 0l;
    Long totalNotstartedCompetencies = 0l;

    for (Long id : drilldownDataModels.keySet()) {
      AggregatedDataGroupReportModel aggregatedDataModel = drilldownDataModels.get(id);
      totalCompletedCompetencies =
          totalCompletedCompetencies + aggregatedDataModel.getCompletedCompetencies();
      totalInferredCompetencies =
          totalInferredCompetencies + aggregatedDataModel.getInferredCompetencies();
      totalInprogressCompetencies =
          totalInprogressCompetencies + aggregatedDataModel.getInprogressCompetencies();
      totalNotstartedCompetencies =
          totalNotstartedCompetencies + aggregatedDataModel.getNotstartedCompetencies();
    }

    OverallStatsModel statsModel = new OverallStatsModel();
    statsModel.setTotalCompletedCompetencies(totalCompletedCompetencies);
    statsModel.setTotalInferredCompetencies(totalInferredCompetencies);
    statsModel.setTotalInprogressCompetencies(totalInprogressCompetencies);
    statsModel.setTotalNotstartedCompetencies(totalNotstartedCompetencies);

    return statsModel;
  }

  private Map<Integer, WeekDataModel> prepareWeeeklyDataReport(
      Map<String, List<GroupCompetencyReportModel>> classReport) {
    // Data Aggregation by Week
    Map<Integer, WeekDataModel> weeklyDataModels = new HashMap<>();
    for (String classId : classReport.keySet()) {
      List<GroupCompetencyReportModel> reportModels = classReport.get(classId);

      reportModels.forEach(reportModel -> {
        Integer week = reportModel.getWeek();

        if (weeklyDataModels.containsKey(week)) {
          WeekDataModel weekModel = weeklyDataModels.get(week);
          weekModel.setCompletedCompetencies(
              weekModel.getCompletedCompetencies() + reportModel.getCompletedCompetencies());
          weekModel.setInferredCompetencies(
              weekModel.getInferredCompetencies() + reportModel.getInferredCompetencies());
          weekModel.setInprogressCompetencies(
              weekModel.getInprogressCompetencies() + reportModel.getInprogressCompetencies());
          weekModel.setNotstartedCompetencies(
              weekModel.getNotstartedCompetencies() + reportModel.getNotstartedCompetencies());
        } else {
          WeekDataModel weekModel = new WeekDataModel();
          weekModel.setLabel(week);
          weekModel.setCompletedCompetencies(reportModel.getCompletedCompetencies());
          weekModel.setInferredCompetencies(reportModel.getInferredCompetencies());
          weekModel.setInprogressCompetencies(reportModel.getInprogressCompetencies());
          weekModel.setNotstartedCompetencies(reportModel.getNotstartedCompetencies());
          weeklyDataModels.put(week, weekModel);
        }
      });
    }

    return weeklyDataModels;
  }
  
  private DataModel prepareDataModel(Long groupId, Map<Integer, WeekDataModel> weeklyDataModels,
      Map<Long, AggregatedDataGroupReportModel> drilldownDataModels,
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
    dataModel.setCoordinates(new ArrayList<WeekDataModel>(weeklyDataModels.values()));
    dataModel
        .setDrilldown(new ArrayList<AggregatedDataGroupReportModel>(drilldownDataModels.values()));
    return dataModel;
  }
  
  private AggregatedDataGroupReportModel prepareAggregatedDataReportModel(GroupModel groupModel,
      Long completedCompetencies, Long inferredCompetencies, Long inprogressCompetencies,
      Long notstartedCompetencies) {
    AggregatedDataGroupReportModel dataModel = new AggregatedDataGroupReportModel();
    dataModel.setGroupId(groupModel.getId());
    dataModel.setName(groupModel.getName());
    dataModel.setCode(groupModel.getCode());
    dataModel.setType(groupModel.getType());
    dataModel.setCompletedCompetencies(completedCompetencies);
    dataModel.setInferredCompetencies(inferredCompetencies);
    dataModel.setInprogressCompetencies(inprogressCompetencies);
    dataModel.setNotstartedCompetencies(notstartedCompetencies);
    return dataModel;
  }
  
  private DataModel prepareEmptyDataModel(GroupModel groupModel) {
    DataModel dataModel = new DataModel();
    dataModel.setId(groupModel.getId());
    dataModel.setCode(groupModel.getCode());
    dataModel.setName(groupModel.getName());
    dataModel.setType(groupModel.getType());
    dataModel.setCoordinates(new ArrayList<WeekDataModel>());
    dataModel.setDrilldown(new ArrayList<AggregatedDataGroupReportModel>());
    return dataModel;
  }
}
