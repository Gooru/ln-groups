package org.gooru.groups.reports.perf.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceReportResponseForClassModel {
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
