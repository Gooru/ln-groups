package org.gooru.groups.reports.dbhelpers.core.hierarchy;

import java.util.List;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru
 *
 */
public class GroupHierarchyService {
  
  private final static Logger LOGGER = LoggerFactory.getLogger(GroupHierarchyService.class);
  
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
    if (groupHierarchy == null || groupHierarchy.isEmpty()) {
      LOGGER.debug("Hierarchy levels not exists for the hierarchy id {}", hierarchyId);
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "hierarchy levels not exists for the provided hierarchy id");
    }
    
    Node<GroupHierarchyDetailsModel> parent = null;
    Node<GroupHierarchyDetailsModel> prevNode = null;
    for (GroupHierarchyDetailsModel level : groupHierarchy) {
      LOGGER.debug("{}", level.toString());
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
