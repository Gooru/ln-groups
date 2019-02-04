
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
  @SqlQuery("SELECT SUM(timespent) as time_spent, ROUND(AVG(score)) AS score, collection_id, collection_type, date_in_time_zone, course_id, unit_id, lesson_id FROM (SELECT DISTINCT ON (session_id, collection_id,lesson_id, unit_id, course_id) timespent, score, course_id, unit_id, lesson_id, collection_id, collection_type, session_id, date_in_time_zone FROM collection_performance WHERE unit_id is NOT NULL AND (path_id IS NULL OR path_id = 0) AND date_in_time_zone BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId ORDER BY session_id, collection_id,lesson_id, unit_id, course_id, date_in_time_zone DESC) AS classData GROUP BY collection_id, collection_type,date_in_time_zone, course_id, unit_id, lesson_id")
  List<StudentPerformanceModel> fetchStudentPerformanceInWeek(@Bind("classId") String classId,
      @Bind("actorId") String actorId, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

}
