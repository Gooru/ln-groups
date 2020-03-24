package org.gooru.groups.hierarchies.fetch;

import java.util.List;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class FetchGroupHierarchiesAllResponseModel {

  private List<FetchGroupHierarchiesAllModel> hierarchies;

  public List<FetchGroupHierarchiesAllModel> getHierarchies() {
    return hierarchies;
  }

  public void setHierarchies(List<FetchGroupHierarchiesAllModel> hierarchies) {
    this.hierarchies = hierarchies;
  }
  
}
