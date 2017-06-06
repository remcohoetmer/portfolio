package nl.cerios.rsclient.dto;

public class OrganisationDTO {
	private String id;
	private String name;
	private String status;

	public OrganisationDTO(String id) {
		this.id=id;
	}

	public OrganisationDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

}
