package org.gooru.groups.reports.competency.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 24-Apr-2020
 *
 */
public class GroupCompetencyReportResponseForClassModel {
  @JsonProperty("context")
  private ContextModel context;

  @JsonProperty("data")
  private List<DataModelForClass> data;

  public ContextModel getContext() {
    return context;
  }

  public void setContext(ContextModel context) {
    this.context = context;
  }

  public List<DataModelForClass> getData() {
    return data;
  }

  public void setData(List<DataModelForClass> data) {
    this.data = data;
  }


}
