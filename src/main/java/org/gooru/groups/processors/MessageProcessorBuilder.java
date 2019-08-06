package org.gooru.groups.processors;

import org.gooru.groups.constants.Constants;
import org.gooru.groups.reports.ca.ClassActivitiesCountProcessor;
import org.gooru.groups.reports.classes.student.detailed.summary.ClassStudentDetailedSummaryProcessor;
import org.gooru.groups.reports.classes.student.summary.ClassStudentSummaryReportProcessor;
import org.gooru.groups.reports.classes.student.summary.weekly.ClassStudentSummaryWeeklyReportProcessor;
import org.gooru.groups.reports.classes.summary.ClassSummaryReportProcessor;
<<<<<<< HEAD
import org.gooru.groups.reports.country.perf.GroupReportByCountryProcessor;
import org.gooru.groups.reports.fetchcountries.FetchCountriesForGroupReportProcessor;
import org.gooru.groups.reports.group.perf.GroupReportByGroupProcessor;
import org.gooru.groups.reports.school.perf.GroupReportBySchoolProcessor;
import org.gooru.groups.reports.state.perf.GroupReportByStateProcessor;
=======
>>>>>>> e55a223e4c6b6846bb319e1c95036a3100b2bf01
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
<<<<<<< HEAD
        
      case Constants.Message.MSG_OP_REPORTS_GROUPS_COUNTRIES:
        return new FetchCountriesForGroupReportProcessor(vertx, message);
        
      case Constants.Message.MSG_OP_REPORTS_GROUPS_BY_COUNTRY:
        return new GroupReportByCountryProcessor(vertx, message);
        
      case Constants.Message.MSG_OP_REPORTS_GROUPS_BY_STATE:
        return new GroupReportByStateProcessor(vertx, message);
        
      case Constants.Message.MSG_OP_REPORTS_GROUPS_BY_GROUP:
        return new GroupReportByGroupProcessor(vertx, message);
        
      case Constants.Message.MSG_OP_REPORTS_GROUPS_BY_SCHOOL:
        return new GroupReportBySchoolProcessor(vertx, message);
        
=======
      case Constants.Message.MSG_OP_REPORTS_GET_CLASS_STUDENT_SUMMARY:
        return new ClassStudentSummaryReportProcessor(vertx, message);
>>>>>>> e55a223e4c6b6846bb319e1c95036a3100b2bf01
      default:
        return null;
    }
  }
}
