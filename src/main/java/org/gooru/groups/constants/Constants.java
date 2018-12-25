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

    public static final String MBUS_TIMEOUT = "event.bus.send.timeout.seconds";

    public static final String MBEP_FAIL_MSG_SESSION_PERSISTENCE =
        "mbus.fail.message.sessionpersistence";
    public static final int MBEP_FAIL_CODE_SESSION_PERSISTENCE = 1;

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

    private static final String API_BASE_ROUTE = "/api/groups/:version/";

    private Route() {
      throw new AssertionError();
    }
  }

  private Constants() {
    throw new AssertionError();
  }
}
