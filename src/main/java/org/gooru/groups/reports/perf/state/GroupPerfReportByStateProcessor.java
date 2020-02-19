
package org.gooru.groups.reports.perf.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.auth.Authorizer;
import org.gooru.groups.reports.auth.AuthorizerBuilder;
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

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerfReportByStateProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService REPORT_SERVICE =
      new GroupReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupPerfReportByStateProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      GroupPerfReportByStateCommand command =
          GroupPerfReportByStateCommand.build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupPerfReportByStateCommand.GroupPerformanceReportByStateCommandBean bean =
          command.asBean();

      Authorizer userAuthorizer =
          AuthorizerBuilder.buildUserRoleAuthorizer(ebMessage.getUserId().get());
      userAuthorizer.authorize();

      Set<String> tenantIds = this.CORE_SERVICE.fetchSubTenants(bean.getTenantId());

      // Fetch all the groups falls below the state
      Map<Long, GroupModel> groupsByState = this.CORE_SERVICE.fetchGroupsByState(bean.getStateId());

      List<PerformanceAndTSReportByGroupModel> report = new ArrayList<>();
      if (command.getFrequency().equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_WEEKLY)) {
        report = userAuthorizer.isGlobalAccess()
            ? this.REPORT_SERVICE.fetchPerformanceAndTSWeekReportByState(groupsByState.keySet(),
                bean)
            : this.REPORT_SERVICE.fetchPerformanceAndTSWeekReportByStateAndTenant(
                groupsByState.keySet(), bean, tenantIds);
      } else {
        report = userAuthorizer.isGlobalAccess()
            ? this.REPORT_SERVICE.fetchPerformanceAndTSMonthReportByState(groupsByState.keySet(),
                bean)
            : this.REPORT_SERVICE.fetchPerformanceAndTSMonthReportByStateAndTenant(
                groupsByState.keySet(), bean, tenantIds);
      }

      GroupPerfReportByStateResponseModel responseModel =
          GroupPerfReportByStateResponseModelBuilder.build(report, groupsByState);

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching group performance report by state", t);
      result.fail(t);
    }

    return this.result;
  }

}
