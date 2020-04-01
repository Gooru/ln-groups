package org.gooru.groups.hierarchies.users;

import java.util.List;

/**
 * @author szgooru on 31-Mar-2020
 *
 */
public class FetchUserGroupHierarchiesResponseModel {

  List<Hierarchy> hierarchies;

  public List<Hierarchy> getHierarchies() {
    return hierarchies;
  }

  public void setHierarchies(List<Hierarchy> hierarchies) {
    this.hierarchies = hierarchies;
  }

  static class Hierarchy {
    Long id;
    String name;
    String description;

    List<Tenant> tenants;

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

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public List<Tenant> getTenants() {
      return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
      this.tenants = tenants;
    }

  }

  static class Tenant {
    String id;
    String name;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

  }
}
