package org.gooru.groups.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * @author ashish on 20/2/18.
 */
public final class Constants {

  public static final class HttpRequestConstants {
  }

  public static final class HttpResponseContants {
    public static final String RESP_KEY_MESSAGE = "message";
    public static final String RESP_KEY_STATUS = "status";

    public static final String STATUS_ERROR = "error";
    public static final String STATUS_SUCCESS = "success";
  }

  public static final class EventBus {

    public static final String MBEP_TOKEN_VERIFICATION =
        "org.gooru.groups.eventbus.token.verification";

    public static final String MBEP_DISPATCHER = "org.gooru.groups.dispatcher";
    public static final String MBUS_TIMEOUT = "event.bus.send.timeout.seconds";

    private EventBus() {
      throw new AssertionError();
    }
  }

  public static final class Message {

    public static final String NO_VALUE = new UUID(0, 0).toString();
    public static final String SESSION_CONTEXT_TYPE = "context.type";
    public static final String SESSION_CONTEXT_TYPE_SYSTEM = "context.system";
    public static final String SESSION_CONTEXT_SYSTEM_TENANT = "context.system.tenant";
    public static final String SESSION_CONTEXT_TYPE_USER = "context.user";

    public static final String MSG_OP = "mb.op";
    public static final String MSG_OP_STATUS = "mb.op.status";
    public static final String MSG_OP_VERIFY_TOKEN = "mb.op.verify.token";
    public static final String MSG_OP_STATUS_SUCCESS = "mb.op.status.success";
    public static final String MSG_OP_STATUS_FAIL = "mb.op.status.fail";

    public static final String MSG_API_VERSION = "api.version";
    public static final String MSG_SESSION_TOKEN = "session.token";
    public static final String MSG_SESSION_TENANT = "tenant";
    public static final String MSG_SESSION_TENANT_ID = "tenant_id";
    public static final String MSG_ACCESS_TOKEN = "access_token";
    public static final String MSG_KEY_SESSION = "session";
    public static final String MSG_USER_ANONYMOUS = "anonymous";
    public static final String MSG_USER_ID = "user_id";
    public static final String MSG_HTTP_STATUS = "http.status";
    public static final String MSG_HTTP_BODY = "http.body";
    public static final String MSG_HTTP_HEADERS = "http.headers";

    public static final String MSG_MESSAGE = "message";

    public static final String MSG_OP_GROUPS_CREATE = "mb.op.groups.create";
    public static final String MSG_OP_GROUPS_UPDATE = "mb.op.groups.update";
    public static final String MSG_OP_GROUPS_DELETE = "mb.op.groups.delete";

    public static final String MSG_OP_GROUPS_ASSOCIATE = "mb.op.groups.associate";
    public static final String MSG_OP_GROUPS_REMOVE = "mb.op.groups.remove";

    public static final String MSG_OP_GROUPS_ADD_CLASS = "mb.op.groups.class.add";
    public static final String MSG_OP_GROUPS_REMOVE_CLASS = "mb.op.groups.class.remove";
    public static final String MSG_OP_GROUPS_LIST_CLASS = "mb.op.groups.class.list";

    public static final String MSG_OP_GROUPS_ADD_USER = "mb.op.groups.user.add";
    public static final String MSG_OP_GROUPS_REMOVE_USER = "mb.op.groups.user.remove";
    public static final String MSG_OP_GROUPS_LIST_USER = "mb.op.groups.user.list";

    public static final String MSG_OP_GROUPS_LIST_GROUPS_TENANT = "mb.op.groups.list.by.tenant";
    public static final String MSG_OP_GROUPS_LIST_CLASSES_TENANT =
        "mb.op.groups.classes.list.by.tenant";

    public static final String MSG_OP_REPORTS_GET_CA_ACTIVITIES_COUNT =
        "mb.op.reports.ca.activities.count.get";
    public static final String MSG_OP_REPORTS_GET_CLASS_SUMMARY_WEEKLY =
        "mb.op.reports.class.summary.weekly";
    public static final String MSG_OP_REPORTS_GET_CLASS_STUDENT_SUMMARY_WEEKLY =
        "mb.op.reports.class.student.summary.weekly";
    public static final String MSG_OP_REPORTS_GET_CLASS_STUDENT_DETAILED_SUMMARY_WEEKLY =
        "mb.op.reports.class.student.detailed.summary.weekly";
    
