package org.gooru.groups.routes.utils;

import org.gooru.groups.constants.Constants.Message;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish on 20/2/18.
 */
public final class DeliveryOptionsBuilder {
  private DeliveryOptionsBuilder() {
    throw new AssertionError();
  }

  public static DeliveryOptions buildWithApiVersion(RoutingContext context) {
    final String apiVersion = context.request().getParam("version");
    VersionValidatorUtility.validateVersion(apiVersion);
    return new DeliveryOptions().addHeader(Message.MSG_API_VERSION, apiVersion);
  }

  public static DeliveryOptions buildWithoutApiVersion(RoutingContext context) {
    return new DeliveryOptions();
  }

  public static DeliveryOptions buildWithoutApiVersion(RoutingContext context, long timeout,
      String op) {
    return new DeliveryOptions().setSendTimeout(timeout * 1000).addHeader(Message.MSG_OP, op);
  }

}
