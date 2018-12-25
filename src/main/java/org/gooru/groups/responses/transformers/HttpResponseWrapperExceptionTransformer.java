package org.gooru.groups.responses.transformers;

import java.util.Collections;
import java.util.Map;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author ashish on 20/2/18.
 */
public final class HttpResponseWrapperExceptionTransformer implements ResponseTransformer {

  private final HttpResponseWrapperException ex;

  HttpResponseWrapperExceptionTransformer(HttpResponseWrapperException ex) {
    this.ex = ex;
  }

  @Override
  public void transform() {
    // no op
  }

  @Override
  public JsonObject transformedBody() {
    return this.ex.getBody();
  }

  @Override
  public Map<String, String> transformedHeaders() {
    return Collections.emptyMap();
  }

  @Override
  public int transformedStatus() {
    return this.ex.getStatus();
  }
}
