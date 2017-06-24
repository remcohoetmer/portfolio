package nl.remco.group.service.dto;

public class MembershipDTO {
  private String role;
  private PersonDTO person;

  public String getRole() {
    return role;
  }

  public void setRole(String rol) {
    this.role = rol;
  }

  public PersonDTO getPerson() {
    return person;
  }

  public void setPerson(PersonDTO person) {
    this.person = person;
  }

  @Override
  public String toString() {
    return String.format("{MembershipDTO person=%s rol=%s}", person, role);
  }

}
