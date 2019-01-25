
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
 * @author szgooru Created On 04-Jan-2019
 */
public class RouteReportsConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteReportsConfigurator.class);

  private EventBus eb = null;
  private long mbusTimeout = 30000L;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eb = vertx.eventBus();
    this.mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1000;
    
    router.get(Constants.Route.API_REPORTS_CA_ACTIVITIES_COUNT_GET)
        .handler(this::getClassActivitiesCount);
  }

  private void getClassActivitiesCount(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GET_CA_ACTIVITIES_COUNT,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }
}