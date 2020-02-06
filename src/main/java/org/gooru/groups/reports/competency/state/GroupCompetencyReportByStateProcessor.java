
package org.gooru.groups.reports.competency.state;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.gooru.groups.app.components.AppConfiguration;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.responses.MessageResponse;
import org.gooru.groups.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportByStateProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportByStateProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupCompetencyReportByStateService reportService =
      new GroupCompetencyReportByStateService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupCompetencyReportByStateProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      // Prepare the command object to validate the inputs
      GroupCompetencyReportByStateCommand command = GroupCompetencyReportByStateCommand
          .build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupCompetencyReportByStateCommand.GroupCompetencyReportByStateCommandBean bean =
          command.asBean();

      UUID userId = ebMessage.getUserId().get();
      List<Integer> userRoles = coreService.fetchUserRoles(userId);
      if (userRoles == null || userRoles.isEmpty()) {
        LOGGER.warn("user {} does not have any role defined", userId.toString());
        result.complete(
            MessageResponseFactory.createForbiddenResponse("user does not have any roles defined"));
        return this.result;
      }

      boolean isGlobalAccess = false;
      JsonArray globalRoles = AppConfiguration.getInstance().getGlobalReportAccessRoles();
      for (Object role : globalRoles) {
        try {
          Integer roleId = (Integer) role;
          if (userRoles.contains(roleId)) {
            isGlobalAccess = true;
            break;
          }
        } catch (Exception ex) {
          LOGGER.warn("invalid role present in the configuration");
        }
      }

      // Fetch the sub tenants if the tenant is parent
      Set<String> tenantIds = this.coreService.fetchSubTenants(bean.getTenantId());

      // Fetch all the groups falls below the state
      Map<Long, GroupModel> groupsByState = this.coreService.fetchGroupsByState(bean.getStateId());

      // Fetch the reports for the groups
      List<GroupCompetencyReportByStateModel> weekReport = isGlobalAccess
          ? this.reportService.fetchGroupCompetencyReportByState(groupsByState.keySet(), bean)
          : this.reportService.fetchGroupCompetencyReportByStateAndTenant(groupsByState.keySet(),
              bean, tenantIds);
      List<GroupCompetencyGroupWiseReportByStateModel> groupWiseReport = isGlobalAccess
          ? this.reportService.fetchGroupCompetencyGroupWiseReportByState(groupsByState.keySet(),
              bean)
          : this.reportService.fetchGroupCompetencyGroupWiseReportByStateAndTenant(
              groupsByState.keySet(), bean, tenantIds);

      Double averagePerformance =
          isGlobalAccess ? this.reportService.fetchAveragePerformanceByState(bean)
              : this.reportService.fetchAveragePerformanceByStateAndTenant(bean, tenantIds);

      // Prepare response models
      GroupCompetencyReportByStateResponseModel responseModel =
          GroupCompetencyReportByStateResponseModelBuilder.build(weekReport, groupWiseReport,
              groupsByState, averagePerformance);

      // Send the response
      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));

    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by state", t);
      result.fail(t);
    }

    return this.result;
  }

}
