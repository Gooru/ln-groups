
package org.gooru.groups.reports.classes.student.summary;

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
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_seq, ucm.status, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_display_code from domain_competency_matrix cm "
      + "left join learner_profile_competency_evidence_ts ucm on cm.tx_comp_code = ucm.gut_code and ucm.user_id = :userId where "
      + "cast(ucm.updated_at as date) BETWEEN :fromDate AND :toDate and ucm.class_id =:classId and ucm.status IN (1,2,4,5) "
      + "and cm.tx_subject_code = :subjectCode order by cm.tx_domain_code, cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchCompetencyCompletionInClassInWeek(
      @Bind("classId") String classId, @Bind("subjectCode") String subjectCode,
      @Bind("userId") String userId, @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select cm.tx_domain_code, cm.tx_comp_code, cm.tx_comp_seq, ucm.status, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_display_code "
      + "from domain_competency_matrix cm left join learner_profile_competency_evidence_ts ucm on cm.tx_comp_code = ucm.gut_code "
      + "and ucm.user_id = :userId where cast(ucm.updated_at as date) <=:now and ucm.class_id =:classId and ucm.status IN (1,2,4,5) "
      + "and cm.tx_subject_code = :subjectCode order by cm.tx_domain_code, cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchCompetencyCompletionInClassTillNow(
      @Bind("classId") String classId, @Bind("subjectCode") String subjectCode,
      @Bind("userId") String userId, @Bind("now") Date now);

  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select dcm.tx_domain_code, dcm.tx_comp_code, dcm.tx_comp_seq, lps.status, dcm.tx_comp_name, dcm.tx_comp_desc, dcm.tx_comp_student_desc, dcm.tx_comp_display_code from domain_competency_matrix dcm "
      + " LEFT JOIN learner_profile_competency_status lps ON dcm.tx_subject_code = lps.tx_subject_code and dcm.tx_comp_code=lps.gut_code and "
      + "lps.user_id = :userId where dcm.tx_subject_code = :subjectCode order by dcm.tx_domain_code, dcm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchUserSkyline(@Bind("userId") String user,
      @Bind("subjectCode") String subjectCode);

  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select dcm.tx_domain_code, dcm.tx_comp_code, dcm.tx_comp_seq, lps.status, dcm.tx_comp_name, dcm.tx_comp_desc, dcm.tx_comp_student_desc, dcm.tx_comp_display_code from domain_competency_matrix dcm "
      + " INNER JOIN learner_profile_competency_status lps ON dcm.tx_comp_code=lps.gut_code and "
      + "lps.user_id = :userId where dcm.tx_subject_code = :subjectCode and cast(updated_at as date) BETWEEN :fromDate AND :toDate order by dcm.tx_domain_code, dcm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchUserSkylineInWeek(@Bind("userId") String user,
      @Bind("subjectCode") String subjectCode, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

  @Mapper(CompetencyStatusModelMapper.class)
  @SqlQuery("select distinct(cm.tx_comp_code) as tx_comp_code, cm.tx_domain_code, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_seq, cm.tx_comp_display_code, "
      + " (SELECT DISTINCT ON (lpcs.gut_code) FIRST_VALUE(lpcs.status) OVER (PARTITION BY lpcs.gut_code ORDER BY lpcs.updated_at desc) "
      + " FROM learner_profile_competency_status_ts lpcs where lpcs.user_id = :userId and lpcs.gut_code = ucm.gut_code and cast(lpcs.updated_at as date) <= :toDate)"
      + " as status from domain_competency_matrix cm left join learner_profile_competency_status_ts"
      + " ucm on cm.tx_subject_code = ucm.tx_subject_code and cm.tx_comp_code = ucm.gut_code and ucm.user_id = :userId and "
      + " cast(ucm.updated_at as date) <= :toDate where cm.tx_subject_code = :subjectCode order by cm.tx_domain_code,"
      + " cm.tx_comp_seq asc")
  List<CompetencyStatusModel> fetchUserSkylineTillToDate(@Bind("userId") String userId,
      @Bind("subjectCode") String subjectCode, @Bind("toDate") Date now);

  @SqlQuery("select distinct(cm.tx_comp_code) as tx_comp_code from domain_competency_matrix cm left join learner_profile_competency_status_ts"
      + " ucm on cm.tx_subject_code = ucm.tx_subject_code and cm.tx_comp_code = ucm.gut_code and ucm.user_id = :userId where "
      + " cast(ucm.updated_at as date) between :fromDate and :toDate and cm.tx_subject_code = :subjectCode")
  List<String> fetchUserSkylineInGivenPeriod(@Bind("userId") String userId,
      @Bind("subjectCode") String subjectCode, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date now);

}
