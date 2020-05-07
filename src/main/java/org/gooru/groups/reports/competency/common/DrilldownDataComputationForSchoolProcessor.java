package org.gooru.groups.reports.competency.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.app.jdbi.PGArray;
import org.gooru.groups.reports.dbhelpers.core.ClassModel;
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
public class DrilldownDataComputationForSchoolProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(DrilldownDataComputationForSchoolProcessor.class);
  
  private final GroupCompetencyReportService REPORT_SERVICE =
      new GroupCompetencyReportService(DBICreator.getDbiForDsdbDS());

  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());

  public DataModelForClass processDrilldownComputationForClasses(Long groupId, String groupType, Integer month, Integer year, PGArray<String> tenants,
      GroupACLResolver aclResolver, Node<GroupHierarchyDetailsModel> groupHierarchy) {
    
    // Fetch the group details from the actual group definition table
    Set<Long> groupIds = new HashSet<>();
    groupIds.add(groupId);
    Map<Long, GroupModel> groupModels = this.CORE_SERVICE.fetchFlexibleGroupDetails(groupIds);
    
    // classesByGroups
    Map<Long, Set<String>> classesByGroups = aclResolver.getClassesByGroupACL(groupHierarchy);

    Set<String> classes = classesByGroups.get(groupId);
    if (classes == null || classes.isEmpty()) {
      LOGGER.debug("seems that user for group {} of type {} does not have any class access",
          groupId, groupType);
      return prepareEmptyDataModel(groupModels.get(groupId));
    }
    
    Map<String, ClassModel> classModels = this.CORE_SERVICE.fetchClassesByGroup(groupId);
    
    // Fetch the competency report for all the classes we resolved above using the user acl. This
    // will return the data for all the classes without grouping them by the specific parent in
    // which the classes fall under.
    Map<String, List<GroupCompetencyReportModel>> classReport =
        this.REPORT_SERVICE.fetchCompetencyReportByMonthYear(classes, tenants, month, year);
    
    Map<Integer, WeekDataModel> weeklyDataModels = prepareWeeeklyDataReport(classReport);

    Map<String, AggregatedDataGroupReportForClassModel> drilldownDataModels = new HashMap<>();
    for (String classId : classes) {
      Long completedCompetencies = 0l;
      Long inferredCompetencies = 0l;
      Long inprogressCompetencies = 0l;
      Long notstartedCompetencies = 0l;
      
      List<GroupCompetencyReportModel> classDataModels = classReport.get(classId);

      for (GroupCompetencyReportModel classDataModel : classDataModels) {
        completedCompetencies = completedCompetencies + classDataModel.getCompletedCompetencies();
        inferredCompetencies = inferredCompetencies + classDataModel.getInferredCompetencies();
        inprogressCompetencies =
            inprogressCompetencies + classDataModel.getInprogressCompetencies();
        notstartedCompetencies =
            notstartedCompetencies + classDataModel.getNotstartedCompetencies();
      }

      ClassModel classModel = classModels.get(classId);

      drilldownDataModels.put(classId,
          prepareAggregatedDataReportModelForClass(classModel, completedCompetencies, inferredCompetencies,
              inprogressCompetencies, notstartedCompetencies));
    }

    return prepareDataModel(groupId, weeklyDataModels, drilldownDataModels, groupModels.get(groupId));
  }
  
  private static OverallStatsModel prepareOverallStatsModel(
      Map<String, AggregatedDataGroupReportForClassModel> drilldownDataModels) {

    if (drilldownDataModels == null || drilldownDataModels.isEmpty()) {
      return new OverallStatsModel();
    }

    // Overall stats
    Long totalCompletedCompetencies = 0l;
    Long totalInferredCompetencies = 0l;
    Long totalInprogressCompetencies = 0l;
    Long totalNotstartedCompetencies = 0l;

    for (String id : drilldownDataModels.keySet()) {
      AggregatedDataGroupReportForClassModel aggregatedDataModel = drilldownDataModels.get(id);
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
  
  private DataModelForClass prepareDataModel(Long groupId, Map<Integer, WeekDataModel> weeklyDataModels,
      Map<String, AggregatedDataGroupReportForClassModel> drilldownDataModels,
      GroupModel groupModel) {
    DataModelForClass dataModel = new DataModelForClass();
    if (groupModel != null) {
      dataModel.setId(groupId);
      dataModel.setCode(groupModel.getCode());
      dataModel.setName(groupModel.getName());
      dataModel.setType(groupModel.getType());
    }
    dataModel.setOverallStats(prepareOverallStatsModel(drilldownDataModels));
    dataModel.setCoordinates(new ArrayList<WeekDataModel>(weeklyDataModels.values()));
    dataModel
        .setDrilldown(new ArrayList<AggregatedDataGroupReportForClassModel>(drilldownDataModels.values()));
    return dataModel;
  }
  
  private AggregatedDataGroupReportForClassModel prepareAggregatedDataReportModelForClass(ClassModel classModel,
      Long completedCompetencies, Long inferredCompetencies, Long inprogressCompetencies,
      Long notstartedCompetencies) {
    AggregatedDataGroupReportForClassModel dataModel = new AggregatedDataGroupReportForClassModel();
    dataModel.setGroupId(classModel.getId());
    dataModel.setName(classModel.getTitle());
    dataModel.setCode(classModel.getCode());
    dataModel.setType("class");
    dataModel.setCompletedCompetencies(completedCompetencies);
    dataModel.setInferredCompetencies(inferredCompetencies);
    dataModel.setInprogressCompetencies(inprogressCompetencies);
    dataModel.setNotstartedCompetencies(notstartedCompetencies);
    return dataModel;
  }

  private DataModelForClass prepareEmptyDataModel(GroupModel groupModel) {
    DataModelForClass dataModel = new DataModelForClass();
    dataModel.setId(groupModel.getId());
    dataModel.setCode(groupModel.getCode());
    dataModel.setName(groupModel.getName());
    dataModel.setType(groupModel.getType());
    dataModel.setCoordinates(new ArrayList<WeekDataModel>());
    dataModel.setDrilldown(new ArrayList<AggregatedDataGroupReportForClassModel>());
    return dataModel;
  }
}
