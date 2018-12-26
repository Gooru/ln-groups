package org.gooru.groups.exceptions;

import org.gooru.groups.responses.MessageResponse;

/**
 * @author ashish on 20/2/18.
 */
public class MessageResponseWrapperException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final MessageResponse response;

  public MessageResponseWrapperException(MessageResponse response) {
    this.response = response;
  }

  public MessageResponse getMessageResponse() {
    return this.response;
  }
}
