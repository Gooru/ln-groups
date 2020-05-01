package org.gooru.groups.reports.dbhelpers.core.groupacl;

import java.util.List;
import java.util.Set;
import org.gooru.groups.app.jdbi.PGArray;
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
  public List<GroupACLModel> fetchUserGroupACL(@Bind("userId") String userId,
      @Bind("hierarchyId") Long hierarchyId);


  @SqlQuery("SELECT group_id FROM group_hierarchy_mapping WHERE hierarchy_id = :hierarchyId AND type = :type AND tenant = ANY(:tenants)")
  public Set<Long> fetchAllGroupsByType(@Bind("hierarchyId") Long hierarchyId,
      @Bind("type") String type, @Bind("tenant") PGArray<String> tenants);
}
