package org.gooru.groups.processors;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 20/2/18.
 */
public final class MessageProcessorBuilder {
  private MessageProcessorBuilder() {
    throw new AssertionError();
  }

  public static MessageProcessor buildProcessor(Vertx vertx, Message<JsonObject> message,
      String op) {
    switch (op) {
      // case MSG_OP_TENANT_PASSPHRASE_REFRESH:
      // return new TenantPassphraseRefreshProcessor(vertx, message);

      default:
        return null;
    }

  }

}
