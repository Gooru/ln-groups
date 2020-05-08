package org.gooru.groups.hierarchies.users;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.hierarchies.users.FetchUserGroupHierarchiesCommand.FetchUserGroupHierarchiesCommandBean;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.dbhelpers.core.groupacl.FetchUserGroupHierarchiesService;
import org.gooru.groups.reports.dbhelpers.core.groupacl.HierarchyModel;
import org.gooru.groups.reports.dbhelpers.core.groupacl.TenantHierarchyModel;
import org.gooru.groups.reports.dbhelpers.core.groupacl.TenantModel;
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
 * @author szgooru on 31-Mar-2020
 *
 */
public class FetchUserGroupHierarchiesProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(FetchUserGroupHierarchiesProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final FetchUserGroupHierarchiesService USER_GROUP_SERVICE =
      new FetchUserGroupHierarchiesService(DBICreator.getDbiForDefaultDS());

  public FetchUserGroupHierarchiesProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      String userId = ebMessage.getUserId().get().toString();
      String tenantId = ebMessage.getTenant().getString(CommandAttributeConstants.TENANT_ID);

      FetchUserGroupHierarchiesCommand command = FetchUserGroupHierarchiesCommand.build(userId);
      FetchUserGroupHierarchiesCommandBean bean = command.asBean();

      // Fetch user accessible tenant of the user
      Set<String> userAccessibleTenants =
          this.USER_GROUP_SERVICE.fetchUserAccessibleTenants(bean.getUserId());

      // Regardless of the user has access to any other tenant, we need to allow user to see the
      // data of his tenant.
      userAccessibleTenants.add(tenantId);

      // Fetch sub tenants of the user accessible tenants. It may be possible that user has access
      // to parent tenants. Then we need to fetch all of its sub tenants so that user can access
      // data of them
      Set<String> subtenants = this.USER_GROUP_SERVICE.fetchSubtenants(userAccessibleTenants);
      userAccessibleTenants.addAll(subtenants);

      // After resolving all tenant that user has access to, fetch the hierarchy mapping using the
      // tenants
      List<TenantHierarchyModel> tenantHierarchyModels =
          this.USER_GROUP_SERVICE.fetchTenantHierarchies(userAccessibleTenants);

      // If none of the tenant that user has access to has group hierarchy assigned then we need to
      // return Forbidden error. Because ideally user won't be able to see any data.
      if (tenantHierarchyModels == null || tenantHierarchyModels.isEmpty()) {
        LOGGER.warn("None of the tenant which user has access to has group hierarchy assigned");
        this.result.complete(MessageResponseFactory
            .createForbiddenResponse("none of the tenant has group hierarchy assigned"));
        return this.result;
      }

      Set<String> tenantIds = new HashSet<>();
      Set<Long> hierarchyIds = new HashSet<>();
      tenantHierarchyModels.forEach(model -> {
        tenantIds.add(model.getTenant());
        hierarchyIds.add(model.getHierarchyId());
      });

      // Fetch group hierarchy and tenant details to prepare the response
      Map<Long, HierarchyModel> hierarchyModels =
          this.USER_GROUP_SERVICE.fetchHierarchyDetails(hierarchyIds);
      Map<String, TenantModel> tenantModels = this.USER_GROUP_SERVICE.fetchTenantDetails(tenantIds);

      // Prepare response using all data
      FetchUserGroupHierarchiesResponseModel responseModel =
          FetchUserGroupHierarchiesResponseModelBuilder.build(tenantHierarchyModels,
              hierarchyModels, tenantModels);

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.error("exception while fetching user accessible groups", t);
      result.fail(t);
    }

    return this.result;
  }

}
