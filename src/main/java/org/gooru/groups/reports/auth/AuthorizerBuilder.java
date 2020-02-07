
package org.gooru.groups.reports.auth;

import java.util.UUID;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class AuthorizerBuilder {
  private AuthorizerBuilder() {
    throw new AssertionError();
  }

  public static Authorizer buildUserRoleAuthorizer(UUID userId) {
    return new UserRoleAuthorizer(userId);
  }
}
