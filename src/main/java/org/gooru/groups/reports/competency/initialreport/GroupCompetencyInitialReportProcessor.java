package org.gooru.groups.reports.competency.initialreport;

import java.util.ArrayList;
import java.util.List;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.competency.common.ContextModel;
import org.gooru.groups.reports.competency.common.DataModel;
import org.gooru.groups.reports.competency.common.DrilldownDataComputationProcessor;
import org.gooru.groups.reports.competency.common.GroupCompetencyReportResponseBuilder;
import org.gooru.groups.reports.competency.common.GroupCompetencyReportResponseModel;
import org.gooru.groups.reports.competency.initialreport.GroupCompetencyInitialReportCommand.GroupCompetencyInitialReportCommandBean;
import org.gooru.groups.reports.dbhelpers.core.groupacl.GroupACLModel;
import org.gooru.groups.reports.dbhelpers.core.groupacl.GroupACLResolver;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyDetailsModel;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyService;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.Node;
import org.gooru.groups.reports.dbhelpers.core.validator.RequestDBValidator;
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
public class GroupCompetencyInitialReportProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyInitialReportProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupHierarchyService GROUP_HIERARCHY_SERVICE =
      new GroupHierarchyService(DBICreator.getDbiForDefaultDS());

  public GroupCompetencyInitialReportProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      // Parse the event and create command and bean objects. Incoming request data is validated in
      // the command.
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      GroupCompetencyInitialReportCommand command = GroupCompetencyInitialReportCommand
          .build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupCompetencyInitialReportCommandBean bean = command.asBean();

      // TODO: Implement the DB validator for the existence cheek of the hierarchy and tenant to
      // ensure that the request contains valid data

      // Fetch the all levels of the group hierarchy by the hierarchy id. This is self referential
      // object contains the reference to its child nodes. This hierarchy will always start with
      // root node.
      Node<GroupHierarchyDetailsModel> groupHierarchy =
          this.GROUP_HIERARCHY_SERVICE.fetchGroupHierarchyDetails(command.getHierarchyId());

      // Fetch all group ACL of the user as the purpose if this API is to return the first level in
      // the hierarchy for which user has access to. It may not be root always. User's access may
      // start at any level in the group hierarchy.
      // If there is no ACL found then return 403.
      String userId = ebMessage.getUserId().get().toString();

      // Validate incoming data to check the tenant and hierarchy mappings
      RequestDBValidator dbValidator = new RequestDBValidator();
      dbValidator.verifyTenantAccess(userId, bean.getTenantId(), bean.getTenants());
      dbValidator.validateTenantHierarchyMapping(bean.getHierarchyId(), bean.getTenants());

      GroupACLResolver aclResolver =
          new GroupACLResolver(userId, bean.getHierarchyId(), bean.getTenants());
      aclResolver.initFiltereGroupACLs();

      // This is the root level based on the user group ACL for which we need to report the data.
      GroupACLModel rootModel = aclResolver.getRootModel();

      List<DataModel> dataModels = new ArrayList<>();
      JsonArray rootGroupIds = rootModel.getGroups();
      for (Object rGroupId : rootGroupIds) {
        Long groupId = Long.valueOf(rGroupId.toString());
        DataModel dataModel = new DrilldownDataComputationProcessor().processDrilldownComputation(
            groupId, rootModel.getType(), bean.getMonth(), bean.getYear(), bean.getTenants(),
            aclResolver, groupHierarchy);
        dataModels.add(dataModel);
      }

      GroupCompetencyReportResponseModel responseModel = GroupCompetencyReportResponseBuilder
          .build(prepareContextModel(bean, rootModel.getType()), dataModels);

      String response = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(response)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by country", t);
      result.fail(t);
    }

    return this.result;
  }

  private ContextModel prepareContextModel(
      GroupCompetencyInitialReportCommand.GroupCompetencyInitialReportCommandBean bean,
      String groupType) {
    ContextModel contextModel = new ContextModel();
    contextModel.setReport("competency");
    contextModel.setHierarchy(bean.getHierarchyId());
    contextModel.setTenants(bean.getTenants());
    contextModel.setGroupId(null);
    contextModel.setGroupType(groupType);
    contextModel.setMonth(bean.getMonth());
    contextModel.setYear(bean.getYear());
    contextModel.setFrequency(null);
    return contextModel;
  }

}
