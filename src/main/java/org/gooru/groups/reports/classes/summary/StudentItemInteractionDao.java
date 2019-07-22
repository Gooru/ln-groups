
package org.gooru.groups.reports.classes.summary;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface StudentItemInteractionDao {

  @Mapper(StudentItemInteractionMapper.class)
  @SqlQuery("select SUM(case when (collection_type IN ('assessment','assessment-external') and status = true) then timespent when collection_type IN ('collection', 'collection-external') then timespent end) as timespent, SUM(case when (collection_type IN ('assessment','assessment-external') and status = true) then 1 when collection_type IN ('collection', 'collection-external') then 1 ELSE 0 end) AS interaction_count FROM collection_performance WHERE course_id is NOT NULL AND (path_id IS NULL OR path_id = 0) AND cast(created_at as date) BETWEEN :fromDate AND :toDate and class_id =:classId")
  StudentItemInteraction fetchContentInteractionStatsInWeek(@BindBean ClassSummaryBean bean);

}
