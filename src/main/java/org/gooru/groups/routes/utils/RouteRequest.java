package org.gooru.groups.routes.utils;

import java.util.Objects;
import org.gooru.groups.constants.Constants.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish on 20/2/18.
 */
public final class RouteRequest {

  private final JsonObject routeRequestBody;
  private final JsonObject httpBody;

  public RouteRequest(RoutingContext routingContext) {
    this.routeRequestBody = RouteRequestUtility.getBodyForMessage(routingContext);
    this.httpBody = this.routeRequestBody.getJsonObject(Message.MSG_HTTP_BODY);
  }

  public JsonObject getHttpBody() {
    return copyJsonObject(this.routeRequestBody.getJsonObject(Message.MSG_HTTP_BODY));
  }

  public JsonObject getSessionObject() {
    return copyJsonObject(this.routeRequestBody.getJsonObject(Message.MSG_KEY_SESSION));
  }

  public String getUserId() {
    return this.routeRequestBody.getString(Message.MSG_USER_ID);
  }

  public String getSessionToken() {
    return this.routeRequestBody.getString(Message.MSG_SESSION_TOKEN);
  }

  public String getRequestParamSingleValueForGetRequest(String param) {
    Objects.requireNonNull(param);
    JsonArray result = (JsonArray) this.httpBody.getValue(param);
    if (result != null && !result.isEmpty()) {
      return result.getString(0);
    }
    return null;
  }

  public JsonArray getRequestParamMultiValueForGetRequest(String param) {
    Objects.requireNonNull(param);
    return (JsonArray) this.httpBody.getValue(param);
  }

  public Object getRequestParamValue(String param) {
    Objects.requireNonNull(param);
    return this.httpBody.getValue(param);
  }

  private static JsonObject copyJsonObject(JsonObject input) {
    if (input == null || input.isEmpty()) {
      return new JsonObject();
    } else {
      return input.copy();
    }
  }

}
