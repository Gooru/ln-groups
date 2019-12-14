
package org.gooru.groups.reports.perf.state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.reports.perf.dbhelpers.GroupReportService;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByGroupModel;
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
public class GroupPerfReportByStateProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupPerfReportByStateProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService service = new GroupReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupPerfReportByStateProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      
      // User role authorization
      //AuthorizerBuilder.buildGroupReportAuthorizer(
      //    ebMessage.getSession().getString(Constants.Message.MSG_USER_ID)).authorize();
      
      GroupPerfReportByStateCommand command =
          GroupPerfReportByStateCommand.build(ebMessage.getRequestBody());
      GroupPerfReportByStateCommand.GroupPerformanceReportByStateCommandBean bean = command.asBean();

      // Extract tenant from the session
      //JsonObject tenantJson =
      //    ebMessage.getSession().getJsonObject(Constants.Message.MSG_SESSION_TENANT);

      List<PerformanceAndTSReportByGroupModel> report = new ArrayList<>();
      if (command.getFrequency().equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_WEEKLY)) {
        report = this.service.fetchPerformanceAndTSWeekReportByState(bean);
      } else {
        report = this.service.fetchPerformanceAndTSMonthReportByState(bean);
      }
      
      Set<Long> uniqueGroupIds = new HashSet<>();
      report.forEach(record -> {
        uniqueGroupIds.add(record.getGroupId());
      });
      
      Map<Long, GroupModel> groupModels = this.coreService.fetchGroupDetails(uniqueGroupIds);

      GroupPerfReportByStateResponseModel responseModel =
          GroupPerfReportByStateResponseModelBuilder.build(report, groupModels);

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class summary", t);
      result.fail(t);
    }

    return this.result;
  }

}
