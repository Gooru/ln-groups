
package org.gooru.groups.reports.perf.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportByGroupModel;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.reports.perf.state.GroupPerfReportByStateResponseModel.GroupResponseModel;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class GroupPerfReportByStateResponseModelBuilder {

  public static GroupPerfReportByStateResponseModel build(
      List<PerformanceAndTSReportByGroupModel> perfModels, Map<Long, GroupModel> groupModels) {
    List<GroupResponseModel> groupResponseModels = new ArrayList<>(perfModels.size());
    perfModels.forEach(perfModel -> {
      GroupModel groupModel = groupModels.get(perfModel.getGroupId());
      groupResponseModels.add(buildGroupModel(perfModel, groupModel));
    });

    GroupPerfReportByStateResponseModel responseModel = new GroupPerfReportByStateResponseModel();
    responseModel.setData(groupResponseModels);
    return responseModel;
  }

  private static GroupResponseModel buildGroupModel(PerformanceAndTSReportByGroupModel perfModel, GroupModel groupModel) {
    GroupResponseModel model = new GroupResponseModel();
    model.setId(perfModel.getGroupId());
    model.setName(groupModel.getName());
    model.setCode(groupModel.getCode());
    model.setType(groupModel.getType());
    model.setSubType(groupModel.getSubType());
    model.setTimespent(perfModel.getTimespent());
    model.setPerformance(perfModel.getPerformance());
    
    model.setInprogressCompetencies(0l);
    model.setCompletedCompetencies(0l);
    return model;
  }
}
