
package org.gooru.groups.reports.competency.group;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gooru.groups.app.data.EventBusMessage;
import org.gooru.groups.app.jdbi.DBICreator;
import org.gooru.groups.constants.CommandAttributeConstants;
import org.gooru.groups.processors.MessageProcessor;
import org.gooru.groups.reports.competency.dbhelpers.GroupCompetencyReportService;
import org.gooru.groups.reports.dbhelpers.core.CoreService;
import org.gooru.groups.reports.dbhelpers.core.DrilldownModel;
import org.gooru.groups.reports.dbhelpers.core.GroupModel;
import org.gooru.groups.responses.MessageResponse;
import org.gooru.groups.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 16-Dec-2019
 */
public class GroupCompetencyReportByGroupProcessor implements MessageProcessor {

  private final static Logger LOGGER =
      LoggerFactory.getLogger(GroupCompetencyReportByGroupProcessor.class);
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private final GroupCompetencyReportService reportService =
      new GroupCompetencyReportService(DBICreator.getDbiForDsdbDS());
  private final CoreService coreService = new CoreService(DBICreator.getDbiForDefaultDS());

  public GroupCompetencyReportByGroupProcessor(Vertx vertx, Message<JsonObject> message) {
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      EventBusMessage ebMessage = EventBusMessage.eventBusMessageBuilder(this.message);

      GroupCompetencyReportByGroupCommand command =
          GroupCompetencyReportByGroupCommand.build(ebMessage.getRequestBody());
      GroupCompetencyReportByGroupCommand.GroupCompetencyReportByGroupCommandBean bean =
          command.asBean();

      GroupModel group = this.coreService.fetchGroupById(command.getGroupId());
      Double averagePerformance = this.reportService.fetchAveragePerformanceByGroup(bean);

      GroupCompentencyReportByGroupReponseModel responseModel = null;

      if (group.getSubType().equalsIgnoreCase(CommandAttributeConstants.GROUP_TYPE_SCHOOL_DISTRICT)
          || group.getSubType().equalsIgnoreCase(CommandAttributeConstants.GROUP_TYPE_CLUSTER)) {
        // Fetch group to school mapping
        Set<Long> allSchoolsByGroup = this.coreService.fetchSchoolsByGroup(command.getGroupId());
        List<GroupCompetencyReportByGroupModel> competencyReportByWeek =
            this.reportService.fetchGroupCompetencyReportBySDorCluster(allSchoolsByGroup, bean);
        List<GroupCompetencyDrillDownReportByGroupOrSchoolModel> competencyReportBySchool =
            this.reportService.fetchGroupCompetencySchoolWiseReportBySDorCluster(allSchoolsByGroup,
                bean);
        Set<Long> schoolIds = new HashSet<>();
        competencyReportBySchool.forEach(model -> {
          schoolIds.add(model.getId());
        });
        Map<Long, DrilldownModel> schoolModels = this.coreService.fetchSchoolDetails(schoolIds);
        responseModel = GroupCompentencyReportByGroupReponseModelBuilder.buildReponseForSDorCluster(
            competencyReportByWeek, competencyReportBySchool, schoolModels, averagePerformance);
      } else if (group.getSubType().equalsIgnoreCase(CommandAttributeConstants.GROUP_TYPE_DISTRICT)
          || group.getSubType().equalsIgnoreCase(CommandAttributeConstants.GROUP_TYPE_BLOCK)) {
        Map<Long, GroupModel> allGroupsByParent =
            this.coreService.fetchGroupsByParent(command.getGroupId());
        List<GroupCompetencyDrillDownReportByGroupOrSchoolModel> competencyReportByGroup =
            this.reportService.fetchGroupCompetencyGroupWiseReportByDistrictOrBlock(
                allGroupsByParent.keySet(), bean);
        List<GroupCompetencyReportByGroupModel> competencyReportByWeek = this.reportService
            .fetchGroupCompetencyReportByDistrictOrBlock(allGroupsByParent.keySet(), bean);
        Set<Long> groupIds = new HashSet<>();
        competencyReportByGroup.forEach(model -> {
          groupIds.add(model.getId());
        });
        Map<Long, GroupModel> groupDetails = this.coreService.fetchGroupDetails(groupIds);

        responseModel =
            GroupCompentencyReportByGroupReponseModelBuilder.buildReponseForDistrictorBlock(
                competencyReportByWeek, competencyReportByGroup, groupDetails, averagePerformance);
      }

      String resultString = new ObjectMapper().writeValueAsString(responseModel);
      result.complete(MessageResponseFactory.createOkayResponse(new JsonObject(resultString)));
    } catch (Throwable t) {
      LOGGER.warn("exception while fetching competency report by group", t);
      result.fail(t);
    }

    return this.result;
  }

}
