package org.gooru.groups.reports.dbhelpers.core.groupacl;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 18-Feb-2020
 *
 */
public interface GroupACLDao {

  @Mapper(GroupACLModelMapper.class)
  @SqlQuery("SELECT type, groups FROM group_user_acl WHERE user_id = :userId")
  public List<GroupACLModel> fetchUserGroupACL(@Bind("userId") String userId); 
}
