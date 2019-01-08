
package org.gooru.groups.reports.ca;

import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.responses.MessageResponse;
import org.gooru.groups.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 07-Jan-2019
 */
public class ClassActivitiesCountProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(ClassActivitiesCountProcessor.class);
  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private ClassActivitiesCountService service =
      new ClassActivitiesCountService(DBICreator.getDbiForDefaultDS());

  public ClassActivitiesCountProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      LOGGER.debug("request body:{}", ebMessage.getRequestBody().toString());

      ClassActivitiesCountCommand command = ClassActivitiesCountCommandBuilder.build(ebMessage);
      ClassActivitiesCountBean bean = new ClassActivitiesCountBean(command);

      JsonObject response = this.service.fetchClassActivitiesCount(bean);
      result.complete(MessageResponseFactory.createOkayResponse(response));
    } catch (Throwable t) {
      LOGGER.error("exception while fetching class activities count", t);
      result.fail(t);
    }

    return this.result;
  }

}
