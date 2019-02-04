package org.gooru.groups.reports.dbhelpers.core;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface CoreCollectionDao {

  @SqlQuery("select title from collection where id = :id::uuid")
  String fetchCollection(@Bind("id") String id);

  @SqlQuery("select dc.value::jsonb ->>'code' as competency from collection JOIN jsonb_each_text(taxonomy) dc ON true where id = :id::uuid")
  List<String> findCompetenciesForCollection(@Bind("id") String id);

}
