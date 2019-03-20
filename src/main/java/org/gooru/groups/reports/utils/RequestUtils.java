
package org.gooru.groups.reports.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 15-Mar-2019
 */
public final class RequestUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtils.class);

  private RequestUtils() {
    throw new AssertionError();
  }

  public static Integer getAsInt(JsonObject requestBody, String key) {
    String value = requestBody.getString(key);
    if (value == null || value.isEmpty()) {
      return null;
    }

    Integer result = null;
    if (key != null) {
      try {
        result = Integer.valueOf(value);
      } catch (NumberFormatException e) {
        LOGGER.info("Invalid number format for {}", key);
        result = null;
      }
    }
    return result;
  }
}
