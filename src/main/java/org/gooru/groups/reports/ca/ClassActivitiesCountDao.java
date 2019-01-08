
package org.gooru.groups.reports.ca;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 07-Jan-2019
 */
public interface ClassActivitiesCountDao {

  @Mapper(ClassActivityMapper.class)
  @SqlQuery("SELECT id, activation_date FROM class_contents WHERE class_id = :classId::uuid AND for_month = :month AND for_year = :year")
  List<ClassActivity> fetchClassActivities(@BindBean ClassActivitiesCountBean bean);
}
