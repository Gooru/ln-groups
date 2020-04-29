package org.gooru.groups.reports.dbhelpers.core.groupacl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.GroupConstants;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyDetailsModel;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonArray;

/**
 * @author szgooru on 01-Apr-2020
 *
 */
public class GroupACLResolver {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupACLResolver.class);

  private final GroupACLService GROUP_ACL_SERVICE =
      new GroupACLService(DBICreator.getDbiForDefaultDS());

  private Map<String, Map<Long, JsonArray>> userGroupACLMap;
  private GroupACLModel rootModel;
  private final String userId;
  private final Long hierarchyId;

  public GroupACLResolver(String userId, Long hierarchyId) {
    this.userId = userId;
    this.hierarchyId = hierarchyId;
  }

  /**
   * This method initializes all the levels of the users group ACL right from the root till leaf
   * node of the hierarchy.
   */
  public void initUsersAllGroupACLs() {
    this.userGroupACLMap =
        this.GROUP_ACL_SERVICE.fetchAllUserGroupACL(this.userId, this.hierarchyId);
    if (userGroupACLMap == null || userGroupACLMap.isEmpty()) {
      LOGGER.debug("user '{}' does not have any group ACL defined", this.userId);
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.FORBIDDEN,
          "no group acl defined for user");
    }
  }

  /**
   * This method initializes the filtered
   */
  public void initFiltereGroupACLs() {
    List<GroupACLModel> groupAcls =
        this.GROUP_ACL_SERVICE.fetchUserGroupACL(this.userId, this.hierarchyId);
    GroupACLModel previousModel = null;
    this.userGroupACLMap = new HashMap<String, Map<Long, JsonArray>>();
    boolean isRootSet = false;
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
            if (!isRootSet) {
              previousModel = model;
            } else {
              updateACLMap(model);
            }
          }
        } else {
          if (!isRootSet && previousModel == null) {
            updateACLMap(model);
            setRootModel(model);
            isRootSet = true;
          } else if (!isRootSet) {
            updateACLMap(previousModel);
            setRootModel(previousModel);
            updateACLMap(model);
            isRootSet = true;
          } else {
            updateACLMap(model);
          }
        }
      }
    }
  }

  private void setRootModel(GroupACLModel model) {
    LOGGER.debug("root is set to group type {}", model.getType());
    this.rootModel = model;
  }

  public GroupACLModel getRootModel() {
    return this.rootModel;
  }

  private void updateACLMap(GroupACLModel model) {
    LOGGER.debug("updating ACL map with Model:{}", model.toString());
    String type = model.getType();

    // If the parent reference id is null in the ACL table, it denotes that the record is for the
    // root node of the group hierarchy. Hence we will store with the zero value as key to
    // identify it while processing.
    Long parentRefId = model.getParentRefId() == null ? 0l : model.getParentRefId();

    if (this.userGroupACLMap.containsKey(type)) {
      this.userGroupACLMap.get(type).put(parentRefId, model.getGroups());
    } else {
      Map<Long, JsonArray> parentGroupMap = new HashMap<>();
      parentGroupMap.put(parentRefId, model.getGroups());
      this.userGroupACLMap.put(type, parentGroupMap);
    }
  }

  public boolean verifyIfGroupIsAccessible(Long requestedGroupId, String groupType) {
    Map<Long, JsonArray> userACLByType = getACLByGroupType(groupType);
    boolean hasUserAccess = false;
    for (Long groupId : userACLByType.keySet()) {
      JsonArray groups = userACLByType.get(groupId);
      for (Object g : groups) {
        Long gId = Long.valueOf(g.toString());
        LOGGER.debug("checking group access for id {} with {}", gId, requestedGroupId);
        if (gId.compareTo(requestedGroupId) == 0) {
          hasUserAccess = true;
          break;
        }
      }
      // Just to break the outer loop as well
      if (hasUserAccess) {
        break;
      }
    }

    if (!hasUserAccess) {
      LOGGER.debug(
          "user '{}' does not have access to group '{}' or provided group type does not match",
          userId, requestedGroupId);
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.FORBIDDEN,
          "group not accessible to user or provided group type does not match");
    }

    return hasUserAccess;
  }

  public Set<Long> getChildNodes(Long requestedGroupId) {
    Map<Long, Set<String>> parentChildMap = new HashMap<>();
    for (String type : this.userGroupACLMap.keySet()) {
      Map<Long, JsonArray> groupMap = this.userGroupACLMap.get(type);
      for (Long groupId : groupMap.keySet()) {
        JsonArray groupIds = groupMap.get(groupId);
        Set<String> childs = new HashSet<>();
        groupIds.forEach(gid -> {
          childs.add(gid.toString());
        });
        parentChildMap.put(groupId, childs);
      }
    }

    Set<String> childs = parentChildMap.get(requestedGroupId);
    if (childs == null || childs.isEmpty()) {
      return new HashSet<>();
    }

    Set<Long> groupIds = new HashSet<>();
    for (String ch : childs) {
      groupIds.add(Long.valueOf(ch));
    }

    return groupIds;
  }

  public Map<Long, Set<String>> getClassesByGroupACL(
      Node<GroupHierarchyDetailsModel> groupHierarchy) {
    Map<Long, Set<String>> classesByGroups = new HashMap<>();
    Node<GroupHierarchyDetailsModel> leafNode = groupHierarchy.getLeaf();
    if (leafNode.getData().getType().equalsIgnoreCase(GroupConstants.LEVEL_CLASS)) {
      Map<Long, JsonArray> userACLByHierarchyType = userGroupACLMap.get(GroupConstants.LEVEL_CLASS);
      for (Long groupId : userACLByHierarchyType.keySet()) {
        JsonArray ids = userACLByHierarchyType.get(groupId);
        Set<String> classSet = new HashSet<>();
        ids.forEach(id -> {
          String strId = id.toString();
          classSet.add(strId);
        });
        classesByGroups.put(groupId, classSet);
      }

      // get the parent node of the leaf to start the data aggregation till root node of the
      // hierarchy. Here we will extract all the groups under the every parent and
      // aggregate the data.
      Node<GroupHierarchyDetailsModel> currentNode = leafNode.getParent();
      boolean isRootProcessingDone = false;
      while (!isRootProcessingDone) {
        String groupType = currentNode.getData().getType();
        Map<Long, JsonArray> aclByGroupType = userGroupACLMap.get(groupType);
        if (aclByGroupType == null || aclByGroupType.isEmpty()) {
          continue;
        }
        
        for (Long groupId : aclByGroupType.keySet()) {
          Set<String> newSetOfClasses = new HashSet<>();
          JsonArray ids = aclByGroupType.get(groupId);
          ids.forEach(id -> {
            Long gId = Long.valueOf(id.toString());
            Set<String> setOfClasses = classesByGroups.get(gId);
            if (setOfClasses != null) {
              newSetOfClasses.addAll(setOfClasses);
            }
          });
          classesByGroups.put(groupId, newSetOfClasses);
        }

        if (currentNode.isRoot() || (this.rootModel != null
            && currentNode.getData().getType().equalsIgnoreCase(this.rootModel.getType()))) {
          LOGGER.debug("class accumulation till root of ACL has been completed");
          isRootProcessingDone = true;
        } else {
          // Here reset the pointers of previous level and current node. Previous level should be
          // the current group type for which the aggregation processing is completed and current
          // node will be parent node of the level for which the data has been aggregated.
          currentNode = currentNode.getParent();
        }
      }
    }

    return classesByGroups;
  }

  private Map<Long, JsonArray> getACLByGroupType(String groupType) {
    Map<Long, JsonArray> userACLByType = this.userGroupACLMap.get(groupType);
    if (userACLByType == null || userACLByType.isEmpty()) {
      LOGGER.debug("user '{}' does not have access to any group of type '{}'", this.userId,
          groupType);
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.FORBIDDEN,
          "ngroup type not accessible to user");
    }

    return userACLByType;
  }

}
