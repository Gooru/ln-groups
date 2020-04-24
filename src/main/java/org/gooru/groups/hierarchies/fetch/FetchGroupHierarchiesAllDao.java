package org.gooru.groups.hierarchies.fetch;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public interface FetchGroupHierarchiesAllDao {

  @Mapper(FetchGroupHierarchiesAllModelMapper.class)
  @SqlQuery("SELECT * FROM group_hierarchy")
  List<FetchGroupHierarchiesAllModel> fetchAllGroupHierarchies();
}
