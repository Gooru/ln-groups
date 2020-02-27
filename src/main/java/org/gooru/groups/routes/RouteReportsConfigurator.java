
package org.gooru.groups.routes;

import org.gooru.groups.constants.Constants;
import org.gooru.groups.routes.utils.RouteHandlerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author szgooru Created On 04-Jan-2019
 */
public class RouteReportsConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteReportsConfigurator.class);

  private EventBus eb = null;
  private long mbusTimeout = 30000L;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eb = vertx.eventBus();
    this.mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1000;

    router.get(Constants.Route.API_REPORTS_CA_ACTIVITIES_COUNT_GET)
        .handler(this::getClassActivitiesCount);

    router.get(Constants.Route.API_REPORTS_CLASS_SUMMARY_GET).handler(this::getClassSummary);

    router.get(Constants.Route.API_REPORTS_CLASS_STUDENT_SUMMARY_WEEKLY_GET)
        .handler(this::getClassStudentSummaryWeekly);

    router.get(Constants.Route.API_REPORTS_CLASS_STUDENT_DETAILED_SUMMARY_GET)
        .handler(this::getClassStudentDetailedSummary);

    router.get(Constants.Route.API_FETCH_COUNTRIES).handler(this::fetchGroupReportCountries);
    router.get(Constants.Route.API_FETCH_PERF_SUBJECTS_BY_COUNTRY).handler(this::fetchSubjectsForPerfReportByCountry);
    router.get(Constants.Route.API_FETCH_PERF_REPORT_BY_COUNTRY)
        .handler(this::fetchPerfReportByCountry);
    router.get(Constants.Route.API_FETCH_PERF_REPORT_BY_STATE)
        .handler(this::fetchPerfReportByState);
    router.get(Constants.Route.API_FETCH_PERF_REPORT_BY_GROUP)
        .handler(this::fetchPerfReportByGroup);
    router.get(Constants.Route.API_FETCH_PERF_REPORT_BY_SCHOOL)
        .handler(this::fetchPerfReportBySchool);
    
    router.get(Constants.Route.API_REPORTS_CLASS_STUDENT_SUMMARY_GET)
    .handler(this::getClassStudentSummary);
    
    router.get(Constants.Route.API_FETCH_GROUP_REPORT).handler(this::fetchGroupReports);
    router.get(Constants.Route.API_FETCH_COMPETENCY_GROUP_REPORT)
        .handler(this::fetchCompetencyGroupReports);
    router.get(Constants.Route.API_FETCH_PERFORMANCE_GROUP_REPORT)
        .handler(this::fetchPerformanceGroupReports);
    
  }
  
  private void fetchGroupReports(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GROUPS,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }
  
  private void fetchCompetencyGroupReports(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GROUPS_COMPETENCY,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }
  
  private void fetchPerformanceGroupReports(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GROUPS_PERFORMANCE,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }
  

  private void getClassActivitiesCount(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GET_CA_ACTIVITIES_COUNT,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }

  private void getClassSummary(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GET_CLASS_SUMMARY_WEEKLY,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }

  private void getClassStudentSummaryWeekly(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GET_CLASS_STUDENT_SUMMARY_WEEKLY,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }

  private void getClassStudentDetailedSummary(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GET_CLASS_STUDENT_DETAILED_SUMMARY_WEEKLY,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }

  private void fetchGroupReportCountries(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GROUPS_COUNTRIES, Constants.EventBus.MBEP_DISPATCHER,
        this.mbusTimeout, LOGGER);
  }
  
  private void fetchSubjectsForPerfReportByCountry(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_PERF_REPORTS_SUBJECTS_BY_COUNTRY, Constants.EventBus.MBEP_DISPATCHER,
        this.mbusTimeout, LOGGER);
  }

  private void fetchPerfReportByCountry(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_PERF_REPORTS_GROUPS_BY_COUNTRY, Constants.EventBus.MBEP_DISPATCHER,
        this.mbusTimeout, LOGGER);
  }

  private void fetchPerfReportByState(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_PERF_REPORTS_GROUPS_BY_STATE, Constants.EventBus.MBEP_DISPATCHER,
        this.mbusTimeout, LOGGER);
  }

  private void fetchPerfReportByGroup(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_PERF_REPORTS_GROUPS_BY_GROUP, Constants.EventBus.MBEP_DISPATCHER,
        this.mbusTimeout, LOGGER);
  }

  private void fetchPerfReportBySchool(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_PERF_REPORTS_GROUPS_BY_SCHOOL, Constants.EventBus.MBEP_DISPATCHER,
        this.mbusTimeout, LOGGER);
  }
  
  private void getClassStudentSummary(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(this.eb, routingContext,
        Constants.Message.MSG_OP_REPORTS_GET_CLASS_STUDENT_SUMMARY,
        Constants.EventBus.MBEP_DISPATCHER, this.mbusTimeout, LOGGER);
  }
}
