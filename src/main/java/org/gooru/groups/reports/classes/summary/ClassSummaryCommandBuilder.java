
package org.gooru.groups.reports.classes.summary;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.gooru.groups.processor.utils.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author renuka
 */
public final class ClassSummaryCommandBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassSummaryCommandBuilder.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private ClassSummaryCommandBuilder() {
    throw new AssertionError();
  }

  public static ClassSummaryCommand build(EventBusMessage ebMessage) {
    ClassSummaryCommand command = buildFromJson(ebMessage.getRequestBody());
    validate(command);
    return command;
  }

  private static void validate(ClassSummaryCommand command) {
    if (command.getClassId() == null || command.getClassId().isEmpty()) {
      LOGGER.warn("class id not provided in request");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("missing.classid"));
    }

    if (!ValidatorUtils.isValidUUID(command.getClassId())) {
      LOGGER.warn("invalid format of the class id");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("invalid.classid.format"));
    }

    if (command.getFromDate() != null && command.getToDate() != null) {
      if (!isValidFromAndToDate(command.getFromDate(), command.getToDate())) {
        LOGGER.warn("Invalid date range requested");
        throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
            RESOURCE_BUNDLE.getString("invalid.date.range"));
      }
    }
  }

  private static ClassSummaryCommand buildFromJson(JsonObject request) {
    String classId = request.getString("classId");
    String fromDate = request.getString("fromDate");
    String toDate = request.getString("toDate");

    if (fromDate == null || toDate == null) {
      LOGGER.warn("Missing Date");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("missing.date"));
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date fDate;
    Date tDate;
    try {
      fDate = simpleDateFormat.parse(fromDate);
      tDate = simpleDateFormat.parse(toDate);
    } catch (Exception e) {
      LOGGER.warn("Invalid date requested");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("invalid.date.format"));
    }
    return new ClassSummaryCommand(classId, fDate, tDate);
  }

  private static Boolean isValidFromAndToDate(Date fromDate, Date toDate) {
    Boolean isValidDate = true;
    Calendar fromCalendar = Calendar.getInstance();
    fromCalendar.setTime(fromDate);
    LocalDate reqFromDate = LocalDate.of(fromCalendar.get(Calendar.YEAR),
        fromCalendar.get(Calendar.MONTH) + 1, fromCalendar.get(Calendar.DAY_OF_MONTH));
    Calendar toCalendar = Calendar.getInstance();
    toCalendar.setTime(toDate);
    LocalDate reqToDate = LocalDate.of(toCalendar.get(Calendar.YEAR),
        toCalendar.get(Calendar.MONTH) + 1, toCalendar.get(Calendar.DAY_OF_MONTH));
    LocalDate now = LocalDate.now();
    if (reqFromDate.isAfter(now) || reqToDate.isBefore(reqFromDate)) {
      isValidDate = false;
    }
    return isValidDate;
  }

}
