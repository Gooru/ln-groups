package org.gooru.groups.reports.competency.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.jdbi.PGArray;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 17-Feb-2020
 *
 */
public class GroupReportService {

  private final GroupReportDao dao;

  public GroupReportService(DBI dbi) {
    this.dao = dbi.onDemand(GroupReportDao.class);
  }

  /*
   * This method return the weekly class competency report by month and year. It contains the data
   * for the classes for which it is requested for. Aggregation based on the groups and its classes
   * should be done by the caller of this method.
   */
  public Map<String, List<GroupReportModel>> fetchCompetencyReportByMonthYear(Set<String> classes,
      PGArray<String> tenants, Integer month, Integer year) {
    List<GroupReportModel> report = this.dao.fetchCompetencyReportByMonthYear(
        CollectionUtils.convertToSqlArrayOfString(classes), tenants, month, year);

    Map<String, List<GroupReportModel>> classReportMap = new HashMap<>();
    report.forEach(model -> {
      String classId = model.getClassId();
      if (classReportMap.containsKey(classId)) {
        classReportMap.get(classId).add(model);
      } else {
        List<GroupReportModel> reportModels = new ArrayList<>();
        reportModels.add(model);
        classReportMap.put(classId, reportModels);
      }
    });

    return classReportMap;
  }
}
