
package org.gooru.groups.reports.fetchcountries;

import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.responses.MessageResponse;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 18-Mar-2019
 */
public class FetchCountriesForGroupReportProcessor implements MessageProcessor {

  public FetchCountriesForGroupReportProcessor(Vertx vertx, Message<JsonObject> message) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public Future<MessageResponse> process() {
    // TODO Auto-generated method stub
    return null;
  }

}