    public static final String MSG_OP_REPORTS_GROUPS_COUNTRIES = "mb.op.reports.groups.contries.get";
    public static final String MSG_OP_PERF_REPORTS_SUBJECTS_BY_COUNTRY = "mb.op.perf.reports.country.subjects.get";
    public static final String MSG_OP_PERF_REPORTS_GROUPS_BY_COUNTRY = "mb.op.perf.reports.groups.country.get";
    public static final String MSG_OP_PERF_REPORTS_GROUPS_BY_STATE = "mb.op.perf.reports.groups.state.get";
    public static final String MSG_OP_PERF_REPORTS_GROUPS_BY_GROUP = "mb.op.perf.reports.groups.group.get";
    public static final String MSG_OP_PERF_REPORTS_GROUPS_BY_SCHOOL = "mb.op.perf.reports.groups.school.get";
    
    public static final String MSG_OP_COMPETENCY_REPORTS_GROUPS_BY_COUNTRY = "mb.op.competency.reports.groups.country.get";
    public static final String MSG_OP_COMPETENCY_REPORTS_GROUPS_BY_STATE = "mb.op.competency.reports.groups.state.get";
    public static final String MSG_OP_COMPETENCY_REPORTS_GROUPS_BY_GROUP = "mb.op.competency.reports.groups.group.get";
    public static final String MSG_OP_COMPETENCY_REPORTS_GROUPS_BY_SCHOOL = "mb.op.competency.reports.groups.school.get";
    
    public static final String MSG_OP_REPORTS_GET_CLASS_STUDENT_SUMMARY =
        "mb.op.reports.class.student.summary";

    private Message() {
      throw new AssertionError();
    }
  }

  public static final class Response {

    public static final String CLASS = "class";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TEACHER = "teacher";
    public static final String COURSE = "course";
    public static final String GRADE = "grade";
    public static final String CODE = "code";
    public static final String STUDENT = "student";
    public static final String STUDENTS = "students";
    public static final String PROFILE_IMAGE = "profileImage";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String ACTIVE_STUDENT_COUNT = "activeStudentCount";
    public static final String SUMMARY_DATA = "summaryData";
    public static final String WEEK_OF = "weekOf";
    public static final String ALL_TIME = "allTime";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String AS_ON = "asOn";
    public static final String MASTERED = "mastered";
    public static final String COMPLETED = "completed";
    public static final String INFERRED = "inferred";
    public static final String IN_PROGRESS = "inprogress";
    public static final String TOTAL_TIME_SPENT = "totalTimespent";
    public static final String SUGGESTIONS = "suggestions";
    public static final String INTERACTIONS = "interactions";
    public static final String COUNT = "count";
    public static final String AVERAGE_SCORE = "averageScore";
    public static final String TOTAL_MAX_SCORE = "totalMaxScore";
    public static final String CONTEXT = "context";
    public static final String CONTENT = "content";
    public static final String UNIT = "unit";
    public static final String LESSON = "lesson";
    public static final String SESSION_ID = "sessionId";
    public static final String DATE_OF_ACTIVITY = "dateOfActivity";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String COMPETENCIES = "competencies";
    public static final String SCORE = "score";
    public static final String MAX_SCORE = "maxScore";
    public static final String TIMESPENT = "timespent";
    public static final String GRADING_STATUS = "gradingStatus";
    public static final String USAGE_DATA = "usageData";
    public static final String SESSIONS_COUNT = "sessionsCount";
    public static final String LAST_ACCESSED = "lastAccessed";

    private Response() {
      throw new AssertionError();
    }
  }

  public static final class Params {

    public static final UUID NO_UUID = new UUID(0, 0);
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Params() {
      throw new AssertionError();
    }
  }

  public static final class Route {

    private static final String SEP = "/";
    private static final String COLON = ":";
    private static final String CLASS = "class";
    private static final String STUDENT = "student";
    private static final String SUMMARY = "summary";
    private static final String DETAILED = "detailed";
    private static final String GROUPS = "groups";
    private static final String ACTIVITIES = "activities";
    private static final String COUNTRIES = "countries";
    private static final String PERFORMANCE = "performance";
    private static final String COMPETENCY = "competency";
    private static final String STATES = "states";
    private static final String SCHOOLS = "schools";
    private static final String SUBJECTS = "subjects";
    private static final String API_GROUPS_BASE_ROUTE = "/api/:version/" + GROUPS;
    private static final String WEEKLY = "weekly";

    public static final String ID_CLASS = "classId";
    public static final String ID_GROUP = "groupId";
    public static final String ID_COUNTRY = "countryId";
    public static final String ID_STATE = "stateId";
    public static final String ID_SCHOOL = "schoolId";

    public static final String API_GROUP_TOKEN_VERIFICATION = API_GROUPS_BASE_ROUTE + SEP + "*";

    public static final String API_GROUP_UPDATE_DELETE = API_GROUPS_BASE_ROUTE + SEP + COLON + ID_GROUP;

