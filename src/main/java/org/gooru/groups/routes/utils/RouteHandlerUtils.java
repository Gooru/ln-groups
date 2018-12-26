package org.gooru.groups.routes.utils;

import org.gooru.groups.constants.Constants.Message;
import org.slf4j.Logger;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish on 20/2/18.
 */
public final class RouteHandlerUtils {

  private RouteHandlerUtils() {
    throw new AssertionError();
  }

  public static void baseHandler(EventBus eb, RoutingContext routingContext, String op,
      String eventBusEndPoint, long mbusTimeout, Logger logger) {
    baseHandler(eb, routingContext, op, eventBusEndPoint, mbusTimeout, true, false, logger);
  }

  public static void baseHandler(EventBus eb, RoutingContext routingContext, String op,
      String eventBusEndPoint, long mbusTimeout, boolean withPathParams, boolean withoutVersion,
      Logger logger) {
    DeliveryOptions options;
    if (withoutVersion) {
      options = DeliveryOptionsBuilder.buildWithoutApiVersion(routingContext)
          .setSendTimeout(mbusTimeout).addHeader(Message.MSG_OP, op);
    } else {
      options = DeliveryOptionsBuilder.buildWithApiVersion(routingContext)
          .setSendTimeout(mbusTimeout).addHeader(Message.MSG_OP, op);
    }
    eb.<JsonObject>send(eventBusEndPoint,
        RouteRequestUtility.getBodyForMessage(routingContext, withPathParams), options,
        reply -> RouteResponseUtility.responseHandler(routingContext, reply, logger));
  }
}
