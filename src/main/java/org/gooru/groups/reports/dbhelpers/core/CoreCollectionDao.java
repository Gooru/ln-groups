package org.gooru.groups.reports.dbhelpers.core;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface CoreCollectionDao {

  @SqlQuery("select title from collection where id = :id::uuid and lesson_id = :lessonId::uuid and is_deleted = false")
  String fetchCollection(@Bind("id") String id, @Bind("lessonId") String lessonId);

  @Mapper(CompetencyModelMapper.class)
  @SqlQuery("select dc.key as id, dc.value::jsonb ->>'code' as code from collection JOIN jsonb_each_text(taxonomy) dc ON true where id = :id::uuid")
  List<CompetencyModel> findCompetenciesForCollection(@Bind("id") String id);

}
