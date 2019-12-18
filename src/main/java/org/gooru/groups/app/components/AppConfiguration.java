
package org.gooru.groups.app.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 15-Mar-2019
 */
public final class AppConfiguration implements Initializer {
  private static final String APP_CONFIG_KEY = "app.configuration";
  private JsonObject configuration;
  private static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);

  public static AppConfiguration getInstance() {
    return Holder.INSTANCE;
  }

  private volatile boolean initialized = false;
  
  @Override
  public void initializeComponent(Vertx vertx, JsonObject config) {
    LOGGER.debug("config : {}", config.toString());
    if (!initialized) {
      synchronized (Holder.INSTANCE) {
        if (!initialized) {
          JsonObject appConfiguration = config.getJsonObject(APP_CONFIG_KEY);
          if (appConfiguration == null || appConfiguration.isEmpty()) {
            LOGGER.warn("App configuration is not available");
          } else {
            configuration = appConfiguration.copy();
            initialized = true;
          }
        }
      }
    }
  }
  
  public JsonArray getGlobalReportAccessRoles() {
    return this.configuration.getJsonArray("gobal.report.access.roles");
  }
  
  public JsonArray getReportAccessRoles() {
    return this.configuration.getJsonArray("report.access.roles");
  }
  
  private static final class Holder {
    private static final AppConfiguration INSTANCE = new AppConfiguration();
  }


}
