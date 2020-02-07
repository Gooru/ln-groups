
package org.gooru.groups.reports.perf.group;

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
import org.gooru.groups.reports.dbhelpers.core.DrilldownModel;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.reports.perf.dbhelpers.GroupReportService;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByClusterModel;
import org.gooru.groups.reports.perf.dbhelpers.PerformanceAndTSReportByGroupModel;
import org.gooru.groups.responses.MessageResponse;
import org.gooru.groups.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class GroupPerfReportByGroupProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerfReportByGroupProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService REPORT_SERVICE =
      new GroupReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupPerfReportByGroupProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      GroupPerfReportByGroupCommand command =
          GroupPerfReportByGroupCommand.build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean = command.asBean();

      Authorizer userAuthorizer =
          AuthorizerBuilder.buildUserRoleAuthorizer(ebMessage.getUserId().get());
      userAuthorizer.authorize();

      // fetch the group details to check for which type of group the data is requested for. For
      // district/school_distrinct and block the data will be fetched and grouped by the groups.
      // However for the cluster type of the group, data will be grouped by the schools because
      // school falls under cluster as per groups hierarchy
      GroupModel group = this.CORE_SERVICE.fetchGroupById(command.getGroupId());
      if (group.getSubType().equalsIgnoreCase(CommandAttributeConstants.GROUP_TYPE_SCHOOL_DISTRICT)
          || group.getSubType().equalsIgnoreCase(CommandAttributeConstants.GROUP_TYPE_CLUSTER)) {
        // Fetch group to school mapping
        Set<Long> schoolIds = this.CORE_SERVICE.fetchSchoolsByGroup(command.getGroupId());
        fetchReportBYCluster(schoolIds, bean, userAuthorizer);
      } else if (group.getSubType().equalsIgnoreCase(CommandAttributeConstants.GROUP_TYPE_DISTRICT)
          || group.getSubType().equalsIgnoreCase(CommandAttributeConstants.GROUP_TYPE_BLOCK)) {
        Map<Long, GroupModel> groupModels =
            this.CORE_SERVICE.fetchGroupsByParent(command.getGroupId());
        fetchReportByGroup(groupModels, bean, userAuthorizer);
      }
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching class summary", t);
      result.fail(t);
    }

    return this.result;
  }

  private void fetchReportBYCluster(Set<Long> schoolIds,
      GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean,
      Authorizer userAuthorizer) throws JsonProcessingException {
    List<PerformanceAndTSReportByClusterModel> report = new ArrayList<>();
    Set<String> tenantIds = this.CORE_SERVICE.fetchSubTenants(bean.getTenantId());
    if (bean.getFrequency().equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_WEEKLY)) {
      report = userAuthorizer.isGlobalAccess()
          ? this.REPORT_SERVICE.fetchPerformanceAndTSWeekReportBySDorCluster(schoolIds, bean)
          : this.REPORT_SERVICE.fetchPerformanceAndTSWeekReportBySDorClusterAndTenant(schoolIds,
              bean, tenantIds);
    } else {
      report = userAuthorizer.isGlobalAccess()
          ? this.REPORT_SERVICE.fetchPerformanceAndTSMonthReportBySDorCluster(schoolIds, bean)
          : this.REPORT_SERVICE.fetchPerformanceAndTSMonthReportBySDorClusterAndTenant(schoolIds,
              bean, tenantIds);
    }

    Map<Long, DrilldownModel> schoolModels = this.CORE_SERVICE.fetchSchoolDetails(schoolIds);
    GroupPerfReportByClusterResponseModel responseModel =
        GroupPerfReportByClusterResponseModelBuilder.build(report, schoolModels);
    String resultString = new ObjectMapper().writeValueAsString(responseModel);
    result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
  }

  private void fetchReportByGroup(Map<Long, GroupModel> groupModels,
      GroupPerfReportByGroupCommand.GroupPerfReportByGroupCommandBean bean,
      Authorizer userAuthorizer) throws JsonProcessingException {
    List<PerformanceAndTSReportByGroupModel> report = new ArrayList<>();
    Set<String> tenantIds = this.CORE_SERVICE.fetchSubTenants(bean.getTenantId());
    if (bean.getFrequency().equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_WEEKLY)) {
      report = userAuthorizer.isGlobalAccess()
          ? this.REPORT_SERVICE
              .fetchPerformanceAndTSWeekReportByDistrictOrBlock(groupModels.keySet(), bean)
          : this.REPORT_SERVICE.fetchPerformanceAndTSMonthReportByDistrictOrBlockAndTenant(
              groupModels.keySet(), bean, tenantIds);
    } else {
      report = userAuthorizer.isGlobalAccess()
          ? this.REPORT_SERVICE
              .fetchPerformanceAndTSMonthReportByDistrictOrBlock(groupModels.keySet(), bean)
          : this.REPORT_SERVICE.fetchPerformanceAndTSMonthReportByDistrictOrBlockAndTenant(
              groupModels.keySet(), bean, tenantIds);
    }

    GroupPerfReportByGroupResponseModel responseModel =
        GroupPerfReportByGroupResponseModelBuilder.build(report, groupModels);
    String resultString = new ObjectMapper().writeValueAsString(responseModel);
    result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
  }

}
