package org.gooru.groups.hierarchies.fetch;

import java.util.List;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.responses.MessageResponse;
import org.gooru.groups.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru on 24-Mar-2020
 *
 */
public class FetchGroupHierarchiesAllProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(FetchGroupHierarchiesAllProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final FetchGroupHierarchiesAllService GROUP_SERVICE =
      new FetchGroupHierarchiesAllService(DBICreator.getDbiForDefaultDS());

  public FetchGroupHierarchiesAllProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {

      List<FetchGroupHierarchiesAllModel> groupHierarchies =
          GROUP_SERVICE.fetchAllGroupHierarchies();
      FetchGroupHierarchiesAllResponseModel responseModel =
          FetchGroupHierarchiesAllResponseBuilder.build(groupHierarchies);

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.error("exception while fetching all group hierarchies", t);
      result.fail(t);
    }

    return this.result;
  }

}
