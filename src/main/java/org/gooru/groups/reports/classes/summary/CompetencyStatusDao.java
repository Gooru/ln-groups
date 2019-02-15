
package org.gooru.groups.reports.classes.summary;

import java.util.Date;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface CompetencyStatusDao {

  @Mapper(CompetencyStatusMapper.class)
  @SqlQuery("select distinct on (gut_code, status) gut_code, status from learner_profile_competency_evidence_ts where status IN (1,2,4,5) and created_at BETWEEN :fromDate AND :toDate and class_id =:classId  order by gut_code, status asc")
  List<CompetencyStatus> fetchCompetenciesInWeek(@BindBean ClassSummaryBean bean);
  
  @Mapper(CompetencyStatusMapper.class)
  @SqlQuery("select distinct on (gut_code, status) gut_code, status  from learner_profile_competency_evidence_ts where status IN (1,2,4,5) and created_at <= :now and class_id =:classId  order by gut_code, status asc")
  List<CompetencyStatus> fetchCompetenciesTillNow(@Bind("classId") String classId, @Bind("now") Date now);
  
}
