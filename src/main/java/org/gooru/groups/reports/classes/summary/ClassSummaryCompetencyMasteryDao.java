
package org.gooru.groups.reports.classes.summary;

import java.util.Date;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface ClassSummaryCompetencyMasteryDao {

  @SqlQuery("select count(distinct gut_code) as mastered from learner_profile_competency_evidence_ts where (status = 4 or status = 5) and updated_at BETWEEN :fromDate AND :toDate and class_id =:classId")
  Integer fetchClassMasteredCountInWeek(@BindBean ClassSummaryBean bean);

  @SqlQuery("select count(distinct gut_code) as badges from learner_profile_competency_evidence_ts where status = 5 and updated_at BETWEEN :fromDate AND :toDate and class_id =:classId")
  Integer fetchClassEarnedBadgesCountInWeek(@BindBean ClassSummaryBean bean);

  @SqlQuery("select count(distinct gut_code) as mastered_till_now from learner_profile_competency_evidence_ts where (status = 4 or status = 5) and updated_at <= :toDate  and class_id =:classId")
  Integer fetchClassMasteredCountUntilNow(@Bind("classId") String classId,
      @Bind("toDate") Date toDate);
}
