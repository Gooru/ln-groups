
package org.gooru.groups.routes;

import org.gooru.groups.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * @author szgooru Created On 26-Dec-2018
 */
public class RouteGroupsConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteGroupsConfigurator.class);
  private EventBus eBus;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eBus = vertx.eventBus();
    this.mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L);
  }

}
