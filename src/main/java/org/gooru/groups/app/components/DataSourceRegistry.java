
package org.gooru.groups.app.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.gooru.groups.app.components.helpers.DatasourceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 25-Dec-2018
 */
public class DataSourceRegistry implements Initializer, Finalizer {

  private static final String CONFIG_DS_KEY = "datasources";
  private static final String DEFAULT_DATA_SOURCE = "defaultDataSource";
  private static final String DSDB_DATA_SOURCE = "dsdbDataSource";
  private static final String ANALYTICS_DATA_SOURCE = "analyticsDataSource";
  private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceRegistry.class);
  
  // All the elements in this array are supposed to be present in config file
  // as keys as we are going to initialize them with the value associated with
  // that key
  private final List<String> datasources =
      Arrays.asList(DEFAULT_DATA_SOURCE, DSDB_DATA_SOURCE, ANALYTICS_DATA_SOURCE);
  private final Map<String, DataSource> registry = new HashMap<>();
  private volatile boolean initialized;

  private DataSourceRegistry() {}

  public static DataSourceRegistry getInstance() {
    return Holder.INSTANCE;
  }

  private static final class Holder {
    private static final DataSourceRegistry INSTANCE = new DataSourceRegistry();
  }

  @Override
  public void initializeComponent(Vertx vertx, JsonObject config) {
    // Skip if we are already initialized
    LOGGER.debug("Initialization called upon.");
    if (!this.initialized) {
      LOGGER.debug("May have to do initialization");
      // We need to do initialization, however, we are running it via
      // verticle instance which is going to run in
      // multiple threads hence we need to be safe for this operation
      synchronized (Holder.INSTANCE) {
        LOGGER.debug("Will initialize after double checking");
        if (!this.initialized) {
          LOGGER.debug("Initializing now");
          JsonObject dsConfig = config.getJsonObject(CONFIG_DS_KEY);
          if (dsConfig == null || dsConfig.isEmpty()) {
            throw new IllegalStateException("No configuaration for datasouces found");
          }
          for (String datasource : this.datasources) {
            JsonObject dbConfig = dsConfig.getJsonObject(datasource);
            if (dbConfig != null) {
              DataSource ds = DatasourceHelper.initializeDataSource(dbConfig);
              this.registry.put(datasource, ds);
            }
          }
          this.initialized = true;
        }
      }
    }
  }

  public DataSource getDefaultDataSource() {
    return this.registry.get(DEFAULT_DATA_SOURCE);
  }

  public DataSource getDsdbDataSource() {
    return this.registry.get(DSDB_DATA_SOURCE);
  }

  public DataSource getAnalyticsDataSource() {
      return registry.get(ANALYTICS_DATA_SOURCE);
  }

  public DataSource getDataSourceByName(String name) {
    if (name != null) {
      return this.registry.get(name);
    }
    return null;
  }

  @Override
  public void finalizeComponent() {
    for (String datasource : this.datasources) {
      DataSource ds = this.registry.get(datasource);
      if (ds != null) {
        if (ds instanceof HikariDataSource) {
          ((HikariDataSource) ds).close();
        }
      }
    }
  }
}
