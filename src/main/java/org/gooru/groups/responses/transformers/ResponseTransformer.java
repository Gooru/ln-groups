package org.gooru.groups.responses.transformers;

import java.util.Map;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 20/2/18.
 */
public interface ResponseTransformer {

  void transform();

  JsonObject transformedBody();

  Map<String, String> transformedHeaders();

  int transformedStatus();

}
