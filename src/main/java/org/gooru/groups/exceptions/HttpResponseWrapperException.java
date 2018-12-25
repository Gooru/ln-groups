package org.gooru.groups.exceptions;

import org.gooru.groups.constants.Constants.Message;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 20/2/18.
 */
public final class HttpResponseWrapperException extends RuntimeException {
  private final HttpStatus status;
  private final JsonObject payload;

  public HttpResponseWrapperException(HttpStatus status, JsonObject payload) {
    this.status = status;
    this.payload = payload;
  }

  public HttpResponseWrapperException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.payload = new JsonObject().put(Message.MSG_MESSAGE, message);
  }

  public int getStatus() {
    return this.status.getCode();
  }

  public JsonObject getBody() {
    return this.payload;
  }
}
