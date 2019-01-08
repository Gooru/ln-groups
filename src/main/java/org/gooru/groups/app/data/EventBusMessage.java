
package org.gooru.groups.app.data;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.gooru.groups.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 07-Jan-2019
 */
public final class EventBusMessage {
  private final static Logger LOGGER = LoggerFactory.getLogger(EventBusMessage.class);
  private final String sessionToken;
  private final JsonObject requestBody;
  private final UUID userId;
  private final JsonObject session;
  private JsonObject readOnlySession;
  private JsonObject readOnlyRequestBody;

  public String getSessionToken() {
    return this.sessionToken;
  }

  public JsonObject getRequestBody() {
    return this.readOnlyRequestBody;
  }

  public Optional<UUID> getUserId() {
    if (Objects.equals(Constants.Message.NO_VALUE, Objects.toString(this.userId, null))) {
      return Optional.empty();
    }
    return Optional.of(this.userId);
  }

  public JsonObject getSession() {
    return this.readOnlySession;
  }

  private EventBusMessage(String sessionToken, JsonObject requestBody, UUID userId,
      JsonObject session) {
    this.sessionToken = sessionToken;
    this.requestBody = requestBody;
    this.userId = userId;
    this.session = session;
    this.readOnlyRequestBody = new JsonObject(Collections.unmodifiableMap(requestBody.getMap()));
    this.readOnlySession = new JsonObject(Collections.unmodifiableMap(session.getMap()));
  }

  public static EventBusMessage eventBusMessageBuilder(Message<JsonObject> message) {
    LOGGER.debug("message: {}", message.body().toString());
    String sessionToken = message.body().getString(Constants.Message.MSG_SESSION_TOKEN);
    String userId = message.body().getString(Constants.Message.MSG_USER_ID);
    JsonObject requestBody = message.body().getJsonObject(Constants.Message.MSG_HTTP_BODY);
    JsonObject session = message.body().getJsonObject(Constants.Message.MSG_KEY_SESSION);

    return new EventBusMessage(sessionToken, requestBody,
        userId != null ? UUID.fromString(userId) : null, session);
  }
}