    public static final String API_GROUP_ASSOCIATE =
        API_GROUPS_BASE_ROUTE + SEP + COLON + ID_GROUP + SEP + "associate" + SEP + ":associateGroupId";
    public static final String API_GROUP_REMOVE =
        API_GROUPS_BASE_ROUTE + SEP + COLON + ID_GROUP + SEP + "remove" + SEP + ":removeGroupId";

    // Groups report API routes
    // Scheduled / Unscheduled activities
    // /api/reports/v1/ca/classes/{class-id}/activities
    private static final String API_REPORTS_BASE_ROUTE = "/api/reports/:version/";
    public static final String API_REPORTS_TOKEN_VERIFICATION = API_REPORTS_BASE_ROUTE + "*";
    public static final String API_REPORTS_CA_ACTIVITIES_COUNT_GET =
        API_REPORTS_BASE_ROUTE + "ca/classes/" + COLON + ID_CLASS + SEP + ACTIVITIES;
    public static final String API_REPORTS_CLASS_SUMMARY_GET =
        API_REPORTS_BASE_ROUTE + CLASS + SEP + COLON + ID_CLASS + SEP + SUMMARY;
    public static final String API_REPORTS_CLASS_STUDENT_SUMMARY_WEEKLY_GET =
        API_REPORTS_BASE_ROUTE + CLASS + SEP + COLON + ID_CLASS + SEP + STUDENT + SEP + SUMMARY + SEP + WEEKLY;
    public static final String API_REPORTS_CLASS_STUDENT_DETAILED_SUMMARY_GET =
        API_REPORTS_BASE_ROUTE + CLASS + SEP + COLON + ID_CLASS + SEP + STUDENT + SEP + DETAILED
            + SEP + SUMMARY;
    public static final String API_REPORTS_CLASS_STUDENT_SUMMARY_GET =
        API_REPORTS_BASE_ROUTE + CLASS + SEP + COLON + ID_CLASS + SEP + STUDENT + SEP + SUMMARY;

    // Performance Reports
    public static final String API_FETCH_COUNTRIES = API_REPORTS_BASE_ROUTE + COUNTRIES;
    
    public static final String API_FETCH_PERF_SUBJECTS_BY_COUNTRY = API_REPORTS_BASE_ROUTE
        + PERFORMANCE + SEP + COUNTRIES + SEP + COLON + ID_COUNTRY + SEP + SUBJECTS;
        
    public static final String API_FETCH_PERF_REPORT_BY_COUNTRY =
        API_REPORTS_BASE_ROUTE + PERFORMANCE + SEP + COUNTRIES + SEP + COLON + ID_COUNTRY;
    
    public static final String API_FETCH_PERF_REPORT_BY_STATE = API_REPORTS_BASE_ROUTE + PERFORMANCE + SEP + COUNTRIES
        + SEP + COLON + ID_COUNTRY + SEP + STATES + SEP + COLON + ID_STATE;
    
    public static final String API_FETCH_PERF_REPORT_BY_GROUP =
        API_REPORTS_BASE_ROUTE + PERFORMANCE + SEP + GROUPS + SEP + COLON + ID_GROUP;
    
    public static final String API_FETCH_PERF_REPORT_BY_SCHOOL =
        API_REPORTS_BASE_ROUTE + PERFORMANCE + SEP + SCHOOLS + SEP + COLON + ID_SCHOOL;
    
    // Competency Count Reports
    public static final String API_FETCH_COMPETENCY_REPORT_BY_COUNTRY =
        API_REPORTS_BASE_ROUTE + COMPETENCY + SEP + COUNTRIES + SEP + COLON + ID_COUNTRY;

    public static final String API_FETCH_COMPETENCY_REPORT_BY_STATE =
        API_REPORTS_BASE_ROUTE + COMPETENCY + SEP + COUNTRIES + SEP + COLON + ID_COUNTRY + SEP
            + STATES + SEP + COLON + ID_STATE;

    public static final String API_FETCH_COMPETENCY_REPORT_BY_GROUP =
        API_REPORTS_BASE_ROUTE + COMPETENCY + SEP + GROUPS + SEP + COLON + ID_GROUP;

    public static final String API_FETCH_COMPETENCY_REPORT_BY_SCHOOL =
        API_REPORTS_BASE_ROUTE + COMPETENCY + SEP + SCHOOLS + SEP + COLON + ID_SCHOOL;
    
    private Route() {
      throw new AssertionError();
    }
  }

  public static final String CDN_URLS = "cdn_urls";
  public static final String USER_CDN_URL = "user_cdn_url";
  public static final String HTTP = "http";
  public static final String COLON = ":";
  public static final String PRODUCT_DOMAIN = "product.domain";
  
  private Constants() {
    throw new AssertionError();
  }
}
