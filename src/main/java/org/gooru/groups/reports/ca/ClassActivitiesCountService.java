
package org.gooru.groups.reports.ca;

import java.util.List;
import org.skife.jdbi.v2.DBI;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 07-Jan-2019
 */
public class ClassActivitiesCountService {

  private final ClassActivitiesCountDao dao;

  public ClassActivitiesCountService(DBI dbi) {
    this.dao = dbi.onDemand(ClassActivitiesCountDao.class);
  }

  public JsonObject fetchClassActivitiesCount(ClassActivitiesCountBean bean) {
    List<ClassActivity> classActivities = this.dao.fetchClassActivities(bean);
    int scheduledCount = 0;
    int unscheduledCount = 0;
    for (ClassActivity ca : classActivities) {
      if (ca.getActivationDate() == null) {
        unscheduledCount = unscheduledCount + 1;
      } else {
        scheduledCount = scheduledCount + 1;
      }
    }

    return new JsonObject().put("scheduled", scheduledCount).put("unscheduled", unscheduledCount);
  }
}
