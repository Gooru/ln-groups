
package org.gooru.groups.reports.ca;

import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 08-Jan-2019
 */
public final class ClassActivitiesCountCommandBuilder {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ClassActivitiesCountCommandBuilder.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private ClassActivitiesCountCommandBuilder() {
    throw new AssertionError();
  }

  public static ClassActivitiesCountCommand build(EventBusMessage ebMessage) {
    ClassActivitiesCountCommand command = buildFromJson(ebMessage.getRequestBody());
    validate(command);
    return command;
  }

  private static void validate(ClassActivitiesCountCommand command) {
    if (command.getClassId() == null || command.getClassId().isEmpty()) {
      LOGGER.warn("class id not provided in request");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("missing.classid"));
    }

    try {
      UUID.fromString(command.getClassId());
    } catch (IllegalArgumentException iae) {
      LOGGER.warn("invalid format of the class id");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("invalid.classid.format"));
    }

    // It's decided not to fall back on the current month and year if the input is invalid or not
    // present.

    // If month is not between valid month number, return bad request
    if (command.getMonth() < 1 || command.getMonth() > 12) {
      LOGGER.warn("Invalid month");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("invalid.month.format"));
    }

    // If year is future year, return bad request
    if (command.getYear() > LocalDate.now().getYear()) {
      LOGGER.warn("future year provided in request");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("future.year"));
    }
  }

  private static ClassActivitiesCountCommand buildFromJson(JsonObject request) {
    String classId = request.getString("classId");

    String strMonth = request.getString("month");
    Integer month;
    try {
      month = Integer.parseInt(strMonth);
    } catch (NumberFormatException nfe) {
      LOGGER.warn("invalid format of month:{}", strMonth);
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("invalid.month.format"));
    }

    String strYear = request.getString("year");
    Integer year;
    try {
      year = Integer.parseInt(strYear);
    } catch (NumberFormatException nfe) {
      LOGGER.warn("invalid format of year:{}", strYear);
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("invalid.year.format"));
    }

    return new ClassActivitiesCountCommand(classId, month, year);
  }
}
