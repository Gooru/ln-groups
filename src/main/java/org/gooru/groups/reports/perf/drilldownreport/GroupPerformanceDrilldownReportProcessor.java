package org.gooru.groups.reports.perf.drilldownreport;

import java.util.ArrayList;
import java.util.List;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.GroupConstants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.dbhelpers.core.groupacl.GroupACLResolver;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyDetailsModel;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.GroupHierarchyService;
import org.gooru.groups.reports.dbhelpers.core.hierarchy.Node;
import org.gooru.groups.reports.perf.common.ContextModel;
import org.gooru.groups.reports.perf.common.DataModel;
import org.gooru.groups.reports.perf.common.DataModelForClass;
import org.gooru.groups.reports.perf.common.GroupPerformanceDrilldownComputationForClassProcessor;
import org.gooru.groups.reports.perf.common.GroupPerformanceDrilldownComputationProcessor;
import org.gooru.groups.reports.perf.common.GroupPerformanceReportResponseBuilder;
import org.gooru.groups.reports.perf.common.GroupPerformanceReportResponseForClassModel;
import org.gooru.groups.reports.perf.common.GroupPerformanceReportResponseModel;
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
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceDrilldownReportProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupPerformanceDrilldownReportProcessor.class);

  private final GroupHierarchyService GROUP_HIERARCHY_SERVICE =
      new GroupHierarchyService(DBICreator.getDbiForDefaultDS());

  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  public GroupPerformanceDrilldownReportProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      // Parse the event and create command and bean objects. Incoming request data is validated in
      // the command.
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      GroupPerformanceDrilldownReportCommand command = GroupPerformanceDrilldownReportCommand
          .build(ebMessage.getRequestBody(), ebMessage.getTenant());
      GroupPerformanceDrilldownReportCommand.GroupPerformanceDrilldownReportCommandBean bean =
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

      String response = null;
      if (isChildNodeAreClasses) {
        List<DataModelForClass> dataModels = new ArrayList<>();
        DataModelForClass dataModel = new GroupPerformanceDrilldownComputationForClassProcessor()
            .processDrilldownComputation(bean.getGroupId(), bean.getGroupType(), bean.getMonth(),
                bean.getYear(), bean.getTenants(), aclResolver, groupHierarchy);
        dataModels.add(dataModel);
        GroupPerformanceReportResponseForClassModel responseModel =
            GroupPerformanceReportResponseBuilder.buildForClass(prepareContextModel(bean),
                dataModels);
        response = new ObjectMapper().writeValueAsString(responseModel);
      } else {
        List<DataModel> dataModels = new ArrayList<>();
        DataModel dataModel = new GroupPerformanceDrilldownComputationProcessor()
            .processDrilldownComputation(bean.getGroupId(), bean.getGroupType(), bean.getMonth(),
                bean.getYear(), bean.getTenants(), aclResolver, groupHierarchy);
        dataModels.add(dataModel);
        GroupPerformanceReportResponseModel responseModel =
            GroupPerformanceReportResponseBuilder.build(prepareContextModel(bean), dataModels);
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
      GroupPerformanceDrilldownReportCommand.GroupPerformanceDrilldownReportCommandBean bean) {
    ContextModel contextModel = new ContextModel();
    contextModel.setReport("performance");
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
