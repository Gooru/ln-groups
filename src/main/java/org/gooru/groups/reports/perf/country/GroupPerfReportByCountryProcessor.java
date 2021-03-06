
package org.gooru.groups.reports.perf.country;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.StateModel;
import org.gooru.groups.reports.perf.dbhelpers.GroupReportService;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByCountryModel;
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
 * @author szgooru Created On 15-Mar-2019
 */
public class GroupPerfReportByCountryProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerfReportByCountryProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService service = new GroupReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupPerfReportByCountryProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      // Build command object and validate input data
      GroupPerfReportByCountryCommand command =
          GroupPerfReportByCountryCommand.build(ebMessage.getRequestBody());
      GroupPerfReportByCountryCommand.GroupReportByCountryCommandBean bean = command.asBean();

      // Fetch performance and timespent report by country
      List<PerformanceAndTSReportByCountryModel> report = new ArrayList<>();
      if (command.getFrequency().equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_WEEKLY)) {
        report = this.service.fetchPerformanceAndTSWeekReportByCountry(bean);
      } else {
        report = this.service.fetchPerformanceAndTSMonthReportByCountry(bean);
      }

      Map<Long, StateModel> states = this.coreService.fetchStatesByCountry(bean.getCountryId());

      // Build the response models and complete result
      GroupPerfReportByCountryResponseModel responseModel =
          GroupPerfReportByCountryResponseModelBuilder.build(report, states);
      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class summary", t);
      result.fail(t);
    }

    return this.result;
  }

}
