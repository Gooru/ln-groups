package org.gooru.groups.processors;

import org.gooru.groups.responses.MessageResponse;
import io.vertx.core.Future;

/**
 * @author ashish on 10/1/18.
 */
public interface MessageProcessor {
  Future<MessageResponse> process();
}
