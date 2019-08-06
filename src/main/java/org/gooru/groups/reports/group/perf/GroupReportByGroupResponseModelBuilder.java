
package org.gooru.groups.reports.group.perf;

import java.util.ArrayList;
import java.util.List;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportByGroupModel;
import org.gooru.groups.reports.group.perf.GroupReportByGroupResponseModel.GroupResponseModel;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupReportByGroupResponseModelBuilder {

  public static GroupReportByGroupResponseModel build(
      List<PerformanceAndTSReportByGroupModel> perfModels) {
    List<GroupResponseModel> groupResponseModels = new ArrayList<>(perfModels.size());
    perfModels.forEach(perfModel -> {
      groupResponseModels.add(buildGroupModel(perfModel));
    });

    GroupReportByGroupResponseModel responseModel = new GroupReportByGroupResponseModel();
    responseModel.setGroups(groupResponseModels);
    return responseModel;
  }

  private static GroupResponseModel buildGroupModel(PerformanceAndTSReportByGroupModel perfModel) {
    GroupResponseModel model = new GroupResponseModel();
    model.setId(perfModel.getGroupId());
    model.setName(perfModel.getName());
    model.setCode(perfModel.getCode());
    model.setSubType(perfModel.getSubType());
    model.setTimespent(perfModel.getTimespent());
    model.setPerformance(perfModel.getPerformance());
    return model;
  }
}
