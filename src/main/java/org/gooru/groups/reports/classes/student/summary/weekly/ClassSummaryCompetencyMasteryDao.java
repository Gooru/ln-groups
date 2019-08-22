
package org.gooru.groups.reports.classes.student.summary.weekly;

import java.util.Date;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface ClassSummaryCompetencyMasteryDao {

  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_seq, cm.tx_comp_display_code, lpcs.status from domain_competency_matrix cm left join learner_profile_competency_status lpcs on  cm.tx_comp_code = lpcs.gut_code WHERE status IN (1,2,4,5) and cast(created_at as date) <= :now and user_id =:userId order by cm.tx_comp_code,lpcs.status,cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchCompetenciesTillNow(
      @Bind("userId") String userId, @Bind("now") Date now);
  
  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_seq, cm.tx_comp_display_code, lpcs.status from domain_competency_matrix cm left join learner_profile_competency_status lpcs on  cm.tx_comp_code = lpcs.gut_code WHERE status IN (1,2,4,5) and cast(created_at as date) BETWEEN :fromDate AND :toDate and user_id =:userId order by cm.tx_comp_code,lpcs.status,cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchCompetenciesInWeek(@Bind("userId") String user_id,
      @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

}
