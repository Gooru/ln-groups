package org.gooru.groups.routes.utils;

import org.gooru.groups.constants.Constants.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish on 23/2/18.
 */
public final class VerificationCompletionHelper {

  private VerificationCompletionHelper() {
    throw new AssertionError();
  }

  public static void setupUserContextInRoutingContext(RoutingContext routingContext,
      JsonObject session, String user) {
    routingContext.put(Message.MSG_KEY_SESSION, session).put(Message.MSG_USER_ID, user)
        .put(Message.SESSION_CONTEXT_TYPE, Message.SESSION_CONTEXT_TYPE_USER)
        .put(Message.SESSION_CONTEXT_SYSTEM_TENANT, Message.NO_VALUE);
  }

  public static void setupSystemContextInRoutingContext(RoutingContext routingContext,
      String tenant) {
    routingContext.put(Message.MSG_KEY_SESSION, new JsonObject())
        .put(Message.MSG_USER_ID, Message.NO_VALUE)
        .put(Message.SESSION_CONTEXT_TYPE, Message.SESSION_CONTEXT_TYPE_SYSTEM)
        .put(Message.SESSION_CONTEXT_SYSTEM_TENANT, tenant);
  }
}
