package org.gooru.groups.reports.dbhelpers.core;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface CoreCourseDao {

  @SqlQuery("select title from course where id = :id::uuid")
  String fetchCourse(@Bind("id") String id);

}
