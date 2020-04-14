package org.gooru.groups.reports.dbhelpers.core;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface CoreClassMemberDao {

  @Mapper(ClassMembersModelMapper.class)
  @SqlQuery("select user_id, first_name, last_name, u.email from class_member cm inner join users u on cm.user_id = u.id where is_active = true and is_deleted = false and class_id = :classId::uuid")
  List<ClassMembersModel> fetchClassMembers(@Bind("classId") String classId);

  @Mapper(ClassMembersModelMapper.class)
  @SqlQuery("select user_id, first_name, last_name, u.email from class_member cm inner join users u on cm.user_id = u.id where is_active = true and is_deleted = false and class_id = :classId::uuid and user_id = :userId::uuid")
  List<ClassMembersModel> fetchClassMembersByMemberId(@Bind("classId") String classId,
      @Bind("userId") String userId);

}
