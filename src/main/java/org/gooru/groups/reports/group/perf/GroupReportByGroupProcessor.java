
package org.gooru.groups.reports.group.perf;

import java.util.List;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.auth.AuthorizerBuilder;
import org.gooru.groups.reports.dbhelpers.GroupReportService;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportByGroupModel;
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
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupReportByGroupProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportByGroupProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService service = new GroupReportService(DBICreator.getDbiForDsdbDS());

  public GroupReportByGroupProcessor(Vertx vertx, Message<JsonObject> message) {
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
      
      GroupReportByGroupCommand command =
          GroupReportByGroupCommand.build(ebMessage.getRequestBody());
      GroupReportByGroupCommand.GroupReportByGroupCommandBean bean = command.asBean();

      // Extract tenant from the session
      JsonObject tenantJson =
          ebMessage.getSession().getJsonObject(Constants.Message.MSG_SESSION_TENANT);

      List<PerformanceAndTSReportByGroupModel> report =
          this.service.fetchPerformanceAndTSReportByGroup(bean,
              tenantJson.getString(Constants.Message.MSG_SESSION_TENANT_ID));

      GroupReportByGroupResponseModel responseModel =
          GroupReportByGroupResponseModelBuilder.build(report);
      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class summary", t);
      result.fail(t);
    }

    return this.result;
  }

}
