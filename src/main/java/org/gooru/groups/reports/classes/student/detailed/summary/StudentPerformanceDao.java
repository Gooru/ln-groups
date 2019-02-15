
package org.gooru.groups.reports.classes.student.detailed.summary;

import java.util.Date;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface StudentPerformanceDao {

  @Mapper(StudentPerformanceModelMapper.class)
  @SqlQuery("SELECT timespent, score, max_score, course_id, unit_id, lesson_id, collection_id, collection_type, session_id, is_graded, created_at FROM collection_performance WHERE course_id is NOT NULL AND (path_id IS NULL OR path_id = 0) AND collection_type IN ('assessment','assessment-external') AND status = true AND created_at BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId")
  List<StudentPerformanceModel> fetchAssessmentPerformanceInWeek(@Bind("classId") String classId,
      @Bind("actorId") String actorId, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

  @Mapper(StudentPerformanceModelMapper.class)
  @SqlQuery("SELECT timespent, score, max_score, course_id, unit_id, lesson_id, collection_id, collection_type, session_id, is_graded, created_at FROM collection_performance WHERE course_id is NOT NULL AND (path_id IS NULL OR path_id = 0) AND collection_type IN ('collection', 'collection-external') AND created_at BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId")
  List<StudentPerformanceModel> fetchCollectionPerformanceInWeek(@Bind("classId") String classId,
      @Bind("actorId") String actorId, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

}
