
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
  @SqlQuery("select gut_code, status from learner_profile_competency_status where status IN (1,2,4,5) and cast(created_at as date) BETWEEN :fromDate AND :toDate  order by gut_code, status asc")
  List<CompetencyStatus> fetchCompetenciesInWeek(@BindBean ClassSummaryBean bean);
  
  @Mapper(CompetencyStatusMapper.class)
  @SqlQuery("select gut_code, status  from learner_profile_competency_status where status IN (1,2,4,5) and cast(created_at as date) <= :now order by gut_code, status asc")
  List<CompetencyStatus> fetchCompetenciesTillNow(@Bind("now") Date now);
  
}
