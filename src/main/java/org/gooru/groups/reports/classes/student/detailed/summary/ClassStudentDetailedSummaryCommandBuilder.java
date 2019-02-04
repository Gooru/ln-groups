
package org.gooru.groups.reports.classes.student.detailed.summary;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.constants.HttpConstants.HttpStatus;
import org.gooru.groups.exceptions.HttpResponseWrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author renuka
 */
public final class ClassStudentDetailedSummaryCommandBuilder {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ClassStudentDetailedSummaryCommandBuilder.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private ClassStudentDetailedSummaryCommandBuilder() {
    throw new AssertionError();
  }

  public static ClassStudentDetailedSummaryCommand build(EventBusMessage ebMessage) {
    ClassStudentDetailedSummaryCommand command = buildFromJson(ebMessage.getRequestBody());
    validate(command);
    return command;
  }

  private static void validate(ClassStudentDetailedSummaryCommand command) {
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

    // If requested week is not valid week (sun-sat), return bad request
    if (!(isFirstDayOfWeek(command.getFromDate()) && isLastDayOfWeek(command.getToDate())
        && hasSixDaysBetween(command.getFromDate(), command.getToDate()))) {
      LOGGER.warn("Invalid date requested");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("invalid.date.format"));
    }

    if (!isValidFromDate(command.getFromDate())) {
      LOGGER.warn("trying to fetch report for future date");
      throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
          RESOURCE_BUNDLE.getString("future.date.report"));
    }
  }

  private static ClassStudentDetailedSummaryCommand buildFromJson(JsonObject request) {
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
    return new ClassStudentDetailedSummaryCommand(classId, fDate, tDate);
  }

  private static Boolean isFirstDayOfWeek(Date requestedDate) {
    Boolean isValidWeek = false;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(requestedDate);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == 1) {
      isValidWeek = true;
    }
    return isValidWeek;
  }

  private static Boolean isLastDayOfWeek(Date requestedDate) {
    Boolean isValidWeek = false;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(requestedDate);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == 7) {
      isValidWeek = true;
    }
    return isValidWeek;
  }

  private static Boolean isValidFromDate(Date requestedDate) {
    Boolean isValidDate = true;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(requestedDate);
    LocalDate reqDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH));
    LocalDate now = LocalDate.now();
    if (!reqDate.isEqual(now) && reqDate.isAfter(now)) {
      isValidDate = false;
    }
    return isValidDate;
  }

  private static Boolean hasSixDaysBetween(Date d1, Date d2) {
    Boolean isValidWeek = false;
    long diff = d2.getTime() - d1.getTime();
    if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 6) {
      isValidWeek = true;
    }
    return isValidWeek;
  }

}
