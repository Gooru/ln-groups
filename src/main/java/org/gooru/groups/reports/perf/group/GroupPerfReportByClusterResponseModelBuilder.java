
package org.gooru.groups.reports.perf.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gooru.groups.reports.dbhelpers.core.SchoolModel;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByClusterModel;
import org.gooru.groups.reports.perf.group.GroupPerfReportByClusterResponseModel.ClusterResponseModel;
import org.gooru.groups.reports.perf.group.GroupPerfReportByClusterResponseModel.OverallClusterStats;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupPerfReportByClusterResponseModelBuilder {

  public static GroupPerfReportByClusterResponseModel build(
      List<PerformanceAndTSReportByClusterModel> perfModels, Map<Long, SchoolModel> schoolModels) {

    Map<Long, PerformanceAndTSReportByClusterModel> perfReportMap = new HashMap<>();
    perfModels.forEach(model -> {
      perfReportMap.put(model.getSchoolId(), model);
    });

    List<ClusterResponseModel> clusterResponseModels = new ArrayList<>();
    for (Map.Entry<Long, SchoolModel> entry : schoolModels.entrySet()) {
      clusterResponseModels
          .add(buildSchoolModel(perfReportMap.get(entry.getKey()), entry.getValue()));
    }

    GroupPerfReportByClusterResponseModel responseModel =
        new GroupPerfReportByClusterResponseModel();
    OverallClusterStats stats = new OverallClusterStats();
    if (perfModels != null && !perfModels.isEmpty()) {
      Double totalPerformance =
          perfModels.stream().collect(Collectors.summingDouble(o -> o.getPerformance()));
      stats.setAveragePerformance(totalPerformance / perfModels.size());
    } else {
      stats.setAveragePerformance(0d);
    }

    responseModel.setOverallStats(stats);
    responseModel.setData(clusterResponseModels);
    return responseModel;
  }

  private static ClusterResponseModel buildSchoolModel(
      PerformanceAndTSReportByClusterModel perfModel, SchoolModel schoolModel) {
    ClusterResponseModel model = new ClusterResponseModel();
    model.setId(schoolModel.getId());
    model.setName(schoolModel.getName());
    model.setCode(schoolModel.getCode());
    model.setType("school");
    model.setSubType(null);
    if (perfModel != null) {
      model.setTimespent(perfModel.getTimespent());
      model.setPerformance(perfModel.getPerformance());
    } else {
      model.setTimespent(0l);
      model.setPerformance(0d);
    }
    return model;
  }
}
