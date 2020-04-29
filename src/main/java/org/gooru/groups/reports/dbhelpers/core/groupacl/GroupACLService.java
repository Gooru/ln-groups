package org.gooru.groups.reports.dbhelpers.core.groupacl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  
  /*
   * This method returns the list of group type along with group ids that user has access to
   * populated by the parent reference id
   */
  public Map<String, Map<Long, JsonArray>> fetchAllUserGroupACL(String userId, Long hierarchyId) {
    List<GroupACLModel> groupAcls = this.dao.fetchUserGroupACL(userId, hierarchyId);

    // This map will hold the group type with sub map of group ids by the parent of that group.
    Map<String, Map<Long, JsonArray>> groupACLMap = new HashMap<>();
    for (GroupACLModel model : groupAcls) {
      // If the user has only one node access in the ACL, then skip those nodes and report the
      // data only for the level for which user has multiple nodes access.

      // TODO: If the user has access to all of groups then ACL will define the '*' instead of all
      // group ids. In this case, we need to resolve to all the child nodes of particular parent to
      // arrive at specific list of group ids.
      JsonArray groups = model.getGroups();
      if (groups != null && !groups.isEmpty()) {
        // If the number of nodes in the group ACL is only 1 then check if its mentioned as *. If it
        // is * then resolve resolve to all group levels to report the data.
        int nodeCount = groups.size();
        if (nodeCount == 1) {
          String nodeValue = String.valueOf(groups.getValue(0));
          if (nodeValue.equalsIgnoreCase("*")) {
            LOGGER.debug("* found in groups list, resolving to all nodes");
            // TODO: resolve all nodes for this level and add in map
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

  /*
   * This method returns the list of group type along with group ids that user has access to
   * populated by the parent reference id
   */
  public List<GroupACLModel> fetchUserGroupACL(String userId, Long hierarchyId) {
    return this.dao.fetchUserGroupACL(userId, hierarchyId);
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
