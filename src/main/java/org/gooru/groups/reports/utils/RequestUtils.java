
package org.gooru.groups.reports.utils;

import java.util.HashSet;
import java.util.Set;
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

  public static Long getAsLong(JsonObject requestBody, String key) {
    String value = requestBody.getString(key);
    if (value == null || value.isEmpty()) {
      return null;
    }

    Long result = null;
    if (key != null) {
      try {
        result = Long.valueOf(value);
      } catch (NumberFormatException e) {
        LOGGER.info("Invalid number format for {}", key);
        result = null;
      }
    }
    return result;
  }

  public static Set<String> getAsSet(JsonObject requestBody, String key) {
    String value = requestBody.getString(key);
    if (value == null || value.isEmpty()) {
      return null;
    }

    Set<String> list = new HashSet<>();
    if (key != null) {
      try {
        String[] array = value.split(",");
        for (String v : array) {
          if (v != null && !v.isEmpty() && v.trim().length() > 0) {
            list.add(v);
          }
        }
      } catch (Exception ex) {
        LOGGER.info("Invalid format for {} to convert to list", key);
      }
    }
    return list;
  }
}
