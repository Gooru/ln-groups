package org.gooru.groups.reports.dbhelpers.core;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface CoreLessonDao {

  @SqlQuery("select title from lesson where lesson_id = :id::uuid")
  String fetchLesson(@Bind("id") String id);

}
