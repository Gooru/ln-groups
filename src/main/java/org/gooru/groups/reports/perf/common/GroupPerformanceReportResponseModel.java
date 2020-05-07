package org.gooru.groups.reports.perf.common;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author szgooru on 05-May-2020
 *
 */
public class GroupPerformanceReportResponseModel {

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
