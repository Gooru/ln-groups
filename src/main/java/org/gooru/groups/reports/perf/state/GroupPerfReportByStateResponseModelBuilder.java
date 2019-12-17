
package org.gooru.groups.reports.perf.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByGroupModel;
import org.gooru.groups.reports.perf.state.GroupPerfReportByStateResponseModel.GroupResponseModel;
import org.gooru.groups.reports.perf.state.GroupPerfReportByStateResponseModel.OverallStats;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupPerfReportByStateResponseModelBuilder {

  public static GroupPerfReportByStateResponseModel build(
      List<PerformanceAndTSReportByGroupModel> perfModels, Map<Long, GroupModel> groupModels) {

    Map<Long, PerformanceAndTSReportByGroupModel> perfReportMap = new HashMap<>();
    perfModels.forEach(perfModel -> {
      perfReportMap.put(perfModel.getGroupId(), perfModel);
    });

    List<GroupResponseModel> groupResponseModels = new ArrayList<>();
    for (Map.Entry<Long, GroupModel> entry : groupModels.entrySet()) {
      groupResponseModels.add(buildGroupModel(perfReportMap.get(entry.getKey()), entry.getValue()));
    }

    GroupPerfReportByStateResponseModel responseModel = new GroupPerfReportByStateResponseModel();
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
