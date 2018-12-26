package org.gooru.groups.responses.auth;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 20/2/18.
 */
public final class AuthSessionResponseHolderBuilder {

  public static AuthSessionResponseHolder build(Message<JsonObject> message) {
    return new AuthSessionMessageBusJsonResponseHolder(message);
  }

  private AuthSessionResponseHolderBuilder() {
    throw new AssertionError();
  }
}
