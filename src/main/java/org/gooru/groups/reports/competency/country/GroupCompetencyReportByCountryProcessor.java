
package org.gooru.groups.reports.competency.country;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.gooru.groups.app.components.AppConfiguration;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.competency.dbhelpers.GroupCompetencyReportService;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.StateModel;
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
 * @author szgooru Created On 14-Dec-2019
 */
public class GroupCompetencyReportByCountryProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportByCountryProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupCompetencyReportService reportService =
      new GroupCompetencyReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupCompetencyReportByCountryProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      LOGGER.debug("prepare the command model for the competency report");
      GroupCompetencyReportByCountryCommand command = GroupCompetencyReportByCountryCommand
          .build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean =
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

      LOGGER.debug("fetching the competency report data by week and state");
      List<GroupCompetencyReportByCountryModel> competencyReportByWeek =
          isGlobalAccess ? this.reportService.fetchGroupCompetencyReportByCountry(bean)
              : this.reportService.fetchGroupCompetencyReportByCountryAndTenant(bean);
      List<GroupCompetencyStateWiseReportByCountryModel> competencyReportByState =
          isGlobalAccess ? this.reportService.fethcGroupCompetencyStateWiseReportByCountry(bean)
              : this.reportService.fethcGroupCompetencyStateWiseReportByCountryAndTenant(bean);

      Set<Long> stateIds = new HashSet<>();
      competencyReportByState.forEach(model -> {
        stateIds.add(model.getStateId());
      });
      Map<Long, StateModel> states = this.coreService.fetchStateDetails(stateIds);
      Double averagePerformance =
          isGlobalAccess ? this.reportService.fetchAveragePerformanceByCountry(bean)
              : this.reportService.fetchAveragePerformanceByCountryAndTenant(bean);

      LOGGER.debug("prepare the response model");
      GroupCompetencyReportByCountryResponseModel responseModel =
          new GroupCompetencyReportByCountryResponseModelBuilder().build(competencyReportByWeek,
              competencyReportByState, states, averagePerformance);

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by country", t);
      result.fail(t);
    }

    return this.result;
  }

}
