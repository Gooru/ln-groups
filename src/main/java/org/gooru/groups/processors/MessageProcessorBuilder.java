package org.gooru.groups.processors;

import org.gooru.groups.constants.Constants;
import org.gooru.groups.reports.ca.ClassActivitiesCountProcessor;
import org.gooru.groups.reports.classes.student.detailed.summary.ClassStudentDetailedSummaryProcessor;
import org.gooru.groups.reports.classes.student.summary.ClassStudentSummaryReportProcessor;
import org.gooru.groups.reports.classes.student.summary.weekly.ClassStudentSummaryWeeklyReportProcessor;
import org.gooru.groups.reports.classes.summary.ClassSummaryReportProcessor;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 20/2/18.
 */
public final class MessageProcessorBuilder {
  private MessageProcessorBuilder() {
    throw new AssertionError();
  }

  public static MessageProcessor buildProcessor(Vertx vertx, Message<JsonObject> message,
      String op) {
    switch (op) {
      case Constants.Message.MSG_OP_REPORTS_GET_CA_ACTIVITIES_COUNT:
        return new ClassActivitiesCountProcessor(vertx, message);
      case Constants.Message.MSG_OP_REPORTS_GET_CLASS_SUMMARY_WEEKLY:
        return new ClassSummaryReportProcessor(vertx, message);
      case Constants.Message.MSG_OP_REPORTS_GET_CLASS_STUDENT_SUMMARY_WEEKLY:
        return new ClassStudentSummaryWeeklyReportProcessor(vertx, message);
      case Constants.Message.MSG_OP_REPORTS_GET_CLASS_STUDENT_DETAILED_SUMMARY_WEEKLY:
        return new ClassStudentDetailedSummaryProcessor(vertx, message);
      case Constants.Message.MSG_OP_REPORTS_GET_CLASS_STUDENT_SUMMARY:
        return new ClassStudentSummaryReportProcessor(vertx, message);
      default:
        return null;
    }

  }

}
