package org.gooru.groups.reports.competency.common;

import java.util.List;

/**
 * @author szgooru on 24-Apr-2020
 *
 */
public class GroupCompetencyReportResponseBuilder {

  public static GroupCompetencyReportResponseModel build(ContextModel contextModel,
      List<DataModel> dataModels) {
    GroupCompetencyReportResponseModel responseModel = new GroupCompetencyReportResponseModel();
    responseModel.setContext(contextModel);
    responseModel.setData(dataModels);
    return responseModel;
  }

  public static GroupCompetencyReportResponseForClassModel buildForClasses(
      ContextModel contextModel, List<DataModelForClass> dataModels) {
    GroupCompetencyReportResponseForClassModel responseModel =
        new GroupCompetencyReportResponseForClassModel();
    responseModel.setContext(contextModel);
    responseModel.setData(dataModels);
    return responseModel;
  }
}
