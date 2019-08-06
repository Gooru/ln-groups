
package org.gooru.groups.reports.school.perf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportBySchoolModel;
import org.gooru.groups.reports.dbhelpers.core.ClassTitleModel;
import org.gooru.groups.reports.school.perf.GroupReportBySchoolResponseModel.ClassPerformance;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupReportBySchoolResponseModelBuilder {

  public static GroupReportBySchoolResponseModel build(
      List<PerformanceAndTSReportBySchoolModel> perfModels, List<ClassTitleModel> classTitles) {

    Map<String, ClassTitleModel> classTitleModelMap = new HashMap<>();
    classTitles.forEach(cls -> {
      classTitleModelMap.put(cls.getId(), cls);
    });

    GroupReportBySchoolResponseModel response = new GroupReportBySchoolResponseModel();
    List<ClassPerformance> classPerformances = new ArrayList<>();
    perfModels.forEach(clsPerformance -> {
      classPerformances.add(buildClassPerfResponseModel(clsPerformance,
          classTitleModelMap.get(clsPerformance.getClassId())));
    });

    response.setClasses(classPerformances);
    return response;
  }

  private static ClassPerformance buildClassPerfResponseModel(
      PerformanceAndTSReportBySchoolModel perfModel, ClassTitleModel cls) {
    ClassPerformance cp = new ClassPerformance();
    cp.setId(perfModel.getClassId());
    cp.setName(cls.getTitle());
    cp.setPerformance(perfModel.getPerformance());
    cp.setTimespent(perfModel.getTimespent());
    return cp;
  }
}
