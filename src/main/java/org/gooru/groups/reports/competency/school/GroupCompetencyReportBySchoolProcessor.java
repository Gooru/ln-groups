
package org.gooru.groups.reports.competency.school;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.gooru.groups.app.components.AppConfiguration;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportBySchoolProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportBySchoolProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupCompetencyReportBySchoolService reportService =
      new GroupCompetencyReportBySchoolService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupCompetencyReportBySchoolProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      GroupCompetencyReportBySchoolCommand command = GroupCompetencyReportBySchoolCommand
          .build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupCompetencyReportBySchoolCommand.GroupCompetencyReportBySchoolCommandBean bean =
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

      List<GroupCompetencyReportBySchoolModel> competencyReportByWeek =
          isGlobalAccess ? this.reportService.fetchGroupCompetencyReportBySchool(bean)
              : this.reportService.fetchGroupCompetencyReportBySchoolAndTenant(bean, tenantIds);
      List<GroupCompetencyClassWiseReportBySchoolModel> competencyReportByClass =
          isGlobalAccess ? this.reportService.fetchGroupCompetencyClassWiseReportBySchool(bean)
              : this.reportService.fetchGroupCompetencyClassWiseReportBySchoolAndTenant(bean,
                  tenantIds);
      Double averagePerformance =
          isGlobalAccess ? this.reportService.fetchAveragePerformanceBySchool(bean)
              : this.reportService.fetchAveragePerformanceBySchoolAndTenant(bean, tenantIds);

      // Fetch the titles of the classes from core db
      Map<String, ClassModel> classDetails =
          this.coreService.fetchClassesBySchool(bean.getSchoolId());

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
