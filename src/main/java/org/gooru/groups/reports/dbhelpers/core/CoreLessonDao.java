package org.gooru.groups.reports.dbhelpers.core;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface CoreLessonDao {

  @SqlQuery("select title from lesson where lesson_id = :id::uuid and unit_id = :unitId::uuid and is_deleted = false")
  String fetchLesson(@Bind("id") String id, @Bind("unitId") String unitId);

}
