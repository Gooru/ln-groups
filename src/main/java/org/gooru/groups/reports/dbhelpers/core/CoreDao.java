
package org.gooru.groups.reports.dbhelpers.core;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public interface CoreDao {

  @Mapper(StateModelMapper.class)
  @SqlQuery("SELECT id, name, code FROM state_ds WHERE id = ANY(:stateIds::bigint[])")
  List<StateModel> fetchStateDetails(@Bind("stateIds") String stateIds);
}
