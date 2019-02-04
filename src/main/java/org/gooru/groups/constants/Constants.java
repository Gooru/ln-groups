package org.gooru.groups.constants;

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

    private Message() {
      throw new AssertionError();
    }
  }

  public static final class Response {

    private Response() {
      throw new AssertionError();
    }
  }

  public static final class Params {

    public static final UUID NO_UUID = new UUID(0, 0);

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
    private static final String ACTIVITIES = "activities";
    private static final String API_GROUPS_BASE_ROUTE = "/api/groups/:version/";

    public static final String ID_CLASS = "classId";

    public static final String API_GROUP_TOKEN_VERIFICATION = API_GROUPS_BASE_ROUTE + "*";

    public static final String API_GROUP = API_GROUPS_BASE_ROUTE + "group";
    public static final String API_GROUP_UPDATE_DELETE = API_GROUP + SEP + ":groupId";

    public static final String API_GROUP_ASSOCIATE =
        API_GROUP + SEP + ":groupId" + SEP + "associate" + SEP + ":associateGroupId";
    public static final String API_GROUP_REMOVE =
        API_GROUP + SEP + ":groupId" + SEP + "remove" + SEP + ":removeGroupId";

    public static final String API_CLASS = API_GROUPS_BASE_ROUTE + "group";

    // Groups report API routes
    // Scheduled / Unscheduled activities
    // /api/reports/v1/ca/classes/{class-id}/activities
    private static final String API_REPORTS_BASE_ROUTE = "/api/reports/:version/";
    public static final String API_REPORTS_TOKEN_VERIFICATION = API_REPORTS_BASE_ROUTE + "*";
    public static final String API_REPORTS_CA_ACTIVITIES_COUNT_GET =
        API_REPORTS_BASE_ROUTE + "ca/classes/" + COLON + ID_CLASS + SEP + ACTIVITIES;
    public static final String API_REPORTS_CLASS_SUMMARY_GET =
        API_REPORTS_BASE_ROUTE + CLASS + SEP + COLON + ID_CLASS + SEP + SUMMARY;
    public static final String API_REPORTS_CLASS_STUDENT_SUMMARY_GET =
        API_REPORTS_BASE_ROUTE + CLASS + SEP + COLON + ID_CLASS + SEP + STUDENT + SEP + SUMMARY;
    public static final String API_REPORTS_CLASS_STUDENT_DETAILED_SUMMARY_GET =
        API_REPORTS_BASE_ROUTE + CLASS + SEP + COLON + ID_CLASS + SEP + STUDENT + SEP + DETAILED
            + SEP + SUMMARY;

    private Route() {
      throw new AssertionError();
    }
  }

  private Constants() {
    throw new AssertionError();
  }
}
