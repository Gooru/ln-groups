
package org.gooru.groups.reports.auth;

/**
 * @author szgooru Created On 20-Mar-2019
 */
public interface Authorizer {
  void authorize();
  
  boolean  isGlobalAccess();
}
