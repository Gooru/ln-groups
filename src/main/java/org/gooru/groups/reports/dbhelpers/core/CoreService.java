package org.gooru.groups.reports.dbhelpers.core;

import java.util.List;
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

  public CoreService(DBI dbi) {
    this.classDao = dbi.onDemand(CoreClassDao.class);
    this.classMembersDao = dbi.onDemand(CoreClassMemberDao.class);
    this.courseDao = dbi.onDemand(CoreCourseDao.class);
    this.unitDao = dbi.onDemand(CoreUnitDao.class);
    this.lessonDao = dbi.onDemand(CoreLessonDao.class);
    this.collectionDao = dbi.onDemand(CoreCollectionDao.class);
    this.userDao = dbi.onDemand(CoreUserDao.class);
  }

  public ClassModel fetchClass(String classId) {
    return this.classDao.fetchClass(classId);
  }

  public String fetchCourseTitle(String courseId) {
    return this.courseDao.fetchCourse(courseId);
  }

  public String fetchUnitTitle(String unitId) {
    return this.unitDao.fetchUnit(unitId);
  }

  public String fetchLessonTitle(String lessonId) {
    return this.lessonDao.fetchLesson(lessonId);
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

  public List<String> findCompetenciesForCollection(String collectionId) {
    return this.collectionDao.findCompetenciesForCollection(collectionId);
  }
}
