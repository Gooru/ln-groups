
package org.gooru.groups.app.components;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 25-Dec-2018
 */
public interface Initializer {
  void initializeComponent(Vertx vertx, JsonObject config);
}
