package org.gooru.groups.reports.dbhelpers.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author renuka
 */
public class CoreService {

  private final static Logger LOGGER = LoggerFactory.getLogger(CoreService.class);

  private final CoreClassDao classDao;
  private final CoreClassMemberDao classMembersDao;
  private final CoreCourseDao courseDao;
  private final CoreUnitDao unitDao;
  private final CoreLessonDao lessonDao;
  private final CoreCollectionDao collectionDao;
  private final CoreUserDao userDao;
  private final CoreDao coreDao;

  public CoreService(DBI dbi) {
    this.classDao = dbi.onDemand(CoreClassDao.class);
    this.classMembersDao = dbi.onDemand(CoreClassMemberDao.class);
    this.courseDao = dbi.onDemand(CoreCourseDao.class);
    this.unitDao = dbi.onDemand(CoreUnitDao.class);
    this.lessonDao = dbi.onDemand(CoreLessonDao.class);
    this.collectionDao = dbi.onDemand(CoreCollectionDao.class);
    this.userDao = dbi.onDemand(CoreUserDao.class);
    this.coreDao = dbi.onDemand(CoreDao.class);
  }

  public ClassModel fetchClass(String classId) {
    return this.classDao.fetchClass(classId);
  }

  public Map<String, ClassModel> fetchClassDetails(Set<String> classIds) {
    Map<String, ClassModel> classModelMap = new HashMap<>();

    List<ClassModel> classModels =
        this.classDao.fetchClassDetails(CollectionUtils.convertToSqlArrayOfUUID(classIds));
    classModels.forEach(classModel -> {
      classModelMap.put(classModel.getId(), classModel);
    });
    return classModelMap;
  }

  public Map<String, ClassModel> fetchClassesBySchool(Long schoolId) {
    Map<String, ClassModel> classModelMap = new HashMap<>();

    List<ClassModel> classModels = this.classDao.fetchClassesBySchool(schoolId);
    classModels.forEach(classModel -> {
      classModelMap.put(classModel.getId(), classModel);
    });
    return classModelMap;
  }
  
  public Map<String, ClassModel> fetchClassesByGroup(Long groupId) {
    Map<String, ClassModel> classModelMap = new HashMap<>();

    List<ClassModel> classModels = this.classDao.fetchClassesByGroup(groupId);
    classModels.forEach(classModel -> {
      classModelMap.put(classModel.getId(), classModel);
    });
    return classModelMap;
  }

  public String fetchCourseTitle(String courseId) {
    return this.courseDao.fetchCourse(courseId);
  }

  public String fetchUnitTitle(String unitId, String courseId) {
    return this.unitDao.fetchUnit(unitId, courseId);
  }

  public String fetchLessonTitle(String lessonId, String unitId) {
    return this.lessonDao.fetchLesson(lessonId, unitId);
  }

  public String fetchCollectionTitleByLessonId(String collectionId, String lessonId) {
    return this.collectionDao.fetchCollectionByLessonId(collectionId, lessonId);
  }

  public String fetchCollectionTitle(String collectionId) {
    return this.collectionDao.fetchCollection(collectionId);
  }

  public List<ClassMembersModel> fetchClassMembers(String classId) {
    return this.classMembersDao.fetchClassMembers(classId);
  }
  
  public List<ClassMembersModel> fetchClassMembersByMemberId(String classId, String userId) {
    return this.classMembersDao.fetchClassMembersByMemberId(classId, userId);
  }

  public UserModel fetchUser(String userId) {
    return this.userDao.fetchUser(userId);
  }

  public List<CompetencyModel> findCompetenciesForCollection(String collectionId) {
    return this.collectionDao.findCompetenciesForCollection(collectionId);
  }

  public Map<Long, CountryModel> fetchCountryDetails(Set<Long> countryIds) {
    Map<Long, CountryModel> countryModelMap = new HashMap<>();
    List<CountryModel> counrtyModels =
        this.coreDao.fetchCountryDetails(CollectionUtils.toPostgresArrayLong(countryIds));
    counrtyModels.forEach(state -> {
      countryModelMap.put(state.getId(), state);
    });
    return countryModelMap;
  }

  public Map<Long, DrilldownModel> fetchStateDetails(Set<Long> stateIds) {
    Map<Long, DrilldownModel> stateModelMap = new HashMap<>();
    List<DrilldownModel> stateModels =
        this.coreDao.fetchStateDetails(CollectionUtils.toPostgresArrayLong(stateIds));
    stateModels.forEach(state -> {
      stateModelMap.put(state.getId(), state);
    });
    return stateModelMap;
  }

