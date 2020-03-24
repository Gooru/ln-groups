package org.gooru.groups.hierarchies.fetch;

import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class FetchGroupHierarchiesAllService {

  private final FetchGroupHierarchiesAllDao dao;
  
  public FetchGroupHierarchiesAllService(DBI dbi) {
    this.dao = dbi.onDemand(FetchGroupHierarchiesAllDao.class);
  }
  
  public List<FetchGroupHierarchiesAllModel> fetchAllGroupHierarchies() {
    return this.dao.fetchAllGroupHierarchies();
  }
}
