package org.gooru.groups.reports.dbhelpers.core.groupacl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.jdbi.PGArray;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonArray;

/**
 * @author szgooru on 18-Feb-2020
 *
 */
public class GroupACLService {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupACLService.class);

  private final GroupACLDao dao;

  public GroupACLService(DBI dbi) {
    this.dao = dbi.onDemand(GroupACLDao.class);
  }

  /**
   * This method returns the mapping of group type along with group ids that user has access to. The
   * data is populated by the parent reference id
   */
  public Map<String, Map<Long, JsonArray>> fetchAllUserGroupACL(String userId, Long hierarchyId,
      Set<String> tenants) {
    List<GroupACLModel> groupAcls = this.dao.fetchUserGroupACL(userId, hierarchyId);

    // This map will hold the group type with sub map of group ids by the parent of that group.
    Map<String, Map<Long, JsonArray>> groupACLMap = new HashMap<>();
    for (GroupACLModel model : groupAcls) {

      JsonArray groups = model.getGroups();
      if (groups != null && !groups.isEmpty()) {
        // If the user has access to all of groups then ACL will define the '*' instead of all
        // group ids. In this case, we need to resolve to all the child nodes of particular parent
        // to arrive at specific list of group ids.
        int nodeCount = groups.size();
        if (nodeCount == 1) {
          String nodeValue = String.valueOf(groups.getValue(0));
          if (nodeValue.equalsIgnoreCase("*")) {
            LOGGER.debug("* found in groups list, resolving all nodes for hierarchy {} and type {}",
                hierarchyId, model.getType());
            model.setGroups(resolveGroupIds(hierarchyId, model.getType(), tenants));
            updateACLMap(groupACLMap, model);
          } else {
            updateACLMap(groupACLMap, model);
          }
        } else {
          updateACLMap(groupACLMap, model);
        }
      }
    }
    return groupACLMap;
  }

  /**
   * This method returns the list of group type along with group ids that user has access to This
   * data is populated by the parent reference id
   */
  public List<GroupACLModel> fetchUserGroupACL(String userId, Long hierarchyId) {
    return this.dao.fetchUserGroupACL(userId, hierarchyId);
  }

  /**
   * This method returns all the groups defined for the hierarchy and type of the group. It also
   * accepts the tenant to filter the list of groups for specific tenant. This method is mostly used
   * to resolve the ACLs defined as *
   */
  public Set<Long> fetchAllGroupsByType(Long hierarchyId, String type, PGArray<String> tenants) {
    return this.dao.fetchAllGroupsByType(hierarchyId, type, tenants);
  }

  private JsonArray resolveGroupIds(Long hierarchyId, String type, Set<String> tenants) {
    Set<Long> allGroupIds = this.dao.fetchAllGroupsByType(hierarchyId, type,
        CollectionUtils.convertToSqlArrayOfString(tenants));
    JsonArray resolvedGroupIds = new JsonArray();
    allGroupIds.forEach(resolvedGroupIds::add);
    return resolvedGroupIds;
  }

  private void updateACLMap(Map<String, Map<Long, JsonArray>> groupACLMap, GroupACLModel model) {
    LOGGER.debug("User ACL {}", model.toString());
    String type = model.getType();

    // If the parent reference id is null in the ACL table, it denotes that the record is for the
    // root node of the group hierarchy. Hence we will store with the zero value as key to
    // identify it while processing.
    Long parentRefId = model.getParentRefId() == null ? 0l : model.getParentRefId();

    if (groupACLMap.containsKey(type)) {
      groupACLMap.get(type).put(parentRefId, model.getGroups());
    } else {
      Map<Long, JsonArray> parentGroupMap = new HashMap<>();
      parentGroupMap.put(parentRefId, model.getGroups());
      groupACLMap.put(type, parentGroupMap);
    }
  }
}
