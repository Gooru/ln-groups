
package org.gooru.groups.reports.classes.student.summary;

import java.util.Date;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface StudentContentInteractionDao {

  @SqlQuery("select sum(time_spent) as time_spent FROM base_reports WHERE unit_id is NOT NULL AND event_name = 'collection.resource.play' AND event_type = 'stop' AND (path_id IS NULL OR path_id = 0) AND date_in_time_zone BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId")
  Long fetchClassTimespentInWeek(@Bind("classId") String classId, @Bind("actorId") String actorId,
      @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

  @Mapper(StudentContentInteractionMapper.class)
  @SqlQuery("select actor_id, sum(a.coll) as collection_count, sum(a.ass) as assessment_count from (select actor_id, collection_type, CASE WHEN collection_type = 'collection' THEN count(collection_type) ELSE 0 END as coll, CASE WHEN collection_type = 'assessment' THEN count(collection_type) ELSE 0 END as ass, SUM(time_spent) as timeSpent from base_reports WHERE unit_id is NOT NULL AND event_name = 'collection.play' AND event_type = 'stop' AND (path_id IS NULL OR path_id = 0)  AND date_in_time_zone BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId group by 1,2) as a group by 1")
  StudentContentInteraction fetchStudentContentInteractionInWeek(@Bind("classId") String classId,
      @Bind("actorId") String actorId, @Bind("fromDate") Date fromDate,
      @Bind("toDate") Date toDate);

  @Mapper(StudentSuggestedItemInteractionMapper.class)
  @SqlQuery("select collection_type from base_reports WHERE  unit_id is NOT NULL AND event_name = 'collection.play' AND event_type = 'stop' AND (path_id IS NOT NULL AND path_id <> 0) AND date_in_time_zone BETWEEN :fromDate AND :toDate and class_id =:classId and actor_id = :actorId")
  List<StudentSuggestedItemInteraction> fetchStudentInteractionOnSuggestedItemInWeek(
      @Bind("classId") String classId, @Bind("actorId") String actorId,
      @Bind("fromDate") Date fromDate, @Bind("toDate") Date toDate);

}
