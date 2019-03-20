
package org.gooru.groups.reports.country.perf;

import java.util.ArrayList;
import java.util.List;
import org.gooru.groups.reports.country.perf.GroupReportByCountryResponseModel.StateLevelPerf;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportByCountryModel;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupReportByCountryResponseModelBuilder {

  public static GroupReportByCountryResponseModel build(
      List<PerformanceAndTSReportByCountryModel> report) {
    GroupReportByCountryResponseModel responseModel = new GroupReportByCountryResponseModel();
    List<StateLevelPerf> stateLevelPerfs = new ArrayList<>();
    report.forEach(perf -> {
      stateLevelPerfs.add(buildStateLevelPerfObject(perf));
    });

    responseModel.setStates(stateLevelPerfs);
    return responseModel;
  }

  private static StateLevelPerf buildStateLevelPerfObject(
      PerformanceAndTSReportByCountryModel perf) {
    StateLevelPerf stateLevelPerf = new StateLevelPerf();
    stateLevelPerf.setId(perf.getStateId());
    stateLevelPerf.setName(perf.getName());
    stateLevelPerf.setCode(perf.getCode());
    stateLevelPerf.setTimespent(perf.getTimespent());
    stateLevelPerf.setPerformance(perf.getPerformance());
    return stateLevelPerf;
  }
}
