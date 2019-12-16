
package org.gooru.groups.reports.competency.school;

import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.responses.MessageResponse;
import org.gooru.groups.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportBySchoolProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportBySchoolProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  public GroupCompetencyReportBySchoolProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      String resultString = new ObjectMapper().writeValueAsString(null);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by school", t);
      result.fail(t);
    }

    return this.result;
  }

}
