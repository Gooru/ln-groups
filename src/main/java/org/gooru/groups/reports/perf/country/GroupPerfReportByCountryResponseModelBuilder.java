
package org.gooru.groups.reports.perf.country;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gooru.groups.reports.dbhelpers.core.StateModel;
import org.gooru.groups.reports.perf.country.GroupPerfReportByCountryResponseModel.OverallStats;
import org.gooru.groups.reports.perf.country.GroupPerfReportByCountryResponseModel.StateLevelPerf;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByCountryModel;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupPerfReportByCountryResponseModelBuilder {

  public static GroupPerfReportByCountryResponseModel build(
      List<PerformanceAndTSReportByCountryModel> report, Map<Long, StateModel> states) {
    GroupPerfReportByCountryResponseModel responseModel = new GroupPerfReportByCountryResponseModel();
    List<StateLevelPerf> stateLevelPerfs = new ArrayList<>();
    report.forEach(perf -> {
      StateModel state = states.get(perf.getStateId());
      stateLevelPerfs.add(buildStateLevelPerfObject(perf, state));
    });
    
    OverallStats stats = new OverallStats();
    if (report != null && !report.isEmpty()) {
      Double totalPerformance =
          report.stream().collect(Collectors.summingDouble(o -> o.getPerformance()));
      stats.setAveragePerformance(totalPerformance / report.size());
    } else {
      stats.setAveragePerformance(0d);
    }
    
    responseModel.setOverallStats(stats);    
    responseModel.setData(stateLevelPerfs);
    return responseModel;
  }

  private static StateLevelPerf buildStateLevelPerfObject(
      PerformanceAndTSReportByCountryModel perf, StateModel state) {
    StateLevelPerf stateLevelPerf = new StateLevelPerf();
    stateLevelPerf.setId(perf.getStateId());
    stateLevelPerf.setName(state.getName());
    stateLevelPerf.setCode(state.getCode());
    stateLevelPerf.setType("state");
    stateLevelPerf.setSubType(null);
    stateLevelPerf.setTimespent(perf.getTimespent());
    stateLevelPerf.setPerformance(perf.getPerformance());
    stateLevelPerf.setCompletedCompetencies(0l);
    stateLevelPerf.setInprogressCompetencies(0l);
    return stateLevelPerf;
  }
}