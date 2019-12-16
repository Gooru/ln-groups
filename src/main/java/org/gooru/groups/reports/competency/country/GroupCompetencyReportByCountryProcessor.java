
package org.gooru.groups.reports.competency.country;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
      GroupCompetencyReportByCountryCommand command =
          GroupCompetencyReportByCountryCommand.build(ebMessage.getRequestBody());
      GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean =
          command.asBean();

      LOGGER.debug("fetching the competency report data by week and state");
      List<GroupCompetencyReportByCountryModel> competencyReportByWeek =
          this.reportService.fetchGroupCompetencyReportByCountry(bean);
      List<GroupCompetencyStateWiseReportByCountryModel> competencyReportByState =
          this.reportService.fethcGroupCompetencyStateWiseReportByCountry(bean);

      Set<Long> uniqueStateIds = new HashSet<>();
      competencyReportByState.forEach(record -> {
        uniqueStateIds.add(record.getStateId());
      });
      Map<Long, StateModel> states = this.coreService.fetchStateDetails(uniqueStateIds);

      LOGGER.debug("prepare the response model");
      GroupCompetencyReportByCountryResponseModel responseModel =
          GroupCompetencyReportByCountryResponseModelBuilder.build(competencyReportByWeek,
              competencyReportByState, states);

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class summary", t);
      result.fail(t);
    }

    return this.result;
  }

}
