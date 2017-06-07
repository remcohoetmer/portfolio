package nl.remco.group.service.domain;


public class Scope {

  private String id;
  private String name;
  private String status;

  public Scope() {
  }

  public Scope(String id) {
    setId(id);
  }

  public Scope(String id, String string) {
    setId(id);
    setName(string);
  }

  public void setId(String id) {
    this.id = id;
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

  @Override
  public String toString() {
    return String.format(
      "Scope[id=%s, name=%s, status=%s]",
      this.getId(),
      this.getName(),
      this.getStatus()
    );
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
