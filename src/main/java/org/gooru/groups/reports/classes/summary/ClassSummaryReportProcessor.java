
package org.gooru.groups.reports.classes.summary;

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
 * @author renuka
 */
public class ClassSummaryReportProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(ClassSummaryReportProcessor.class);
  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private ClassSummaryService service = new ClassSummaryService(DBICreator.getDbiForDefaultDS(),
      DBICreator.getDbiForDsdbDS(), DBICreator.getDbiForAnalyticsDS());

  public ClassSummaryReportProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      LOGGER.debug("request body:{}", ebMessage.getRequestBody().toString());

      ClassSummaryCommand command = WeeklyClassSummaryCommandBuilder.build(ebMessage);
      ClassSummaryBean bean = new ClassSummaryBean(command);

      JsonObject response = this.service.fetchClassSummary(bean);
      result.complete(MessageResponseFactory.createOkayResponse(response));
    } catch (Throwable t) {
      LOGGER.error("exception while fetching class activities count", t);
      result.fail(t);
    }

    return this.result;
  }

}
