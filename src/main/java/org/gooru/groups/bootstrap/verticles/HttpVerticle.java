package org.gooru.groups.bootstrap.verticles;

import org.gooru.groups.app.components.Finalizer;
import org.gooru.groups.app.components.Finalizers;
import org.gooru.groups.routes.RouteConfiguration;
import org.gooru.groups.routes.RouteConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * @author ashish on 20/2/18.
 */
public class HttpVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class);

  @Override
  public void start(Future<Void> startFuture) {
    LOGGER.info("Starting Http Verticle ...");
    final HttpServer httpServer = this.vertx.createHttpServer();

    final Router router = Router.router(this.vertx);
    this.configureRoutes(router);

    final int port = this.config().getInteger("http.port");
    LOGGER.info("Http Verticle starting on port: '{}'", port);
    httpServer.requestHandler(router::accept).listen(port, result -> {
      if (result.succeeded()) {
        LOGGER.info("Http Verticle started successfully");
        startFuture.complete();
      } else {
        LOGGER.error("Http Verticle failed to start", result.cause());
        startFuture.fail(result.cause());
      }
    });

  }

  @Override
  public void stop(Future<Void> stopFuture) {
    // Currently a no op
    this.finalizeApplication(stopFuture);
  }

  private void configureRoutes(final Router router) {
    RouteConfiguration rc = new RouteConfiguration();
    for (RouteConfigurator configurator : rc) {
      configurator.configureRoutes(this.vertx, router, this.config());
    }
  }

  private void finalizeApplication(Future<Void> stopFuture) {
    this.vertx.executeBlocking(future -> {
      Finalizers finalizers = new Finalizers();
      for (Finalizer finalizer : finalizers) {
        finalizer.finalizeComponent();
      }
      future.complete();
    }, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Application finalization done");
        stopFuture.complete();
      } else {
        LOGGER.warn("Application finalization failed", ar.cause());
        stopFuture.fail(ar.cause());
      }
    });
  }

}
