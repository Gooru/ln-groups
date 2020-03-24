package org.gooru.groups.hierarchies.fetch;

import java.util.List;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class FetchGroupHierarchiesAllResponseBuilder {

  public static FetchGroupHierarchiesAllResponseModel build(
      List<FetchGroupHierarchiesAllModel> hierarchies) {
    FetchGroupHierarchiesAllResponseModel response = new FetchGroupHierarchiesAllResponseModel();
    response.setHierarchies(hierarchies);
    return response;
  }
}
