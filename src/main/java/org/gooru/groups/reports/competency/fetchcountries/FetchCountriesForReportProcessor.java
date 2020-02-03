
package org.gooru.groups.reports.competency.fetchcountries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.gooru.groups.app.components.AppConfiguration;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.competency.dbhelpers.GroupCompetencyReportService;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
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
 * @author szgooru Created On 18-Mar-2019
 */
public class FetchCountriesForReportProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(FetchCountriesForReportProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());
  private final GroupCompetencyReportService GROUP_REPORT_SERVICE =
      new GroupCompetencyReportService(DBICreator.getDbiForDsdbDS());

  public FetchCountriesForReportProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      FetchCountriesForReportCommand command =
          FetchCountriesForReportCommand.build(ebMessage.getTenant());

      UUID userId = ebMessage.getUserId().get();
      List<Integer> userRoles = CORE_SERVICE.fetchUserRoles(userId);
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
          Integer roleId = Integer.parseInt((String) role);
          if (userRoles.contains(roleId)) {
            isGlobalAccess = true;
            break;
          }
        } catch (NumberFormatException nfe) {
          LOGGER.warn("invalid role present in the configuration");
        }
      }

      List<FetchCountriesForReportModel> competencyCounts = null;
      if (isGlobalAccess) {
        // Fetch data for all tenants
        competencyCounts = GROUP_REPORT_SERVICE.fetchCompetencyCounts(command.asBean());
      } else {
        // Filter by tenant
        competencyCounts = GROUP_REPORT_SERVICE.fetchCompetencyCountsByTenant(command.asBean());
      }

      Set<Long> countryIds = new HashSet<>();
      competencyCounts.forEach(model -> {
        countryIds.add(model.getCountryId());
      });

      FetchCountriesForReportResponseModel responseModel =
          new FetchCountriesForReportResponseModelBuilder().build(competencyCounts,
              CORE_SERVICE.fetchCountryDetails(countryIds));
      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by group", t);
      result.fail(t);
    }

    return this.result;
  }

}
