package nl.remco.group.service.dto;

public class ScopeDTO {
  public ScopeDTO(String id) {
    this.id = id;
  }

  public ScopeDTO() {
  }

  @Override
  public String toString() {
    return "ScopeDTO{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      '}';
  }

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

  private String id;

  private String name;
}
