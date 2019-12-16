
package org.gooru.groups.reports.perf.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gooru.groups.reports.dbhelpers.core.ClassModel;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportBySchoolModel;
import org.gooru.groups.reports.perf.school.GroupPerfReportBySchoolResponseModel.ClassPerformance;
import org.gooru.groups.reports.perf.school.GroupPerfReportBySchoolResponseModel.OverallStats;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupPerfReportBySchoolResponseModelBuilder {

  public static GroupPerfReportBySchoolResponseModel build(
      List<PerformanceAndTSReportBySchoolModel> perfModels, Map<String, ClassModel> classDetails) {

    GroupPerfReportBySchoolResponseModel response = new GroupPerfReportBySchoolResponseModel();
    List<ClassPerformance> classPerformances = new ArrayList<>();
    perfModels.forEach(clsPerformance -> {
      classPerformances.add(buildClassPerfResponseModel(clsPerformance,
          classDetails.get(clsPerformance.getClassId())));
    });
    
    OverallStats stats = new OverallStats();
    if (perfModels != null && !perfModels.isEmpty()) {
      Double totalPerformance =
          perfModels.stream().collect(Collectors.summingDouble(o -> o.getPerformance()));
      stats.setAveragePerformance(totalPerformance / perfModels.size());
    } else {
      stats.setAveragePerformance(0d);
    }
    
    response.setOverallStats(stats);  
    response.setData(classPerformances);
    return response;
  }

  private static ClassPerformance buildClassPerfResponseModel(
      PerformanceAndTSReportBySchoolModel perfModel, ClassModel classModel) {
    ClassPerformance cp = new ClassPerformance();
    cp.setId(perfModel.getClassId());
    cp.setName(classModel.getTitle());
    cp.setCode(classModel.getCode());
    cp.setCourseId(classModel.getCourseId());
    cp.setPerformance(perfModel.getPerformance());
    cp.setTimespent(perfModel.getTimespent());
    
    cp.setType("class");
    cp.setSubType(null);

    cp.setCompletedCompetencies(0l);
    cp.setInprogressCompetencies(0l);
    return cp;
  }
}