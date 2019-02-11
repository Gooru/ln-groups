package org.gooru.groups.reports.dbhelpers.core;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface CoreClassDao {

  @Mapper(ClassModelMapper.class)
  @SqlQuery("select code, title, creator_id, course_id, grade_current from class where id = :classId::uuid and is_deleted = false")
  ClassModel fetchClass(@Bind("classId") String classId);

}
