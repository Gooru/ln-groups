package org.gooru.groups.responses.auth;

/**
 * @author ashish on 20/2/18.
 */

public interface AuthResponseHolder {
  boolean isAuthorized();

  boolean isAnonymous();

  String getUser();
}
