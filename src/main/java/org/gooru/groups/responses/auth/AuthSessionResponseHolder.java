package org.gooru.groups.responses.auth;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 20/2/18.
 */
public interface AuthSessionResponseHolder extends AuthResponseHolder {
  JsonObject getSession();
}
