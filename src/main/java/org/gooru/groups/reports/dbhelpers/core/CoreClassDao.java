package org.gooru.groups.reports.dbhelpers.core;

import java.util.List;
import java.util.UUID;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface CoreClassDao {

  @Mapper(ClassModelMapper.class)
  @SqlQuery("select c.id as id, code, title, creator_id, course_id, g.grade as grade_current from class c left join grade_master g on"
      + " c.grade_current = g.id where c.id = :classId::uuid and is_deleted = false")
  ClassModel fetchClass(@Bind("classId") String classId);

  @Mapper(ClassModelMapper.class)
  @SqlQuery("SELECT id, title, code, creator_id, course_id, grade_current FROM class where id = ANY(:classIds) AND is_deleted = false")
  List<ClassModel> fetchClassDetails(@Bind("classIds") PGArray<UUID> classIds);

  @Mapper(ClassModelMapper.class)
  @SqlQuery("SELECT id, title, code, creator_id, course_id, grade_current FROM class where school_id = :schoolId::bigint AND is_deleted = false")
  List<ClassModel> fetchClassesBySchool(@Bind("schoolId") Long schoolId);
}
