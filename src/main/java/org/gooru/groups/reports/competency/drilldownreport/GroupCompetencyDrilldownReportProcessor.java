package org.gooru.groups.reports.competency.drilldownreport;

import java.util.ArrayList;
import java.util.List;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.GroupConstants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.competency.common.ContextModel;
import org.gooru.groups.reports.competency.common.DataModel;
import org.gooru.groups.reports.competency.common.DataModelForClass;
import org.gooru.groups.reports.competency.common.DrilldownDataComputationForSchoolProcessor;
import org.gooru.groups.reports.competency.common.DrilldownDataComputationProcessor;
import org.gooru.groups.reports.competency.common.GroupCompetencyReportResponseBuilder;
import org.gooru.groups.reports.competency.common.GroupCompetencyReportResponseForClassModel;
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
      GroupCompetencyDrilldownReportCommand command = GroupCompetencyDrilldownReportCommand
          .build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupCompetencyDrilldownReportCommand.GroupCompetencyDrilldownReportCommandBean bean =
          command.asBean();

      // Fetch the all levels of the group hierarchy by the hierarchy id. This is self referential
      // object contains the reference to its child nodes. This hierarchy will always start with
      // root node.
      Node<GroupHierarchyDetailsModel> groupHierarchy =
          this.GROUP_HIERARCHY_SERVICE.fetchGroupHierarchyDetails(command.getHierarchyId());

      String userId = ebMessage.getUserId().get().toString();

      // Initiate the group ACLs for the user and group hierarchy and verify whether user has access
      // to requested group id and type. These methods should sufficient to return appropriate error
      // if user does not have access
      GroupACLResolver aclResolver =
          new GroupACLResolver(userId, bean.getHierarchyId(), bean.getTenants());
      aclResolver.initUsersAllGroupACLs();
      aclResolver.verifyIfGroupIsAccessible(bean.getGroupId(), bean.getGroupType());

      // Check if the group type for which the data has been request contain classes as child nodes.
      // Because it will need special handling in further processing.
      boolean isChildNodeAreClasses =
          checkIfChildNodesAreClasses(groupHierarchy, command.getGroupType());

      // Here we need special handling if the data is request for such group which has child nodes
      // as classes. It is just because class id is String and group id is Long, the data structures
      // we are using need to be differentiated. Otherwise there is no difference in the computation
      // logic
      String response = null;
      if (isChildNodeAreClasses) {
        List<DataModelForClass> dataModels = new ArrayList<>();
        DataModelForClass dataModel = new DrilldownDataComputationForSchoolProcessor()
            .processDrilldownComputationForClasses(bean.getGroupId(), bean.getGroupType(),
                bean.getMonth(), bean.getYear(), bean.getTenants(), aclResolver, groupHierarchy);
        dataModels.add(dataModel);

        GroupCompetencyReportResponseForClassModel responseModel =
            GroupCompetencyReportResponseBuilder.buildForClasses(prepareContextModel(bean),
                dataModels);
        response = new ObjectMapper().writeValueAsString(responseModel);
      } else {
        List<DataModel> dataModels = new ArrayList<>();
        DataModel dataModel = new DrilldownDataComputationProcessor().processDrilldownComputation(
            bean.getGroupId(), bean.getGroupType(), bean.getMonth(), bean.getYear(),
            bean.getTenants(), aclResolver, groupHierarchy);
        dataModels.add(dataModel);

        GroupCompetencyReportResponseModel responseModel =
            GroupCompetencyReportResponseBuilder.build(prepareContextModel(bean), dataModels);

        response = new ObjectMapper().writeValueAsString(responseModel);
      }

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

  /**
   * This methods checks that does requested group has class as child nodes.
   * 
   * @param groupHierarchy
   * @param requestedGroupType
   * @return
   */
  private boolean checkIfChildNodesAreClasses(Node<GroupHierarchyDetailsModel> groupHierarchy,
      String requestedGroupType) {
    boolean childNodesAreClasses = false;

    Node<GroupHierarchyDetailsModel> currentNode = groupHierarchy;
    Node<GroupHierarchyDetailsModel> requestedNode = null;
    while (!currentNode.isLeaf()) {
      currentNode = currentNode.getChild();
      if (currentNode.getData().getType().equalsIgnoreCase(requestedGroupType)) {
        requestedNode = currentNode;
      }
    }

    if (requestedNode != null) {
      String childGroupType = requestedNode.getChild().getData().getType();
      if (childGroupType.equalsIgnoreCase(GroupConstants.LEVEL_CLASS)) {
        childNodesAreClasses = true;
      }
    }

    return childNodesAreClasses;
  }

}
