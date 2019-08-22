
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;
import java.util.List;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface ClassSummaryCompetencyMasteryDao {

  @SqlQuery("select DISTINCT gut_code from learner_profile_competency_evidence_ts WHERE status IN (1,2,4,5) and cast(created_at as date) <=:now and class_id =:classId and user_id =:userId order by gut_code")
  List<String> fetchCompetenciesTillNow(@Bind("classId") String classId,
      @Bind("userId") String userId, @Bind("now") Date now);

  @SqlQuery("select DISTINCT gut_code from learner_profile_competency_evidence_ts WHERE status IN (1,2,4,5) and cast(created_at as date) BETWEEN :fromDate AND :toDate and class_id =:classId and user_id =:userId order by gut_code")
  List<String> fetchCompetenciesInAPeriod(@Bind("classId") String classId,
      @Bind("userId") String userId, @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_seq, cm.tx_comp_display_code, lpcs.status from domain_competency_matrix cm left join learner_profile_competency_status lpcs on  cm.tx_comp_code = lpcs.gut_code WHERE status IN (1,2,4,5) and lpcs.gut_code = ANY(:gutCode) and user_id =:userId order by cm.tx_comp_code,lpcs.status,cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchCompetenciesStatus(@Bind("userId") String userId, @Bind("gutCode") PGArray<String> gutCode);
  
}
