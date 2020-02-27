package org.gooru.groups.routes;

import org.gooru.groups.constants.Constants;
import org.gooru.groups.constants.Constants.Route;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.responses.auth.AuthSessionResponseHolder;
import org.gooru.groups.responses.auth.AuthSessionResponseHolderBuilder;
import org.gooru.groups.routes.utils.DeliveryOptionsBuilder;
import org.gooru.groups.routes.utils.TokenValidationUtils;
import org.gooru.groups.routes.utils.UserPermissionAuthorizerUtil;
import org.gooru.groups.routes.utils.VerificationCompletionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish on 20/2/18.
 */
class RouteTokenVerificationConfigurator implements RouteConfigurator {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RouteTokenVerificationConfigurator.class);
  private EventBus eBus;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eBus = vertx.eventBus();
    this.mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L);

    router.route(Route.API_GROUP_TOKEN_VERIFICATION).handler(this::verifyToken);
    router.route(Route.API_REPORTS_TOKEN_VERIFICATION).handler(this::verifyToken);
  }

  private void verifyToken(RoutingContext routingContext) {
    String sessionToken = TokenValidationUtils
        .extractSessionToken(routingContext.request().getHeader(HttpConstants.HEADER_AUTH));

    if (sessionToken == null || sessionToken.isEmpty()) {
      this.sendUnAuthorizedResponse(routingContext);
    } else {
      routingContext.put(Constants.Message.MSG_SESSION_TOKEN, sessionToken);
      this.eBus.<JsonObject>send(Constants.EventBus.MBEP_TOKEN_VERIFICATION, null,
          this.createDeliveryOptionsForTokenVerification(routingContext, sessionToken),
          reply -> this.tokenVerificationCompletionHandler(routingContext, reply));
    }
  }

  private void tokenVerificationCompletionHandler(RoutingContext routingContext,
      AsyncResult<Message<JsonObject>> reply) {
    if (reply.succeeded()) {
      AuthSessionResponseHolder responseHolder =
          AuthSessionResponseHolderBuilder.build(reply.result());

      if (responseHolder.isAuthorized() && !responseHolder.isAnonymous()) {
        JsonObject session = responseHolder.getSession();
        VerificationCompletionHelper.setupUserContextInRoutingContext(routingContext, session,
            responseHolder.getUser());
        //if (UserPermissionAuthorizerUtil.authorize(routingContext, session)) {
          routingContext.next();  
        //} else {
        //  this.sendForbiddenResponse(routingContext);
        //}
      } else {
        this.logUnAuthorized(responseHolder);
        this.sendUnAuthorizedResponse(routingContext);
      }
    } else {
      LOGGER.error("Not able to send message to Token verification endpoint", reply.cause());
      routingContext.response().setStatusCode(HttpStatus.ERROR.getCode()).end();
    }
  }

  private void logUnAuthorized(AuthSessionResponseHolder responseHolder) {
    if (responseHolder.isAuthorized()) {
      LOGGER.warn("Anonymous access not allowed");
    } else {
      LOGGER.warn("Unauthorized access not allowed");
    }
  }

  private DeliveryOptions createDeliveryOptionsForTokenVerification(RoutingContext routingContext,
      String sessionToken) {
    return DeliveryOptionsBuilder
        .buildWithoutApiVersion(routingContext, this.mbusTimeout,
            Constants.Message.MSG_OP_VERIFY_TOKEN)
        .addHeader(Constants.Message.MSG_SESSION_TOKEN, sessionToken);
  }

  private void sendUnAuthorizedResponse(RoutingContext routingContext) {
    routingContext.response().setStatusCode(HttpStatus.UNAUTHORIZED.getCode())
        .setStatusMessage(HttpStatus.UNAUTHORIZED.getMessage()).end();
  }
  
  private void sendForbiddenResponse(RoutingContext routingContext) {
    routingContext.response().setStatusCode(HttpStatus.FORBIDDEN.getCode())
    .setStatusMessage(HttpStatus.FORBIDDEN.getMessage()).end();
  }

}
