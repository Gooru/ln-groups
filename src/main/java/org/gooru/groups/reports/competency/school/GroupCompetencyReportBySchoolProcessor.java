
package org.gooru.groups.reports.competency.school;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.competency.dbhelpers.GroupCompetencyReportService;
import org.gooru.groups.reports.dbhelpers.core.ClassModel;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
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
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportBySchoolProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportBySchoolProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupCompetencyReportService reportService =
      new GroupCompetencyReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupCompetencyReportBySchoolProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      GroupCompetencyReportBySchoolCommand command =
          GroupCompetencyReportBySchoolCommand.build(ebMessage.getRequestBody());
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean =
          command.asBean();

      List<GroupCompetencyReportBySchoolModel> competencyReportByWeek =
          this.reportService.fetchGroupCompetencyReportBySchool(bean);
      List<GroupCompetencyClassWiseReportBySchoolModel> competencyReportByClass =
          this.reportService.fetchGroupCompetencyClassWiseReportBySchool(bean);

      Set<String> classIds = new HashSet<>();
      competencyReportByClass.forEach(model -> {
        classIds.add(model.getClassId());
      });

      // Fetch the titles of the classes from core db
      Map<String, ClassModel> classDetails = this.coreService.fetchClassDetails(classIds);
      Double averagePerformance = this.reportService.fetchAveragePerformanceBySchool(bean);

      GroupCompetencyReportBySchoolResponseModel responseModel =
          GroupCompetencyReportBySchoolResponseModelBuilder.build(competencyReportByWeek,
              competencyReportByClass, classDetails, averagePerformance);

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by school", t);
      result.fail(t);
    }

    return this.result;
  }

}
