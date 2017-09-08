package nl.remco.scope.service.dto;

public final class ScopeDTO {

  private String id;

  private String name;

  private String status;

  public ScopeDTO() {

  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return String.format(
      "ScopeDTO[id=%s, withName=%s, status=%s]",
      this.id,
      this.name,
      this.status
    );
  }
}
