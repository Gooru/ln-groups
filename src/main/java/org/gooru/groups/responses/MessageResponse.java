
package org.gooru.groups.responses;

import org.gooru.groups.constants.Constants.Message;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 26-Dec-2018
 */
public class MessageResponse {
  private final DeliveryOptions deliveryOptions;
  private final JsonObject reply;

  // Private constructor
  private MessageResponse(JsonObject response) {
    this.deliveryOptions = new DeliveryOptions();
    this.reply = response;
  }

  public DeliveryOptions deliveryOptions() {
    return this.deliveryOptions;
  }

  public JsonObject reply() {
    return this.reply;
  }

  public boolean isSuccessful() {
    return this.reply.getInteger(Message.MSG_HTTP_STATUS) == HttpStatus.SUCCESS.getCode();
  }

  // Public builder with validations
  public static class Builder {
    private HttpStatus httpStatus;
    private JsonObject responseBody;
    private JsonObject headers;

    public Builder() {
      this.headers = new JsonObject();
      // Default content type is JSON, unless caller overrides it later
      this.setContentTypeJson();
    }

    // Setters for http status code
    public Builder setStatusCreated() {
      this.httpStatus = HttpStatus.CREATED;
      return this;
    }

    public Builder setStatusOkay() {
      this.httpStatus = HttpStatus.SUCCESS;
      return this;
    }

    public Builder setStatusNoOutput() {
      this.httpStatus = HttpStatus.NO_CONTENT;
      return this;
    }

    public Builder setStatusBadRequest() {
      this.httpStatus = HttpStatus.BAD_REQUEST;
      return this;
    }

    public Builder setStatusForbidden() {
      this.httpStatus = HttpStatus.FORBIDDEN;
      return this;
    }

    public Builder setStatusUnAuthorized() {
      this.httpStatus = HttpStatus.UNAUTHORIZED;
      return this;
    }

    public Builder setStatusNotFound() {
      this.httpStatus = HttpStatus.NOT_FOUND;
      return this;
    }

    public Builder setStatusInternalError() {
      this.httpStatus = HttpStatus.ERROR;
      return this;
    }

    public Builder setStatusHttpCode(HttpStatus httpStatus) {
      this.httpStatus = httpStatus;
      return this;
    }

    // Setters for headers
    public Builder setContentTypeJson() {
      this.headers.put(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
      return this;
    }

    public Builder setHeader(String key, String value) {
      if (key == null || value == null) {
        return this;
      }
      if (key.equalsIgnoreCase(HttpConstants.HEADER_CONTENT_LENGTH)) {
        // Do not allow content length to be setup, it should be handled
        // in gateway
        return this;
      }
      this.headers.put(key, value);
      return this;
    }

    // Setters for Response body, interpreted as per status of message
    public Builder setResponseBody(JsonObject responseBody) {
      this.responseBody = responseBody;
      return this;
    }

    public MessageResponse build() {
      JsonObject result = new JsonObject();
      result.put(Message.MSG_HTTP_STATUS, this.httpStatus.getCode())
          .put(Message.MSG_HTTP_HEADERS, this.headers).put(Message.MSG_HTTP_BODY,
              this.responseBody == null ? new JsonObject() : this.responseBody);
      return new MessageResponse(result);
    }

  }
}
