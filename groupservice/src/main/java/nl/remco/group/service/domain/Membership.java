package nl.remco.group.service.domain;


public class Membership {

  private String role;
  private Person person;

  public String getRole() {
    return role;
  }

  public void setRole(String rol) {
    this.role = rol;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }
  public Membership() {}

  public Membership(String role, Person person) {
    this.role = role;
    this.person = person;
  }

  @Override
  public String toString() {
    return "Membership " + person == null ? "(Unknown)" : person.toString();
  }
}
