
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface StudentItemInteractionDao {

  @Mapper(StudentItemInteractionMapper.class)
  @SqlQuery("select collection_type, count(*) as interaction_count, SUM(timespent) as timespent, ROUND(AVG(score)) as score, SUM(max_score) as max_score from collection_performance WHERE unit_id is NOT NULL AND (path_id IS NULL OR path_id = 0) AND created_at BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId group by collection_type")
  List<StudentItemInteraction> fetchStudentContentInteractionInWeek(@Bind("classId") String classId,
      @Bind("actorId") String actorId, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

  @Mapper(StudentItemInteractionMapper.class)
  @SqlQuery("select collection_type, count(*) as interaction_count, SUM(timespent) as timespent, ROUND(AVG(score)) as score, SUM(max_score) as max_score from collection_performance WHERE unit_id is NOT NULL AND (path_id IS NOT NULL AND path_id <> 0) AND created_at BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId group by collection_type")
  List<StudentItemInteraction> fetchStudentInteractionOnSuggestedItemInWeek(
      @Bind("classId") String classId, @Bind("actorId") String actorId,
      @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

}
