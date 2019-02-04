
package org.gooru.groups.reports.classes.summary;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author renuka
 */
public interface ClassTimespentSummaryDao {

  @SqlQuery("select sum(time_spent) as time_spent FROM base_reports WHERE unit_id is NOT NULL AND event_name = 'collection.resource.play' AND event_type = 'stop' AND (path_id IS NULL OR path_id = 0) AND date_in_time_zone BETWEEN :fromDate AND :toDate and class_id =:classId")
  Long fetchClassTimespentInWeek(@BindBean ClassSummaryBean bean);

}
