package org.gooru.groups.reports.dbhelpers.core.groupacl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.skife.jdbi.v2.DBI;
import io.vertx.core.json.JsonArray;

/**
 * @author szgooru on 18-Feb-2020
 *
 */
public class GroupACLService {

  private final GroupACLDao dao;

  public GroupACLService(DBI dbi) {
    this.dao = dbi.onDemand(GroupACLDao.class);
  }

  public Map<String, JsonArray> fetchUserGroupACL(String userId) {
    List<GroupACLModel> groupAcls = this.dao.fetchUserGroupACL(userId);
    Map<String, JsonArray> groupACLMap = new HashMap<>();
    groupAcls.forEach(model -> {
      groupACLMap.put(model.getType(), model.getGroups());
    });
    return groupACLMap;
  }
}
