package org.gooru.groups.processors;

import org.gooru.groups.constants.Constants;
import org.gooru.groups.reports.ca.ClassActivitiesCountProcessor;
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
      case Constants.Message.MSG_OP_REPORTS_GET_CA_ACTIVITIES_COUNT:
        return new ClassActivitiesCountProcessor(vertx, message);

      default:
        return null;
    }

  }

}
