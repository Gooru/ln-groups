package org.gooru.groups.routes.utils;

import java.util.Objects;
import org.gooru.groups.constants.Constants.Message;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by ashish on 10/1/18.
 */
public final class RouteRequestUtility {
  private RouteRequestUtility() {
    throw new AssertionError();
  }

  /*
   * If the incoming request is POST or PUT, it is expected to have a payload of JSON which is
   * returned. In case of GET request, any query parameters will be used to create a JSON body. Note
   * that only query string is used and not path matchers. In case of no query parameters send out
   * empty Json object, but don't send null
   */
  public static JsonObject getBodyForMessage(RoutingContext routingContext) {
    return getBodyForMessage(routingContext, false);
  }

  public static JsonObject getBodyForMessage(RoutingContext routingContext,
      boolean withPathParams) {
    JsonObject result = new JsonObject();
    result.put(Message.MSG_HTTP_BODY, getHttpBody(routingContext, withPathParams));
    result.put(Message.MSG_KEY_SESSION, getSession(routingContext));
    result.put(Message.MSG_USER_ID, getUserId(routingContext));
    result.put(Message.MSG_SESSION_TOKEN, getSessionToken(routingContext));
    return result;
  }

  private static String getSessionToken(RoutingContext routingContext) {
    return (String) routingContext.get(Message.MSG_SESSION_TOKEN);
  }

  private static String getUserId(RoutingContext routingContext) {
    return (String) routingContext.get(Message.MSG_USER_ID);
  }

  private static JsonObject getSession(RoutingContext routingContext) {
    return (JsonObject) routingContext.get(Message.MSG_KEY_SESSION);
  }

  private static JsonObject getHttpBody(RoutingContext routingContext, boolean withParams) {
    JsonObject httpBody;
    if (requestIsPostOrPut(routingContext)) {
      httpBody = routingContext.getBodyAsJson();
      if (withParams) {
        applyParams(routingContext, httpBody);
      }
    } else {
      httpBody = new JsonObject();
      if (withParams) {
        applyParams(routingContext, httpBody);
      }
    }
    return httpBody;
  }

  private static void applyParams(RoutingContext routingContext, JsonObject httpBody) {
    MultiMap params = routingContext.request().params();
    for (String paramName : params.names()) {
      httpBody.put(paramName, params.get(paramName));
    }
  }

  private static boolean requestIsGet(RoutingContext routingContext) {
    return Objects.equals(routingContext.request().method().name(), HttpMethod.GET.name());
  }

  private static boolean requestIsPostOrPut(RoutingContext routingContext) {
    return routingContext.request().method().name().equals(HttpMethod.POST.name())
        || routingContext.request().method().name().equals(HttpMethod.PUT.name());
  }
}
