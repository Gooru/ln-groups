
package org.gooru.groups.reports.perf.school;

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
import org.gooru.groups.reports.dbhelpers.core.ClassModel;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.perf.dbhelpers.GroupReportService;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportBySchoolModel;
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
public class GroupPerfReportBySchoolProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerfReportBySchoolProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService REPORT_SERVICE =
      new GroupReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupPerfReportBySchoolProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      GroupPerfReportBySchoolCommand command =
          GroupPerfReportBySchoolCommand.build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupPerfReportBySchoolCommand.GroupPerfReportBySchoolCommandBean bean = command.asBean();

      Authorizer userAuthorizer =
          AuthorizerBuilder.buildUserRoleAuthorizer(ebMessage.getUserId().get());
      userAuthorizer.authorize();

      Set<String> tenantIds = this.CORE_SERVICE.fetchSubTenants(bean.getTenantId());

      List<PerformanceAndTSReportBySchoolModel> report = new ArrayList<>();
      if (bean.getFrequency().equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_WEEKLY)) {
        report = userAuthorizer.isGlobalAccess()
            ? this.REPORT_SERVICE.fetchPerformanceAndTSWeekReportBySchool(bean)
            : this.REPORT_SERVICE.fetchPerformanceAndTSWeekReportBySchoolAndTenant(bean, tenantIds);
      } else {
        report = userAuthorizer.isGlobalAccess()
            ? this.REPORT_SERVICE.fetchPerformanceAndTSMonthReportBySchool(bean)
            : this.REPORT_SERVICE.fetchPerformanceAndTSMonthReportBySchoolAndTenant(bean,
                tenantIds);
      }

      // Fetch the titles of the classes from core db
      Map<String, ClassModel> classDetails =
          this.CORE_SERVICE.fetchClassesBySchool(bean.getSchoolId());

      // Build response
      GroupPerfReportBySchoolResponseModel responseModel =
          GroupPerfReportBySchoolResponseModelBuilder.build(report, classDetails);
      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class summary", t);
      result.fail(t);
    }

    return this.result;
  }

}
