package nl.remco.employee.service;

import org.springframework.data.annotation.Id;

public class Employee {

  @Id
  private String id;



  private String firstName;
  private String lastName;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  private String description;

  public Employee() {
  }


  @Override
  public String toString() {
    return "Employee{" +
      "id='" + id + '\'' +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      ", description='" + description + '\'' +
      '}';
  }
}
