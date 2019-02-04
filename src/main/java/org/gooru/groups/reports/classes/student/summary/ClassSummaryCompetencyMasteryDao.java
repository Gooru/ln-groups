
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface ClassSummaryCompetencyMasteryDao {

  @SqlQuery("select distinct gut_code from learner_profile_competency_evidence_ts where (status = 4 or status = 5) and updated_at BETWEEN :fromDate AND :toDate and class_id =:classId and user_id =:user_id")
  List<String> fetchCompetenciesMasteredInWeek(@Bind("classId") String classId,
      @Bind("user_id") String user_id, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

  @SqlQuery("select count(distinct gut_code) as badges from learner_profile_competency_evidence_ts where status = 5 and updated_at BETWEEN :fromDate AND :toDate and class_id =:classId and user_id =:user_id")
  Integer fetchBadgesEarnedInWeek(@Bind("classId") String classId, @Bind("user_id") String user_id,
      @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

  @SqlQuery("select count(distinct gut_code) as mastered_till_now from learner_profile_competency_evidence_ts where (status = 4 or status = 5) and updated_at <= :toDate  and class_id =:classId and user_id =:user_id")
  Integer fetchCompetenciesMasteredUntilNow(@Bind("classId") String classId,
      @Bind("user_id") String user_id, @Bind("toDate") Date toDate);

}
