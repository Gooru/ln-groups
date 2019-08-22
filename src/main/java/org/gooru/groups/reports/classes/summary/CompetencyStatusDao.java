
package org.gooru.groups.reports.classes.summary;

import java.util.Date;
import java.util.List;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface CompetencyStatusDao {

  @SqlQuery("select DISTINCT gut_code from learner_profile_competency_evidence_ts WHERE status IN (1,2,4,5) and cast(created_at as date) <=:now and class_id =:classId order by gut_code")
  List<String> fetchCompetenciesTillNow(@Bind("classId") String classId, @Bind("now") Date now);

  @SqlQuery("select DISTINCT gut_code from learner_profile_competency_evidence_ts WHERE status IN (1,2,4,5) and cast(created_at as date) BETWEEN :fromDate AND :toDate and class_id =:classId order by gut_code")
  List<String> fetchCompetenciesInWeek(@BindBean ClassSummaryBean bean);

  @Mapper(CompetencyStatusMapper.class)
  @SqlQuery("select gut_code, status from learner_profile_competency_status WHERE status IN (1,2,4,5) and gut_code = ANY(:gutCode) order by gut_code, status")
  List<CompetencyStatus> fetchCompetenciesStatus(@Bind("gutCode") PGArray<String> gutCode);

}
