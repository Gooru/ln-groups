package org.gooru.groups.reports.competency.drilldownreport;

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
import org.gooru.groups.reports.dbhelpers.core.groupacl.GroupACLResolver;
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
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru on 24-Apr-2020
 *
 */
public class GroupCompetencyDrilldownReportProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyDrilldownReportProcessor.class);

  private final GroupHierarchyService GROUP_HIERARCHY_SERVICE =
      new GroupHierarchyService(DBICreator.getDbiForDefaultDS());

  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  public GroupCompetencyDrilldownReportProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      // Parse the event and create command and bean objects. Incoming request data is validated in
      // the command.
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      GroupCompetencyDrilldownReportCommand command =
          GroupCompetencyDrilldownReportCommand.build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupCompetencyDrilldownReportCommand.GroupCompetencyDrilldownReportCommandBean bean = command.asBean();

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

      GroupACLResolver aclResolver = new GroupACLResolver(userId, bean.getHierarchyId());
      aclResolver.initUsersAllGroupACLs();
      aclResolver.verifyIfGroupIsAccessible(bean.getGroupId(), bean.getGroupType());

      List<DataModel> dataModels = new ArrayList<>();
      DataModel dataModel = new DrilldownDataComputationProcessor().processDrilldownComputation(
          bean.getGroupId(), bean.getGroupType(), bean.getMonth(), bean.getYear(),
          bean.getTenants(), aclResolver, groupHierarchy);
      dataModels.add(dataModel);

      GroupCompetencyReportResponseModel responseModel =
          GroupCompetencyReportResponseBuilder.build(prepareContextModel(bean), dataModels);

      String response = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(response)));

    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report for group", t);
      result.fail(t);
    }

    return this.result;
  }

  private ContextModel prepareContextModel(
      GroupCompetencyDrilldownReportCommand.GroupCompetencyDrilldownReportCommandBean bean) {
    ContextModel contextModel = new ContextModel();
    contextModel.setReport("competency");
    List<String> tenants = new ArrayList<>();
    for (Object o : bean.getTenants().getElements()) {
      tenants.add(o.toString());
    }
    contextModel.setHierarchy(bean.getHierarchyId());
    contextModel.setTenants(tenants);
    contextModel.setGroupId(bean.getGroupId());
    contextModel.setGroupType(bean.getGroupType());
    contextModel.setMonth(bean.getMonth());
    contextModel.setYear(bean.getYear());
    contextModel.setFrequency(bean.getFrequency());
    return contextModel;
  }

}
