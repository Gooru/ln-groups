
package org.gooru.groups.reports.competency.dbhelpers;

import java.util.List;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryCommand;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryModel;
import org.gooru.groups.reports.competency.country.GroupCompetencyReportByCountryModelMapper;
import org.gooru.groups.reports.competency.country.GroupCompetencyStateWiseReportByCountryModel;
import org.gooru.groups.reports.competency.country.GroupCompetencyStateWiseReportByCountryModelMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public interface GroupCompetencyReportDao {

  @Mapper(GroupCompetencyReportByCountryModelMapper.class)
  @SqlQuery("SELECT week, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE country_id = :countryId AND"
      + " month = :month AND year = :year GROUP BY week")
  List<GroupCompetencyReportByCountryModel> fetchGroupCompetencyReportByCountry(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean);

  @Mapper(GroupCompetencyStateWiseReportByCountryModelMapper.class)
  @SqlQuery("SELECT state_id, SUM(completed_count) as completed_competencies FROM class_competency_data_reports WHERE country_id = :countryId AND"
      + " month = :month AND year = :year GROUP BY state_id")
  List<GroupCompetencyStateWiseReportByCountryModel> fetchGroupCompetencyStateWiseReportByCountry(
      @BindBean GroupCompetencyReportByCountryCommand.GroupCompetencyReportByCountryCommandBean bean);
}
