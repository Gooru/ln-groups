package org.gooru.groups.reports.competency.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 24-Apr-2020
 *
 */
public class GroupCompetencyReportResponseModel {
  @JsonProperty("context")
  private ContextModel context;

  @JsonProperty("data")
  private List<DataModel> data;

  public ContextModel getContext() {
    return context;
  }

  public void setContext(ContextModel context) {
    this.context = context;
  }

  public List<DataModel> getData() {
    return data;
  }

  public void setData(List<DataModel> data) {
    this.data = data;
  }


}
