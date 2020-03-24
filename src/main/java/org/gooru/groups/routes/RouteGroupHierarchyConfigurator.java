package org.gooru.groups.routes;

import org.gooru.groups.constants.Constants;
import org.gooru.groups.routes.utils.RouteHandlerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class RouteGroupHierarchyConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteGroupHierarchyConfigurator.class);
  private EventBus eb;
  private long mbusTimeout = 30000L;
  
  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eb = vertx.eventBus();
    this.mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1000;
    
    router.get(Constants.Route.API_FETCH_GROUP_HIERARCHIES_ALL)
    .handler(this::fetchAllGroupHierarhies);
  }
  
  private void fetchAllGroupHierarhies(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_HIERARCHIES_GROUP_ALL,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }

}
