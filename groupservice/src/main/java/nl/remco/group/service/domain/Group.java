package nl.remco.group.service.domain;

import java.util.ArrayList;
import java.util.List;


public class Group {
  private String id;
  private String name;
  private String status;
  private String description;
  private String code;
  private Scope scope;
  private Organisation organisation;
  private List<Membership> memberships;
  private List<String> features;
  private Group master;
  private String createdBy;

  public Group(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Scope getScope() {
    return scope;
  }

  public void setScope(Scope scope) {
    this.scope = scope;
  }

  public Organisation getOrganisation() {
    return organisation;
  }

  public void setOrganisation(Organisation organisation) {
    this.organisation = organisation;
  }

  public List<String> getFeatures() {
    if (features == null) {
      features = new ArrayList<>();
    }
    return features;
  }

  public void setMemberships(List<Membership> memberships) {
    this.memberships = memberships;
  }

  public List<Membership> getMemberships() {
    if (memberships == null) {
      memberships = new ArrayList<Membership>();
    }
    return memberships;
  }

  public String getName() {
    return name;
  }

  public void setName(String naam) {
    this.name = naam;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Group getMaster() {
    return master;
  }

  public void setMaster(Group master) {
    this.master = master;
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  @Override
  public String toString() {
    return String.format(
      "Group[id=%s, name=%s, status=%s]",
      this.getId(),
      this.name,
      this.getStatus()
    );
  }

  public Group() {
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void update(String status2, String name2) {
    status = status2;
    name = name2;

  }

  public static class Builder {

    private String name;
    private String description;
    private String status;
    private Organisation organisation;
    private Scope scope;
    private List<Membership> members;

    private Builder() {
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder status(String status) {
      this.status = status;
      return this;
    }

    public Group build() {
      Group group = new Group();
      group.setName(name);
      group.setDescription(description);
      group.setStatus(status);
      group.setOrganisation(organisation);
      group.setScope(scope);
      group.setMemberships(members);
      return group;
    }

    public Builder withOrganisation(Organisation organisation) {
      this.organisation = organisation;
      return this;
    }

    public Builder withScope(Scope scope) {
      this.scope = scope;
      return this;
    }

    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder withMembers(List<Membership> members) {
      this.members = members;
      return this;
    }
  }


}
