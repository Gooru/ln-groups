
package org.gooru.groups.reports.auth;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public class AuthorizerBuilder {
  private AuthorizerBuilder() {
    throw new AssertionError();
  }

  public static Authorizer buildGroupReportAuthorizer(String userId) {
    return new GroupReportAuthorizer(userId);
  }
}
