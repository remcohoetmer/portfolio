package nl.remco.group.service;

public class PersonDTO {
	private String id;
	private String name;

	public PersonDTO(String name) {
		this.setId(name);
		this.setName(name);
	
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

}
