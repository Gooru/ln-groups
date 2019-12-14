package org.gooru.groups.reports.dbhelpers.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.reports.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author renuka
 */
public class CoreService {

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

  public UserModel fetchUser(String userId) {
    return this.userDao.fetchUser(userId);
  }

  public List<CompetencyModel> findCompetenciesForCollection(String collectionId) {
    return this.collectionDao.findCompetenciesForCollection(collectionId);
  }

  public Map<Long, StateModel> fetchStateDetails(Set<Long> stateIds) {
    Map<Long, StateModel> stateModelMap = new HashMap<>();
    List<StateModel> stateModels =
        this.coreDao.fetchStateDetails(CollectionUtils.toPostgresArrayLong(stateIds));
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

  public Map<Long, SchoolModel> fetchSchoolDetails(Set<Long> schoolIds) {
    Map<Long, SchoolModel> schoolModelMap = new HashMap<>();
    List<SchoolModel> schoolModels =
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
}
