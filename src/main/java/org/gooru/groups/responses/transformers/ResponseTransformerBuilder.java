package org.gooru.groups.responses.transformers;

import org.gooru.groups.exceptions.HttpResponseWrapperException;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 20/2/18.
 */
public final class ResponseTransformerBuilder {

  public static ResponseTransformer build(Message<JsonObject> message) {
    return new HttpResponseTransformer(message);
  }

  public static ResponseTransformer buildHttpResponseWrapperExceptionBuild(
      HttpResponseWrapperException ex) {
    return new HttpResponseWrapperExceptionTransformer(ex);
  }

  private ResponseTransformerBuilder() {
    throw new AssertionError();
  }
}
