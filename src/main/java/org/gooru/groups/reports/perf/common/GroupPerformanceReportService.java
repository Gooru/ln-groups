package org.gooru.groups.reports.perf.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceReportService {

  private final GroupPerformanceReportDao dao;

  public GroupPerformanceReportService(DBI dbi) {
    this.dao = dbi.onDemand(GroupPerformanceReportDao.class);
  }

  public Map<String, List<GroupPerformanceReportModel>> fetchWeeklyClassPerformance(
      Set<String> classes, Set<String> tenants, Integer month, Integer year) {
    List<GroupPerformanceReportModel> report =
        this.dao.fetchWeeklyClassPerformance(CollectionUtils.convertToSqlArrayOfString(classes),
            CollectionUtils.convertToSqlArrayOfString(tenants), month, year);

    Map<String, List<GroupPerformanceReportModel>> classReportMap = new HashMap<>();
    report.forEach(model -> {
      String classId = model.getClassId();
      if (classReportMap.containsKey(classId)) {
        classReportMap.get(classId).add(model);
      } else {
        List<GroupPerformanceReportModel> reportModels = new ArrayList<>();
        reportModels.add(model);
        classReportMap.put(classId, reportModels);
      }
    });

    return classReportMap;
  }
}
