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
  
  
  @SqlQuery("SELECT acl.id, acl.type, acl.groups, acl.parent_reference_id FROM group_user_acl acl, group_hierarchy_details hierarchy WHERE"
      + " acl.user_id = :userId AND acl.type = hierarchy.type AND hierarchy.hierarchy_id = :hierarchyId ORDER BY hierarchy.sequence ASC")
  public List<GroupACLModel> fetchUserGroupACL(@Bind("userId") String userId, @Bind("hierarchyId") Long hierarchyId);

  @Mapper(GroupACLModelMapper.class)
  @SqlQuery("SELECT id, type, groups, parent_reference_id FROM group_user_acl WHERE user_id = :userId AND type = :type"
      + " AND parent_reference_id = :parentRefId")
  public List<GroupACLModel> fetchUserGroupACLByTypeAndId(@Bind("userId") String userId,
      @Bind("type") String type, @Bind("parentRefId") Long parentRefId);
}
