
package org.gooru.groups.reports.perf.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByGroupModel;
import org.gooru.groups.reports.perf.group.GroupPerfReportByGroupResponseModel.GroupResponseModel;
import org.gooru.groups.reports.perf.group.GroupPerfReportByGroupResponseModel.OverallStats;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupPerfReportByGroupResponseModelBuilder {

  public static GroupPerfReportByGroupResponseModel build(
      List<PerformanceAndTSReportByGroupModel> perfModels, Map<Long, GroupModel> groupModels) {

    Map<Long, PerformanceAndTSReportByGroupModel> perfReportMap = new HashMap<>();
    perfModels.forEach(perfModel -> {
      perfReportMap.put(perfModel.getGroupId(), perfModel);
    });

    List<GroupResponseModel> groupResponseModels = new ArrayList<>();
    for (Map.Entry<Long, GroupModel> entry : groupModels.entrySet()) {
      groupResponseModels.add(buildGroupModel(perfReportMap.get(entry.getKey()), entry.getValue()));
    }

    GroupPerfReportByGroupResponseModel responseModel = new GroupPerfReportByGroupResponseModel();
    OverallStats stats = new OverallStats();
    if (perfModels != null && !perfModels.isEmpty()) {
      Double totalPerformance =
          perfModels.stream().collect(Collectors.summingDouble(o -> o.getPerformance()));
      stats.setAveragePerformance(totalPerformance / perfModels.size());
    } else {
      stats.setAveragePerformance(0d);
    }

    responseModel.setOverallStats(stats);
    responseModel.setData(groupResponseModels);
    return responseModel;
  }

  private static GroupResponseModel buildGroupModel(PerformanceAndTSReportByGroupModel perfModel,
      GroupModel groupModel) {
    GroupResponseModel model = new GroupResponseModel();
    model.setId(groupModel.getId());
    model.setName(groupModel.getName());
    model.setCode(groupModel.getCode());
    model.setType(groupModel.getType());
    model.setSubType(groupModel.getSubType());
    if (perfModel != null) {
      model.setTimespent(perfModel.getTimespent());
      model.setPerformance(perfModel.getPerformance());
    } else {
      model.setTimespent(0l);
      model.setPerformance(0d);
    }
    model.setInprogressCompetencies(0l);
    model.setCompletedCompetencies(0l);
    return model;
  }
}
