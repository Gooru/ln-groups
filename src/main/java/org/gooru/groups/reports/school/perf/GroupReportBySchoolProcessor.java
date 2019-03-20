
package org.gooru.groups.reports.school.perf;

import java.util.ArrayList;
import java.util.List;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.Constants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.auth.AuthorizerBuilder;
import org.gooru.groups.reports.dbhelpers.GroupReportService;
import org.gooru.groups.reports.dbhelpers.PerformanceAndTSReportBySchoolModel;
import org.gooru.groups.reports.dbhelpers.core.ClassTitleModel;
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
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupReportBySchoolProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportBySchoolProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService service = new GroupReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupReportBySchoolProcessor(Vertx vertx, Message<JsonObject> message) {
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

      GroupReportBySchoolCommand command =
          GroupReportBySchoolCommand.build(ebMessage.getRequestBody());
      GroupReportBySchoolCommand.GroupReportBySchoolCommandBean bean = command.asBean();

      // Extract tenant from the session
      JsonObject tenantJson =
          ebMessage.getSession().getJsonObject(Constants.Message.MSG_SESSION_TENANT);

      List<PerformanceAndTSReportBySchoolModel> report =
          this.service.fetchPerformanceAndTSReportBySchool(bean,
              tenantJson.getString(Constants.Message.MSG_SESSION_TENANT_ID));

      List<Integer> schoolIds = new ArrayList<>(1);
      schoolIds.add(bean.getSchoolId());

      // Fetch class ids from the report data
      List<String> classes = new ArrayList<>(report.size());
      report.forEach(model -> {
        classes.add(model.getClassId());
      });

      // Fetch the titles of the classes from core db
      List<ClassTitleModel> classTitles = this.coreService.fetchClassTitles(classes);

      // Build response
      GroupReportBySchoolResponseModel responseModel =
          GroupReportBySchoolResponseModelBuilder.build(report, classTitles);
      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class summary", t);
      result.fail(t);
    }

    return this.result;
  }

}
