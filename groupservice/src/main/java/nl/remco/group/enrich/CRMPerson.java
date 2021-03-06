package nl.remco.group.enrich;

import java.util.Date;

import nl.remco.group.organisation.service.CRMOrganisation;

public class CRMPerson {
	private String id;
	private String name;
	private String surname;
	private String email;
	private Date dateofbirth;
	private CRMOrganisation organisation;
	
	public CRMPerson() {
	}
	public CRMPerson(String name) {
		this.setId(name);
		this.setName(name);
	
	}

	public CRMPerson(String id, String name, String surname, String email,
			Date dateofbirth,
			CRMOrganisation organisation) {
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
	public CRMOrganisation getOrganisation() {
		return organisation;
	}
	public void setOrganisation(CRMOrganisation organisation) {
		this.organisation = organisation;
	}
}
