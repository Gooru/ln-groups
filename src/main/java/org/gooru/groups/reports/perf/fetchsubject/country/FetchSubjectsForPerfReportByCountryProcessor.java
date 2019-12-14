
package org.gooru.groups.reports.perf.fetchsubject.country;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.dbhelpers.GroupReportService;
import org.gooru.groups.reports.dbhelpers.SubjectFrameworkModel;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.SubjectModel;
import org.gooru.groups.responses.MessageResponse;
import org.gooru.groups.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public class FetchSubjectsForPerfReportByCountryProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(FetchSubjectsForPerfReportByCountryProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupReportService service = new GroupReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());
  
  public FetchSubjectsForPerfReportByCountryProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);
      FetchSubjectsForPerfReportByCountryCommand command =
          FetchSubjectsForPerfReportByCountryCommand.build(ebMessage.getRequestBody());
      FetchSubjectsForPerfReportByCountryCommand.FetchSubjectsForPerfReportByCountryCommandBean bean =
          command.asBean();

      List<SubjectFrameworkModel> subjects = new ArrayList<>();
      if (command.getFrequency().equalsIgnoreCase(CommandAttributeConstants.FREQUENCY_WEEKLY)) {
        subjects = this.service.fetchSubjectsForPerfReportMonthByCountry(bean);
      } else {
        subjects = this.service.fetchSubjectsForPerfReportMonthByCountry(bean);
      }

      Set<String> uniqueSubjects = new HashSet<>();
      subjects.forEach(subject -> {
        String subjectCode = subject.getFramework() == null ? subject.getSubject()
            : (subject.getFramework() + "." + subject.getSubject());

        if (subject.getFramework() != null) {
          uniqueSubjects.add(subjectCode);
        }
      });
      
      Map<String, SubjectModel> subjectModels = this.coreService.fetchSubjectDetails(uniqueSubjects);

      JsonObject response = new JsonObject();
      JsonArray subjectArray = new JsonArray();
      subjects.forEach(subject -> {
        JsonObject subjectJson = new JsonObject();
        String subjectCode = subject.getFramework() == null ? subject.getSubject()
            : (subject.getFramework() + "." + subject.getSubject());
        subjectJson.put("name", subjectModels.get(subjectCode).getTitle());
        subjectJson.put("subject", subject.getSubject());
        subjectJson.put("framework", subject.getFramework());
        subjectArray.add(subjectJson);
      });

      response.put("subjects", subjectArray);
      result.complete(MessageResponseFactory.createOkayResponse(response));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching subjects for performance report", t);
      result.fail(t);
    }

    return this.result;
  }

}
