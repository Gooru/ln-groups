
package org.gooru.groups.reports.competency.country;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.gooru.groups.app.components.AppConfiguration;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryResponseModel.OverallStats;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.DrilldownModel;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyDetailsModel;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyService;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.Node;
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

  private final GroupCompetencyReportByCountryService reportService =
      new GroupCompetencyReportByCountryService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());
  private final GroupHierarchyService groupHierarchyService =
      new GroupHierarchyService(DBICreator.getDbiForDefaultDS());

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

      Long hierarchyId = null;

      Node<GroupHierarchyDetailsModel> groupHierarchy =
          this.groupHierarchyService.fetchGroupHierarchyDetails(hierarchyId);

      GroupCompetencyReportByCountryResponseModel responseModel = null;
      Node<GroupHierarchyDetailsModel> rootNode = groupHierarchy.getRoot();
      if (rootNode.getData().getType().equalsIgnoreCase("country")) {
        Node<GroupHierarchyDetailsModel> childNode = rootNode.getChild();
        if (childNode.getData().getType().equalsIgnoreCase("state")) {
          responseModel = fetchStateWiseReport(isGlobalAccess, bean);
        } else if (childNode.getData().getType().equalsIgnoreCase("school")) {
          responseModel = fetchSchoolWiseReport(isGlobalAccess, bean);
        } else {
          LOGGER.debug("unknown type of child element in the hierarchy, returning empty");
          responseModel = populateEmptyResponse();
        }
      } else {
        // Country is not a parent node in the group hierarchy, so we are not sure about what type
        // of hierarchy tenant is following. Return emopty response
        LOGGER.debug("root of group hierarchy is not country, returning empty");
        responseModel = populateEmptyResponse();
      }

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by country", t);
      result.fail(t);
    }

    return this.result;
  }

  private GroupCompetencyReportByCountryResponseModel populateEmptyResponse() {
    GroupCompetencyReportByCountryResponseModel responseModel =
        new GroupCompetencyReportByCountryResponseModel();
    responseModel.setData(new ArrayList<GroupCompetencyReportByCountryResponseModel.Data>());
    responseModel
        .setDrilldown(new ArrayList<GroupCompetencyReportByCountryResponseModel.Drilldown>());
    responseModel.setOverallStats(new OverallStats());
    return responseModel;
  }


  private GroupCompetencyReportByCountryResponseModel fetchStateWiseReport(boolean isGlobalAccess,
      GroupCompetencyReportByCountryCommandBean bean) {
    LOGGER.debug("fetching the competency report data by week and state");
    Set<String> tenantIds = this.coreService.fetchSubTenants(bean.getTenantId());
    List<GroupCompetencyReportByCountryModel> competencyReportByWeek =
        isGlobalAccess ? this.reportService.fetchGroupCompetencyReportByCountry(bean)
            : this.reportService.fetchGroupCompetencyReportByCountryAndTenant(bean, tenantIds);

    List<GroupCompetencyDrilldownReportByCountryModel> competencyReportByState = isGlobalAccess
        ? this.reportService.fetchGroupCompetencyStateWiseReportByCountry(bean)
        : this.reportService.fetchGroupCompetencyStateWiseReportByCountryAndTenant(bean, tenantIds);

    Map<Long, DrilldownModel> states = this.coreService.fetchStatesByCountry(bean.getCountryId());
    Double averagePerformance =
        isGlobalAccess ? this.reportService.fetchAveragePerformanceByCountry(bean)
            : this.reportService.fetchAveragePerformanceByCountryAndTenant(bean, tenantIds);

    LOGGER.debug("prepare the response model");
    return new GroupCompetencyReportByCountryResponseModelBuilder().build(competencyReportByWeek,
        competencyReportByState, states, averagePerformance, "state");
  }

  private GroupCompetencyReportByCountryResponseModel fetchSchoolWiseReport(boolean isGlobalAccess,
      GroupCompetencyReportByCountryCommandBean bean) {
    LOGGER.debug("fetching the competency report data by week and school");
    Set<String> tenantIds = this.coreService.fetchSubTenants(bean.getTenantId());
    List<GroupCompetencyReportByCountryModel> competencyReportByWeek =
        isGlobalAccess ? this.reportService.fetchGroupCompetencyReportByCountry(bean)
            : this.reportService.fetchGroupCompetencyReportByCountryAndTenant(bean, tenantIds);

    List<GroupCompetencyDrilldownReportByCountryModel> competencyReportBySchool =
        isGlobalAccess ? this.reportService.fetchGroupCompetencySchoolWiseReportByCountry(bean)
            : this.reportService.fetchGroupCompetencySchoolWiseReportByCountryAndTenant(bean,
                tenantIds);

    Set<Long> schoolIds = this.coreService.fetchSchoolsByCountry(bean.getCountryId());
    Map<Long, DrilldownModel> schools = this.coreService.fetchSchoolDetails(schoolIds);
    Double averagePerformance =
        isGlobalAccess ? this.reportService.fetchAveragePerformanceByCountry(bean)
            : this.reportService.fetchAveragePerformanceByCountryAndTenant(bean, tenantIds);

    LOGGER.debug("prepare the response model");
    return new GroupCompetencyReportByCountryResponseModelBuilder().build(competencyReportByWeek,
        competencyReportBySchool, schools, averagePerformance, "school");
  }

}
