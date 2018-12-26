package org.gooru.groups.responses;

import org.gooru.groups.constants.Constants.Message;
import org.gooru.groups.constants.HttpConstants;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.responses.MessageResponse.Builder;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 20/2/18.
 */
public final class MessageResponseFactory {

  private static final String API_VERSION_DEPRECATED = "API version is deprecated";

  private MessageResponseFactory() {
    throw new AssertionError();
  }

  public static MessageResponse createInvalidRequestResponse() {
    return new Builder().setStatusBadRequest()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, "Invalid request")).build();
  }

  public static MessageResponse createForbiddenResponse() {
    return new Builder().setStatusForbidden()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, "Forbidden")).build();
  }

  public static MessageResponse createInternalErrorResponse() {
    return new Builder().setStatusInternalError()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, "Internal error")).build();
  }

  public static MessageResponse createInvalidRequestResponse(String message) {
    return new Builder().setStatusBadRequest()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createForbiddenResponse(String message) {
    return new Builder().setStatusForbidden()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createUnauthorizedResponse(String message) {
    return new Builder().setStatusUnAuthorized()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createInternalErrorResponse(String message) {
    return new Builder().setStatusInternalError()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createInternalErrorResponse(JsonObject body) {
    return new Builder().setStatusInternalError().setResponseBody(body).build();
  }

  public static MessageResponse createNotFoundResponse(String message) {
    return new Builder().setStatusNotFound()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createOkayResponse(JsonObject body) {
    return new Builder().setStatusOkay().setResponseBody(body).build();
  }

  public static MessageResponse createCreatedResponse(String location) {
    return new Builder().setStatusCreated().setHeader(HttpConstants.HEADER_LOCATION, location)
        .build();
  }

  public static MessageResponse createNoContentResponse() {
    return new Builder().setStatusNoOutput().setResponseBody(new JsonObject()).build();
  }

  public static MessageResponse createVersionDeprecatedResponse() {
    return new Builder().setStatusHttpCode(HttpStatus.GONE).setContentTypeJson()
        .setResponseBody(new JsonObject().put(Message.MSG_MESSAGE, API_VERSION_DEPRECATED)).build();
  }
}
