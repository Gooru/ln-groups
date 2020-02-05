
package org.gooru.groups.reports.dbhelpers.core;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.gooru.groups.app.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public interface CoreDao {

  @Mapper(CountryModelMapper.class)
  @SqlQuery("SELECT id, name, code FROM country_ds WHERE id = ANY(:countryIds::bigint[])")
  List<CountryModel> fetchCountryDetails(@Bind("countryIds") String countryIds);

  @Mapper(DrilldownModelMapper.class)
  @SqlQuery("SELECT id, name, code FROM state_ds WHERE id = ANY(:stateIds::bigint[])")
  List<DrilldownModel> fetchStateDetails(@Bind("stateIds") String stateIds);

  @Mapper(DrilldownModelMapper.class)
  @SqlQuery("SELECT id, name, code FROM state_ds WHERE country_id = :countryId::bigint")
  List<DrilldownModel> fetchStatesByCountry(@Bind("countryId") Long countryId);

  @Mapper(GroupModelMapper.class)
  @SqlQuery("SELECT id, name, code, type, sub_type FROM groups WHERE id = ANY(:groupIds::bigint[])")
  List<GroupModel> fetchGroupDetails(@Bind("groupIds") String groupIds);

  @Mapper(GroupModelMapper.class)
  @SqlQuery("SELECT id, name, code, type, sub_type FROM groups WHERE id = :groupId::bigint")
  GroupModel fetchGroupById(@Bind("groupId") Long groupId);

  @Mapper(DrilldownModelMapper.class)
  @SqlQuery("SELECT id, name, code FROM school_ds WHERE id = ANY(:schoolIds::bigint[])")
  List<DrilldownModel> fetchSchoolDetails(@Bind("schoolIds") String schoolIds);

  @Mapper(SubjectModelMapper.class)
  @SqlQuery("SELECT id, title FROM taxonomy_subject WHERE id = ANY(:subjectCodes)")
  List<SubjectModel> fetchSubjectDetails(@Bind("subjectCodes") PGArray<String> subjectCodes);

  @Mapper(GroupModelMapper.class)
  @SqlQuery("SELECT id, name, code, type, sub_type FROM groups WHERE state_id = :stateId::bigint AND (sub_type = 'school_district' OR"
      + " sub_type = 'district')")
  List<GroupModel> fetchGroupsByState(@Bind("stateId") Long stateId);

  @SqlQuery("SELECT school_id FROM group_school_mapping WHERE group_id = :groupId::bigint")
  Set<Long> fetchSchoolsByGroup(@Bind("groupId") Long groupId);

  @Mapper(GroupModelMapper.class)
  @SqlQuery("SELECT id, name, code, type, sub_type FROM groups WHERE parent_id = :groupId::bigint")
  List<GroupModel> fetchGroupsByParent(@Bind("groupId") Long groupId);

  @SqlQuery("SELECT role_id FROM user_role_mapping WHERE user_id = :userId")
  List<Integer> fetchUserRoles(@Bind("userId") UUID userId);
  
  @SqlQuery("SELECT school_id FROM country_school_mapping WHERE country_id = :countryId::bigint")
  Set<Long> fetchSchoolsByCountry(@Bind("countryId") Long countryId);
}
