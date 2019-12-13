
package org.gooru.groups.reports.perf.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportByClusterModel;
import org.gooru.groups.reports.dbhelpers.core.SchoolModel;
import org.gooru.groups.reports.perf.group.GroupPerfReportByClusterResponseModel.ClusterResponseModel;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupPerfReportByClusterResponseModelBuilder {

  public static GroupPerfReportByClusterResponseModel build(
      List<PerformanceAndTSReportByClusterModel> perfModels, Map<Long, SchoolModel> schoolModels) {
    List<ClusterResponseModel> clusterResponseModels = new ArrayList<>(perfModels.size());
    perfModels.forEach(perfModel -> {
      SchoolModel schoolModel = schoolModels.get(perfModel.getSchoolId());
      clusterResponseModels.add(buildSchoolModel(perfModel, schoolModel));
    });

    GroupPerfReportByClusterResponseModel responseModel = new GroupPerfReportByClusterResponseModel();
    responseModel.setData(clusterResponseModels);
    return responseModel;
  }

  private static ClusterResponseModel buildSchoolModel(PerformanceAndTSReportByClusterModel perfModel,
      SchoolModel schoolModel) {
    ClusterResponseModel model = new ClusterResponseModel();
    model.setId(perfModel.getSchoolId());
    model.setName(schoolModel.getName());
    model.setCode(schoolModel.getCode());
    model.setType("school");
    model.setSubType(null);
    model.setTimespent(perfModel.getTimespent());
    model.setPerformance(perfModel.getPerformance());
    return model;
  }
}
