
package org.gooru.groups.reports.country.perf;

import java.util.List;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.auth.AuthorizerBuilder;
import org.gooru.groups.reports.dbhelpers.GroupReportService;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportByCountryModel;
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
public class GroupReportByCountryProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportByCountryProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService service = new GroupReportService(DBICreator.getDbiForDsdbDS());

  public GroupReportByCountryProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      
      // User role authorization
      AuthorizerBuilder.buildGroupReportAuthorizer(
          ebMessage.getSession().getString(Constants.Message.MSG_USER_ID)).authorize();
      
      // Build command object and validate input data
      GroupReportByCountryCommand command =
          GroupReportByCountryCommand.build(ebMessage.getRequestBody());
      GroupReportByCountryCommand.GroupReportByCountryCommandBean bean = command.asBean();
      
      // Extract tenant from the session
      JsonObject tenantJson =
          ebMessage.getSession().getJsonObject(Constants.Message.MSG_SESSION_TENANT);

      // Fetch performance and timespent report by country 
      List<PerformanceAndTSReportByCountryModel> report =
          this.service.fetchPerformanceAndTSReportByCountry(bean,
              tenantJson.getString(Constants.Message.MSG_SESSION_TENANT_ID));

      // Build the response models and complete result
      GroupReportByCountryResponseModel responseModel =
          GroupReportByCountryResponseModelBuilder.build(report);
      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class summary", t);
      result.fail(t);
    }

    return this.result;
  }

}
