
package org.gooru.groups.reports.classes.summary;

import org.gooru.groups.reports.dbhelpers.core.ClassModel;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.UserModel;
import org.skife.jdbi.v2.DBI;
import io.vertx.core.json.JsonObject;

/**
 * @author renuka
 */
public class ClassSummaryService {

  private final ClassSummaryCompetencyMasteryDao classSummaryMasterydao;
  private final ClassTimespentSummaryDao classTimespentSummaryDao;
  private final CoreService coreService;

  public ClassSummaryService(DBI coreDbi, DBI dsDbi, DBI analyticsDbi) {
    this.coreService = new CoreService(coreDbi);
    this.classSummaryMasterydao = dsDbi.onDemand(ClassSummaryCompetencyMasteryDao.class);
    this.classTimespentSummaryDao = analyticsDbi.onDemand(ClassTimespentSummaryDao.class);
  }

  public JsonObject fetchClassSummary(ClassSummaryBean bean) {
    Integer masteredCountInWeek = this.classSummaryMasterydao.fetchClassMasteredCountInWeek(bean);
    Integer badgesCountInWeek = this.classSummaryMasterydao.fetchClassEarnedBadgesCountInWeek(bean);
    Integer masteredCountUntilNow = this.classSummaryMasterydao
        .fetchClassMasteredCountUntilNow(bean.getClassId(), bean.getToDate());
    Long totalTimespentInWeek = this.classTimespentSummaryDao.fetchClassTimespentInWeek(bean);
    ClassModel classData = this.coreService.fetchClass(bean.getClassId());
    UserModel userData = this.coreService.fetchUser(classData.getCreatorId());
    JsonObject teacher = new JsonObject();
    teacher.put("id", classData.getCreatorId()).put("firstName", userData.getFirstName())
        .put("lastName", userData.getLastName());

    return new JsonObject().put("id", bean.getClassId()).put("title", classData.getTitle())
        .put("teacher", teacher).put("competenciesMasteredInWeek", masteredCountInWeek)
        .put("badgesEarnedInWeek", badgesCountInWeek)
        .put("totalCompetenciesMastered", masteredCountUntilNow)
        .put("totalTimespentInWeek", totalTimespentInWeek);
  }
}
