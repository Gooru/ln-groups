
package org.gooru.groups.reports.country.perf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.reports.country.perf.GroupReportByCountryResponseModel.StateLevelPerf;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportByCountryModel;
import org.gooru.groups.reports.dbhelpers.StateModel;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupReportByCountryResponseModelBuilder {

  public static GroupReportByCountryResponseModel build(
      List<PerformanceAndTSReportByCountryModel> report, Map<Long, StateModel> states) {
    GroupReportByCountryResponseModel responseModel = new GroupReportByCountryResponseModel();
    List<StateLevelPerf> stateLevelPerfs = new ArrayList<>();
    report.forEach(perf -> {
      StateModel state = states.get(perf.getStateId());
      stateLevelPerfs.add(buildStateLevelPerfObject(perf, state));
    });

    responseModel.setStates(stateLevelPerfs);
    return responseModel;
  }

  private static StateLevelPerf buildStateLevelPerfObject(
      PerformanceAndTSReportByCountryModel perf, StateModel state) {
    StateLevelPerf stateLevelPerf = new StateLevelPerf();
    stateLevelPerf.setId(perf.getStateId());
    stateLevelPerf.setName(state.getName());
    stateLevelPerf.setCode(state.getCode());
    stateLevelPerf.setTimespent(perf.getTimespent());
    stateLevelPerf.setPerformance(perf.getPerformance());
    return stateLevelPerf;
  }
}
