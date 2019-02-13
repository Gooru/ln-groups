
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
  @SqlQuery("select collection_type, count(distinct collection_id) as unique_item_count, count(*) as interaction_count, SUM(timespent) as timespent, ROUND(AVG(score)) as score, SUM(max_score) as max_score from collection_performance WHERE course_id is NOT NULL AND (path_id IS NULL OR path_id = 0) AND collection_type IN ('assessment', 'assessment-external') AND status = true AND created_at BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId group by collection_type")
  List<StudentItemInteraction> fetchAssessmentInteractionInWeek(@Bind("classId") String classId,
      @Bind("actorId") String actorId, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

  @Mapper(StudentItemInteractionMapper.class)
  @SqlQuery("select collection_type, count(distinct collection_id) as unique_item_count, count(*) as interaction_count, SUM(timespent) as timespent, ROUND(AVG(score)) as score, SUM(max_score) as max_score from collection_performance WHERE course_id is NOT NULL AND (path_id IS NOT NULL AND path_id <> 0) AND collection_type IN ('assessment', 'assessment-external') AND status = true AND created_at BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId group by collection_type")
  List<StudentItemInteraction> fetchAssessmentSuggestionInteractionInWeek(
      @Bind("classId") String classId, @Bind("actorId") String actorId,
      @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

  @Mapper(StudentItemInteractionMapper.class)
  @SqlQuery("select collection_type, count(distinct collection_id) as unique_item_count, count(*) as interaction_count, SUM(timespent) as timespent, ROUND(AVG(score)) as score, SUM(max_score) as max_score from collection_performance WHERE course_id is NOT NULL AND (path_id IS NULL OR path_id = 0) AND collection_type IN ('collection', 'collection-external') AND created_at BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId group by collection_type")
  List<StudentItemInteraction> fetchCollectionInteractionInWeek(@Bind("classId") String classId,
      @Bind("actorId") String actorId, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

  @Mapper(StudentItemInteractionMapper.class)
  @SqlQuery("select collection_type, count(distinct collection_id) as unique_item_count, count(*) as interaction_count, SUM(timespent) as timespent, ROUND(AVG(score)) as score, SUM(max_score) as max_score from collection_performance WHERE course_id is NOT NULL AND (path_id IS NOT NULL AND path_id <> 0) AND collection_type IN ('collection', 'collection-external') AND created_at BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId group by collection_type")
  List<StudentItemInteraction> fetchCollectionSuggestionInteractionInWeek(
      @Bind("classId") String classId, @Bind("actorId") String actorId,
      @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

}
