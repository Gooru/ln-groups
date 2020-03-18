
package org.gooru.groups.reports.perf.country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gooru.groups.reports.dbhelpers.core.DrilldownModel;
import org.gooru.groups.reports.perf.country.GroupPerfReportByCountryResponseModel.OverallStats;
import org.gooru.groups.reports.perf.country.GroupPerfReportByCountryResponseModel.StateLevelPerf;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByCountryModel;

/**
 * @author szgooru Created On 19-Mar-2019
 */
public class GroupPerfReportByCountryResponseModelBuilder {

  public static GroupPerfReportByCountryResponseModel build(
      List<PerformanceAndTSReportByCountryModel> report, Map<Long, DrilldownModel> states) {
    GroupPerfReportByCountryResponseModel responseModel =
        new GroupPerfReportByCountryResponseModel();

    Map<Long, PerformanceAndTSReportByCountryModel> perfReportMap = new HashMap<>();
    report.forEach(perf -> {
      perfReportMap.put(perf.getStateId(), perf);
    });

    List<StateLevelPerf> stateLevelPerfs = new ArrayList<>();
    for (Map.Entry<Long, DrilldownModel> entry : states.entrySet()) {
      stateLevelPerfs
          .add(buildStateLevelPerfObject(perfReportMap.get(entry.getKey()), entry.getValue()));
    }

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

  private static StateLevelPerf buildStateLevelPerfObject(PerformanceAndTSReportByCountryModel perf,
      DrilldownModel state) {
    StateLevelPerf stateLevelPerf = new StateLevelPerf();
    stateLevelPerf.setId(state.getId());
    stateLevelPerf.setName(state.getName());
    stateLevelPerf.setCode(state.getCode());
    stateLevelPerf.setType("state");
    stateLevelPerf.setSubType(null);
    if (perf != null) {
      stateLevelPerf.setTimespent(perf.getTimespent());
      stateLevelPerf.setPerformance(perf.getPerformance());
    } else {
      stateLevelPerf.setTimespent(0l);
      stateLevelPerf.setPerformance(0d);
    }
    stateLevelPerf.setCompletedCompetencies(0l);
    stateLevelPerf.setInprogressCompetencies(0l);
    return stateLevelPerf;
  }
}
