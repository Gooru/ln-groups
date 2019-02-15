package org.gooru.groups.reports.dbhelpers.core;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface CoreUnitDao {

  @SqlQuery("select title from unit where unit_id = :id::uuid and course_id = :courseId::uuid and is_deleted = false")
  String fetchUnit(@Bind("id") String id, @Bind("courseId") String courseId);

}
