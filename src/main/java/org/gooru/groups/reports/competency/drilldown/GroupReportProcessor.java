package org.gooru.groups.reports.competency.drilldown;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.competency.drilldown.GroupReportCommand.GroupReportCommandBean;
import org.gooru.groups.reports.competency.response.CompetencyReportResponseModel;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.groupacl.GroupACLService;
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
 * @author szgooru on 14-Feb-2020
 *
 */
public class GroupReportProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupReportProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService REPORT_SERVICE =
      new GroupReportService(DBICreator.getDbiForDsdbDS());

  private final GroupACLService GROUP_ACL_SERVICE =
      new GroupACLService(DBICreator.getDbiForDefaultDS());

  private final GroupHierarchyService GROUP_HIERARCHY_SERVICE =
      new GroupHierarchyService(DBICreator.getDbiForDefaultDS());

  private final CoreService CORE_SERVICE = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupReportProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      // Parse the event and create command object
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      GroupReportCommand command =
          GroupReportCommand.build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupReportCommandBean bean = command.asBean();

      // Fetch the hierarchy id of the tenant from the tenant setting
      Long hierarchyId =
          this.GROUP_HIERARCHY_SERVICE.fetchGroupHierarchyByTenant(bean.getTenantId());
      if (hierarchyId == null) {
        LOGGER.debug("there is no group hierarchy defined for the tenant {}", bean.getTenantId());
        String resultString = new ObjectMapper().writeValueAsString(populateEmptyResponse());
        result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
        return this.result;
      }

      // Fetch the group hierarchy by the hierarchy id.
      Node<GroupHierarchyDetailsModel> groupHierarchy =
          this.GROUP_HIERARCHY_SERVICE.fetchGroupHierarchyDetails(hierarchyId);

      // Fetch users group ACL. If there is no ACL found then return 403
      String userId = ebMessage.getUserId().get().toString();
      Map<String, JsonArray> userGroupACLMap = this.GROUP_ACL_SERVICE.fetchUserGroupACL(userId);

      if (userGroupACLMap == null || userGroupACLMap.isEmpty()) {
        LOGGER.debug("user '{}' does not have any group ACL defined", userId);
        result.complete(
            MessageResponseFactory.createForbiddenResponse("no group acl defined for user"));
        return result;
      }

      LOGGER.debug("group acl types found :{}", userGroupACLMap.size());
      List<GroupReportByCountryModel> report = new ArrayList<GroupReportByCountryModel>();

      // Fetch the groups accessible to the user based on the group hierarchy and user ACL
      JsonArray groups = fetchUserAccessibleGroups(userGroupACLMap, groupHierarchy);
      if (groups == null || groups.isEmpty()) {
        LOGGER.warn("user does not have access to any groups based on the ACL");
        result.complete(
            MessageResponseFactory.createForbiddenResponse("no group acl defined for user"));
        return result;
      }

      LOGGER.debug("user have access to '{}' groups at country level", groups.size());

      Set<Long> groupIds = new HashSet<>();
      if (groups.size() == 1) {
        String value = String.valueOf(groups.getValue(0));
        if (value.equalsIgnoreCase("*")) {
          report = this.REPORT_SERVICE.fetchCompetencyCounts(bean);
        } else {
          groupIds.add(Long.valueOf(value));
          report = this.REPORT_SERVICE.fetchCompetencyCountsByCountry(bean, groupIds);
        }
      } else {
        groups.forEach(group -> {
          groupIds.add(Long.valueOf(String.valueOf(group)));
        });
        report = this.REPORT_SERVICE.fetchCompetencyCountsByCountry(bean, groupIds);
      }
      
      LOGGER.debug("number of report records returned '{}'", report.size());

      Set<Long> countryIds = new HashSet<>();
      report.forEach(model -> {
        LOGGER.debug("countryID: {}", model.getCountryId());
        countryIds.add(model.getCountryId());
      });

      CompetencyReportResponseModel response = new GroupReportResponseModelBuilder().build(bean,
          report, CORE_SERVICE.fetchCountryDetails(countryIds));
      String resultString = new ObjectMapper().writeValueAsString(response);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by country", t);
      result.fail(t);
    }

    return this.result;
  }

  private String populateEmptyResponse() {
    return null;
  }

  private JsonArray fetchUserAccessibleGroups(Map<String, JsonArray> userGroupACLMap,
      Node<GroupHierarchyDetailsModel> groupHierarchy) {
    // check if user has access to root level group
    String groupType = groupHierarchy.getData().getType();
    LOGGER.debug("group type for which finding the ACLs '{}'", groupType);
   return userGroupACLMap.get(groupType);
  }

}