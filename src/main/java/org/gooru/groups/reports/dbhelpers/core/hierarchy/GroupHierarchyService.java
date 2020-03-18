package org.gooru.groups.reports.dbhelpers.core.hierarchy;

import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru
 *
 */
public class GroupHierarchyService {
  
  private final GroupHierarchyDao dao;

  public GroupHierarchyService(DBI dbi) {
    this.dao = dbi.onDemand(GroupHierarchyDao.class);
  }

  public Long fetchGroupHierarchyByTenant(String tenant) {
    String strGroupHierarchyId = this.dao.fetchHierarchyByTenant(tenant);
    try {
      return Long.parseLong(strGroupHierarchyId);
    } catch (NumberFormatException nfe) {
      return null;
    }
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Node<GroupHierarchyDetailsModel> fetchGroupHierarchyDetails(Long hierarchyId) {
    List<GroupHierarchyDetailsModel> groupHierarchy = this.dao.fetchGroupHierarchyDetails(hierarchyId);
    Node<GroupHierarchyDetailsModel> parent = null;
    Node<GroupHierarchyDetailsModel> prevNode = null;
    for (GroupHierarchyDetailsModel level : groupHierarchy) {
      if (parent == null) {
        parent = new Node(level);
        prevNode = parent;
      } else {
        Node<GroupHierarchyDetailsModel> child = new Node(level);
        prevNode.addChild(child);
        prevNode = child;
      }
    }
    
    return parent;
    
  }
}
