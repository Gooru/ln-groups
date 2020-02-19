
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

  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_seq, ucm.status, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_display_code from domain_competency_matrix cm "
      + "left join learner_profile_competency_evidence_ts ucm on  "
      + "cm.tx_comp_code = ucm.gut_code and ucm.user_id = :userId where "
      + "and cast(ucm.created_at as date) BETWEEN :fromDate AND :toDate and ucm.class_id =:classId and ucm.status IN (1,2,4,5) order by cm.tx_domain_code, cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchCompetencyCompletionInClassInWeek(@Bind("classId") String classId,
      @Bind("userId") String userId, @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);
  
  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_seq, ucm.status, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_display_code from domain_competency_matrix cm "
      + "left join learner_profile_competency_evidence_ts ucm on  "
      + "cm.tx_comp_code = ucm.gut_code and ucm.user_id = :userId where "
      + "cast(ucm.created_at as date) <=:now and ucm.class_id =:classId and ucm.status IN (1,2,4,5) order by cm.tx_domain_code, cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchCompetencyCompletionInClassTillNow(@Bind("classId") String classId,
      @Bind("userId") String userId, @Bind("now") Date now);
  
  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_seq, ucm.status, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_display_code from domain_competency_matrix cm "
      + "left join learner_profile_competency_status ucm on  cm.tx_subject_code = ucm.tx_subject_code and "
      + "cm.tx_comp_code = ucm.gut_code and ucm.user_id = :userId where "
      + "cm.tx_subject_code = :subjectCode and lpcs.gut_code = ANY(:gutCode) order by cm.tx_domain_code, cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchCompetencyCompletionByCompCodes(@Bind("userId") String user,
      @Bind("subjectCode") String subjectCode, @Bind("gutCode") PGArray<String> gutCode);
  
  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select dcm.tx_domain_code, dcm.tx_comp_code, dcm.tx_comp_seq, lps.status, dcm.tx_comp_name, dcm.tx_comp_desc, dcm.tx_comp_student_desc, dcm.tx_comp_display_code from domain_competency_matrix dcm "
      + " INNER JOIN learner_profile_competency_status lps ON dcm.tx_comp_code=lps.gut_code where "
      + "lps.user_id = :userId and dcm.tx_subject_code = :subjectCode order by dcm.tx_domain_code, dcm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchUserSkyline(@Bind("userId") String user,
      @Bind("subjectCode") String subjectCode);
  
  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select dcm.tx_domain_code, dcm.tx_comp_code, dcm.tx_comp_seq, lps.status, dcm.tx_comp_name, dcm.tx_comp_desc, dcm.tx_comp_student_desc, dcm.tx_comp_display_code from domain_competency_matrix dcm "
      + " INNER JOIN learner_profile_competency_status lps ON dcm.tx_comp_code=lps.gut_code where "
      + "lps.user_id = :userId and dcm.tx_subject_code = :subjectCode and cast(created_at as date) BETWEEN :fromDate AND :toDate order by dcm.tx_domain_code, dcm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchUserSkylineInWeek(@Bind("userId") String user,
      @Bind("subjectCode") String subjectCode, @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

}
