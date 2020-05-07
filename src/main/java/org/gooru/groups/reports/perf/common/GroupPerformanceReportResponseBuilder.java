package org.gooru.groups.reports.perf.common;

import java.util.List;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceReportResponseBuilder {

  public static GroupPerformanceReportResponseModel build(ContextModel contextModel,
      List<DataModel> dataModels) {
    GroupPerformanceReportResponseModel responseModel = new GroupPerformanceReportResponseModel();
    responseModel.setContext(contextModel);
    responseModel.setData(dataModels);
    return responseModel;
  }

  public static GroupPerformanceReportResponseForClassModel buildForClass(ContextModel contextModel,
      List<DataModelForClass> dataModels) {
    GroupPerformanceReportResponseForClassModel responseModel =
        new GroupPerformanceReportResponseForClassModel();
    responseModel.setContext(contextModel);
    responseModel.setData(dataModels);
    return responseModel;
  }
}
