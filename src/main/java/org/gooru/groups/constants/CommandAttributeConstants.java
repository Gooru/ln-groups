
package org.gooru.groups.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author szgooru Created On 13-Dec-2019
 */
public class CommandAttributeConstants {
  
  private CommandAttributeConstants() {
    throw new AssertionError();
  }

  public static final String GROUP_ID = "groupId";
  public static final String STATE_ID = "stateId";
  public static final String COUNTRY_ID = "countryId";
  public static final String FREQUENCY = "frequency";
  public static final String WEEK = "week";
  public static final String MONTH = "month";
  public static final String YEAR = "year";
  public static final String SUBJECT = "subject";
  public static final String FRAMEWORK = "framework";
  
  public final static String FREQUENCY_WEEKLY = "weekly";
  public final static String FREQUENCY_MONTHLY = "monthly";
  public final static List<String> VALID_FREQUENCY = Arrays.asList(FREQUENCY_WEEKLY, FREQUENCY_MONTHLY);
}
