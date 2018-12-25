
package org.gooru.groups.routes;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * @author szgooru Created On 25-Dec-2018
 */
public interface RouteConfigurator {
  void configureRoutes(Vertx vertx, Router router, JsonObject config);
}
