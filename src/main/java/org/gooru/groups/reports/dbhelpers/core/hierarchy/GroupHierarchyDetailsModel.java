/**
 * 
 */
package org.gooru.groups.reports.dbhelpers.core.hierarchy;

/**
 * @author szgooru
 *
 */
public class GroupHierarchyDetailsModel {
  
  private Long id;
  private String name;
  private String type;
  private Long hierarchyId;
  private Integer sequence;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getHierarchyId() {
    return hierarchyId;
  }

  public void setHierarchyId(Long hierarchyId) {
    this.hierarchyId = hierarchyId;
  }

  public Integer getSequence() {
    return sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }


}
