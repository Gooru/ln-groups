package org.gooru.groups.reports.dbhelpers.core;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface CoreUnitDao {

  @SqlQuery("select title from unit where unit_id = :id::uuid")
  String fetchUnit(@Bind("id") String id);

}
