
package org.gooru.groups.reports.classes.student.summary;

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
public class ClassStudentSummaryReportProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(ClassStudentSummaryReportProcessor.class);
  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private ClassStudentSummaryService allTimeService =
      new ClassStudentSummaryService(DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForDsdbDS(),
          DBICreator.getDbiForAnalyticsDS());
  private ClassStudentSummaryForCustomDateService customDateService =
      new ClassStudentSummaryForCustomDateService(DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForDsdbDS(),
          DBICreator.getDbiForAnalyticsDS());

  public ClassStudentSummaryReportProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      ClassStudentSummaryCommand command = ClassStudentSummaryCommandBuilder.build(ebMessage);
      ClassStudentSummaryBean bean = new ClassStudentSummaryBean(command);

      JsonObject response = new JsonObject();
      if (bean.getDateTill() != null) {
        response = this.allTimeService.fetchClassStudentSummary(bean, ebMessage.getUserCdnUrl());
      } else if (bean.getFromDate() != null && bean.getToDate() != null) {
        response = this.customDateService.fetchClassStudentSummary(bean, ebMessage.getUserCdnUrl());
      }
      result.complete(MessageResponseFactory.createOkayResponse(response));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class student summary", t);
      result.fail(t);
    }

    return this.result;
  }

}
