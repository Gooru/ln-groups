package org.gooru.groups.app.data;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ashish on 8/3/18.
 */
public class JsonNodeHolder {
  private final JsonNode jsonNode;

  public JsonNodeHolder(JsonNode jsonNode) {
    this.jsonNode = jsonNode;
  }

  public JsonNode getJsonNode() {
    return jsonNode;
  }
}
