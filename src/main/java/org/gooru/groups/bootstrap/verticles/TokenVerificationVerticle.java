package org.gooru.groups.bootstrap.verticles;

import org.gooru.groups.constants.Constants;
import org.gooru.groups.constants.Constants.Message;
import org.gooru.groups.responses.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * @author ashish on 20/2/18.
 */
public class TokenVerificationVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(TokenVerificationVerticle.class);
  private RedisClient redisClient;

  @Override
  public void start(Future<Void> startFuture) {
    EventBus eb = this.vertx.eventBus();

    this.initializeVerticle(startFuture);

    eb.<JsonObject>localConsumer(Constants.EventBus.MBEP_TOKEN_VERIFICATION, message -> {
      String sessionToken = message.headers().get(Message.MSG_SESSION_TOKEN);
      Future<JsonObject> fetchSessionFuture = this.fetchSessionFromRedis(sessionToken);

      fetchSessionFuture.setHandler(sessionAsyncResult -> {
        if (sessionAsyncResult.succeeded()) {
          ResponseUtil.processSuccess(message, sessionAsyncResult.result());
          this.updateSessionExpiryInRedis(sessionToken);
        } else {
          ResponseUtil.processFailure(message);
        }
      });

    }).completionHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("Auth end point ready to listen");
      } else {
        LOGGER.error("Error registering the auth handler. Halting the auth machinery");
        Runtime.getRuntime().halt(2);
      }
    });
  }

  private Future<JsonObject> fetchSessionFromRedis(String sessionToken) {
    Future<JsonObject> future = Future.future();

    this.redisClient.get(sessionToken, redisAsyncResult -> {
      if (redisAsyncResult.succeeded()) {
        final String redisResult = redisAsyncResult.result();
        LOGGER.debug("Redis responded with '{}'", redisResult);
        if (redisResult != null) {
          try {
            JsonObject jsonResult = new JsonObject(redisResult);
            future.complete(jsonResult);
          } catch (DecodeException de) {
            LOGGER.error("exception while decoding json for token '{}'", sessionToken, de);
            future.fail(de);
          }
        } else {
          LOGGER.info("Session not found. Invalid session");
          future.fail("Session not found. Invalid session");
        }
      } else {
        LOGGER.error("Redis operation failed", redisAsyncResult.cause());
        future.fail(redisAsyncResult.cause());
      }
    });
    return future;
  }

  private Future<Void> updateSessionExpiryInRedis(String sessionToken) {
    Future<Void> future = Future.future();
    int sessionTimeout = this.config().getInteger("sessionTimeoutInSeconds");
    this.redisClient.expire(sessionToken, sessionTimeout, updateHandler -> {
      if (updateHandler.succeeded()) {
        LOGGER.debug("expiry time of session {} is updated", sessionToken);
      } else {
        LOGGER.warn("Not able to update expiry for key {}", sessionToken, updateHandler.cause());
      }
      future.complete();
    });
    return future;
  }

  @Override
  public void stop(Future<Void> stopFuture) {
    this.finalizeVerticle(stopFuture);
  }

  private void initializeVerticle(Future<Void> startFuture) {
    try {
      JsonObject configuration = this.config().getJsonObject("redisConfig");
      RedisOptions options = new RedisOptions(configuration);
      this.redisClient = RedisClient.create(this.vertx, options);
      this.redisClient.get("NonExistingKey", initHandler -> {
        if (initHandler.succeeded()) {
          LOGGER.info("Initial connection check with Redis done");
          startFuture.complete();
        } else {
          startFuture.fail(initHandler.cause());
        }
      });
    } catch (Throwable throwable) {
      LOGGER.error("Not able to continue initialization.", throwable);
      startFuture.fail(throwable);
    }
  }

  private void finalizeVerticle(Future<Void> stopFuture) {
    if (this.redisClient != null) {
      this.redisClient.close(redisCloseAsyncHandler -> {
        if (redisCloseAsyncHandler.succeeded()) {
          LOGGER.info("Redis client has been closed successfully");
        } else {
          LOGGER.error("Error in closing redis client", redisCloseAsyncHandler.cause());
        }
        stopFuture.complete();
      });
    }
  }

}
