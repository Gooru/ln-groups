package org.gooru.groups.reports.dbhelpers.core.hierarchy;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru
 *
 */
public interface GroupHierarchyDao {
  
  @Mapper(GroupHierarchyDetailsModelMapper.class)
  @SqlQuery("SELECT * FROM group_hierarchy_details WHERE hierarchy_id = :hierarchyId ORDER BY sequence ASC")
  public List<GroupHierarchyDetailsModel> fetchGroupHierarchyDetails(@Bind("hierarchyId") Long hierarchyId);
  
  @SqlQuery("SELECT value FROM tenant_setting WHERE id = :tenant::uuid AND key = 'group_hierarchy'")
  public String fetchHierarchyByTenant(@Bind("tenant") String tenant);
}