  public Map<Long, DrilldownModel> fetchStatesByCountry(Long countryId) {
    Map<Long, DrilldownModel> stateModelMap = new HashMap<>();
    List<DrilldownModel> stateModels = this.coreDao.fetchStatesByCountry(countryId);
    stateModels.forEach(state -> {
      stateModelMap.put(state.getId(), state);
    });
    return stateModelMap;
  }

  public Map<Long, GroupModel> fetchGroupDetails(Set<Long> groupIds) {
    Map<Long, GroupModel> groupModelMap = new HashMap<>();
    List<GroupModel> groupModels =
        this.coreDao.fetchGroupDetails(CollectionUtils.toPostgresArrayLong(groupIds));
    groupModels.forEach(group -> {
      groupModelMap.put(group.getId(), group);
    });

    return groupModelMap;
  }

  public Map<Long, GroupModel> fetchGroupsByState(Long stateId) {
    Map<Long, GroupModel> groupModelMap = new HashMap<>();
    List<GroupModel> groupModels = this.coreDao.fetchGroupsByState(stateId);
    groupModels.forEach(group -> {
      groupModelMap.put(group.getId(), group);
    });
    return groupModelMap;
  }

  public Map<Long, DrilldownModel> fetchSchoolDetails(Set<Long> schoolIds) {
    Map<Long, DrilldownModel> schoolModelMap = new HashMap<>();
    List<DrilldownModel> schoolModels =
        this.coreDao.fetchSchoolDetails(CollectionUtils.toPostgresArrayLong(schoolIds));
    schoolModels.forEach(school -> {
      schoolModelMap.put(school.getId(), school);
    });
    return schoolModelMap;
  }

  public GroupModel fetchGroupById(Long groupId) {
    return this.coreDao.fetchGroupById(groupId);
  }

  public Map<String, SubjectModel> fetchSubjectDetails(Set<String> subjectCodes) {
    Map<String, SubjectModel> subjectModelMap = new HashMap<>();
    List<SubjectModel> subjectModels =
        this.coreDao.fetchSubjectDetails(CollectionUtils.convertToSqlArrayOfString(subjectCodes));
    subjectModels.forEach(model -> {
      subjectModelMap.put(model.getId(), model);
    });
    return subjectModelMap;
  }

  public Set<Long> fetchSchoolsByGroup(Long groupId) {
    return this.coreDao.fetchSchoolsByGroup(groupId);
  }

  public Map<Long, GroupModel> fetchGroupsByParent(Long groupId) {
    Map<Long, GroupModel> groupModelMap = new HashMap<>();
    List<GroupModel> groupModels = this.coreDao.fetchGroupsByParent(groupId);
    groupModels.forEach(group -> {
      groupModelMap.put(group.getId(), group);
    });
    return groupModelMap;
  }

  public List<Integer> fetchUserRoles(UUID userId) {
    return this.coreDao.fetchUserRoles(userId);
  }

  public Set<Long> fetchSchoolsByCountry(Long countryId) {
    return this.coreDao.fetchSchoolsByCountry(countryId);
  }

  private Boolean isParentTenant(String tenantId) {
    return this.coreDao.isParentTenant(tenantId);
  }

  public Set<String> fetchSubTenants(String tenantId) {
    Set<String> tenants = null;
    if (isParentTenant(tenantId)) {
      LOGGER.debug("tenant '{}' is parent, fetching sub tenants", tenantId);
      tenants = this.coreDao.fetchSubTenants(tenantId);
      if (tenants != null && !tenants.isEmpty()) {
        return tenants;
      }
    }

    LOGGER.debug("looks like tenant '{}' is not parent or don't have any sub tenant, returning it",
        tenantId);
    tenants = new HashSet<>();
    tenants.add(tenantId);
    return tenants;
  }
  
  public Map<Long, GroupModel> fetchFlexibleGroupDetails(Set<Long> groupIds) {
    Map<Long, GroupModel> groupModelMap = new HashMap<>();
    List<GroupModel> groupModels =
        this.coreDao.fetchFlexibleGroupDetails(CollectionUtils.toPostgresArrayLong(groupIds));
    groupModels.forEach(group -> {
      groupModelMap.put(group.getId(), group);
    });

    return groupModelMap;
  }
}
