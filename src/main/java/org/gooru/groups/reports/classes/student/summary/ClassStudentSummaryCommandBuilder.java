
package org.gooru.groups.reports.classes.student.summary;

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
public final class ClassStudentSummaryCommandBuilder {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ClassStudentSummaryCommandBuilder.class);
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

  private ClassStudentSummaryCommandBuilder() {
    throw new AssertionError();
  }

  public static ClassStudentSummaryCommand build(EventBusMessage ebMessage) {
    ClassStudentSummaryCommand command = buildFromJson(ebMessage.getRequestBody());
    validate(command);
    return command;
  }

  private static void validate(ClassStudentSummaryCommand command) {
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

  private static ClassStudentSummaryCommand buildFromJson(JsonObject request) {
    String classId = request.getString("classId");
    String dateTill = request.getString("dateTill");
    String fromDate = request.getString("fromDate");
    String toDate = request.getString("toDate");

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date fDate = null;
    Date tDate = null;
    Date tillDate = null;
    if (fromDate != null && toDate != null) {
      try {
        fDate = simpleDateFormat.parse(fromDate);
        tDate = simpleDateFormat.parse(toDate);
      } catch (Exception e) {
        LOGGER.warn("Invalid date requested");
        throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
            RESOURCE_BUNDLE.getString("invalid.date.format"));
      }
    } else if (dateTill != null){
      try {
        tillDate = simpleDateFormat.parse(dateTill);
      } catch (Exception e) {
        LOGGER.warn("Invalid date requested");
        throw new HttpResponseWrapperException(HttpStatus.BAD_REQUEST,
            RESOURCE_BUNDLE.getString("invalid.date.format"));
      }
    } else {
      tillDate = Calendar.getInstance().getTime();
    } 

    if (tillDate != null && !isValidDate(tillDate)) {
      tillDate = Calendar.getInstance().getTime();
    }
    return new ClassStudentSummaryCommand(classId, fDate, tDate, tillDate);
  }

  private static Boolean isValidDate(Date requestedDate) {
    Boolean isValidDate = true;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(requestedDate);
    LocalDate reqDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH));
    LocalDate now = LocalDate.now();
    if (reqDate.isAfter(now)) {
      isValidDate = false;
    }
    return isValidDate;
  }
  
  private static Boolean isValidFromAndToDate(Date fromDate, Date toDate) {
    Boolean isValidDate = true;
    Calendar fromCalendar = Calendar.getInstance();
    fromCalendar.setTime(fromDate);
    LocalDate reqFromDate = LocalDate.of(fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH) + 1,
        fromCalendar.get(Calendar.DAY_OF_MONTH));
    Calendar toCalendar = Calendar.getInstance();
    toCalendar.setTime(toDate);
    LocalDate reqToDate = LocalDate.of(toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH) + 1,
        toCalendar.get(Calendar.DAY_OF_MONTH));
    LocalDate now = LocalDate.now();
    if (reqFromDate.isAfter(now) || reqToDate.isBefore(reqFromDate)) {
      isValidDate = false;
    }
    return isValidDate;
  }

}
