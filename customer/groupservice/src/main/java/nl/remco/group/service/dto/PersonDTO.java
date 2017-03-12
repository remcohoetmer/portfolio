package nl.remco.group.service.dto;

import java.util.Date;

public class PersonDTO {
	private String id;
	private String name;
	private String surname;
	private String email;
	private Date dateofbirth;
	private OrganisationDTO organisation;
	
	public PersonDTO() {
	}
	public PersonDTO(String name) {
		this.setId(name);
		this.setName(name);
	
	}

	public PersonDTO(String id, String name, String surname, String email,
			Date dateofbirth,
			OrganisationDTO organisation) {
		this.id=id;
		this.name=name;
		this.surname=surname;
		this.email=email;
		this.dateofbirth=dateofbirth;
		this.organisation=organisation;

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
	public Date getDateofbirth() {
		return dateofbirth;
	}
	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public OrganisationDTO getOrganisation() {
		return organisation;
	}
	public void setOrganisation(OrganisationDTO organisation) {
		this.organisation = organisation;
	}
	@Override
	public String toString() {
		return String.format("{PersonDTO id=%s name=%s}", id, name);
	}
}
