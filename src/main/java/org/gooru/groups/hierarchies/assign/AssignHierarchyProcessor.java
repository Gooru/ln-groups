package org.gooru.groups.hierarchies.assign;

import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.responses.MessageResponse;
import org.gooru.groups.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class AssignHierarchyProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(AssignHierarchyProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final AssignHierarchyService ASSIGN_SERVICE =
      new AssignHierarchyService(DBICreator.getDbiForDefaultDS());

  public AssignHierarchyProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      // Prepare command object and validate the input
      AssignHierarchyCommand command = AssignHierarchyCommand.build(ebMessage.getRequestBody());
      AssignHierarchyCommand.AssignHierarchyCommandBean bean = command.asBean();

      // Verify the role and permission of user, if it has rights to assign the tenant hierarchy
      // TODO: authorization should be added later once we finalize the roles and permissions.

      // Verify that the given hierarchy present in database
      if (!ASSIGN_SERVICE.isHierarchyExists(bean.getHierarchyId())) {
        LOGGER.warn("hierarchy '{}' not present in data store", bean.getHierarchyId());
        result.complete(MessageResponseFactory.createNotFoundResponse("hierarchy not found"));
        return this.result;
      }

      // Verify that the given tenant is present in database
      if (!ASSIGN_SERVICE.isTenantExists(bean.getTenant())) {
        LOGGER.warn("tenant '{}' not present in data store", bean.getTenant());
        result.complete(MessageResponseFactory.createNotFoundResponse("tenant not found"));
        return this.result;
      }

      // Fetch number of sub tenants of the tenant. If there are sub tenants present, we can not
      // assign the hierarchy to parent tenant.
      Integer count = ASSIGN_SERVICE.fetchSubtenantCount(bean.getTenant());
      if (count != null && count > 0) {
        LOGGER.warn("tenant '{}' has '{}' subtenants, hence we can't set hierarchy",
            bean.getTenant(), count);
        result.complete(MessageResponseFactory
            .createInvalidRequestResponse("hierarchy can not be assigned to parent tenant"));
        return result;
      }

      // persist the hierarchy against the tenant
      ASSIGN_SERVICE.insertOrupdateTenantHierarchy(bean);

      result.complete(MessageResponseFactory.createNoContentResponse());
    } catch (Throwable t) {
      LOGGER.error("exception while fetching all group hierarchies", t);
      result.fail(t);
    }

    return this.result;
  }

}
