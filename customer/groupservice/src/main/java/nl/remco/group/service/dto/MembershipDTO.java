package nl.remco.group.service.dto;

public class MembershipDTO {
	private String rol;
	private PersonDTO persoon;
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public PersonDTO getPersoon() {
		return persoon;
	}
	public void setPersoon(PersonDTO persoon) {
		this.persoon = persoon;
	}

}
