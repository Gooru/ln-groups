package org.gooru.groups.routes.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 20/2/18.
 */
public final class VersionValidatorUtility {

  private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
  private static final Logger LOGGER = LoggerFactory.getLogger(VersionValidatorUtility.class);
  private static final List<String> supportedVersions = Arrays.asList("v1", "v2");
  private static final List<String> deprecatedVersions = new ArrayList<>();

  private VersionValidatorUtility() {
    throw new AssertionError();
  }

  public static void validateVersion(String version) {
    LOGGER.debug("Version in API call is : {}", version);
    if (deprecatedVersions.contains(version)) {
      throw new HttpResponseWrapperException(HttpStatus.GONE,
          resourceBundle.getString("api.version.deprecated"));
    } else if (!supportedVersions.contains(version)) {
      throw new HttpResponseWrapperException(HttpStatus.NOT_IMPLEMENTED,
          resourceBundle.getString("api.version.unsupported"));
    }
  }
}
