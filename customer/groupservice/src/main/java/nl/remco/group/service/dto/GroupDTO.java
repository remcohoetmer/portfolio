package nl.remco.group.service.dto;

import java.util.ArrayList;
import java.util.List;

public final class GroupDTO {
	private String id;
    private String name;
    private String status;
	private String description;
	private String code;
	private String product;
;
	private ScopeDTO scope;
	private OrganisationDTO organisation;
	private List<MembershipDTO> memberships;
	private List<String> features;
	private GroupDTO master;
	private String createdBy;

    public GroupDTO() {
    }

    public GroupDTO(String id) {
    	this.id=id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public ScopeDTO getScope() {
		return scope;
	}

	public void setScope(ScopeDTO scope) {
		this.scope = scope;
	}

	public OrganisationDTO getOrganisation() {
		return organisation;
	}

	public void setOrganisation(OrganisationDTO organisation) {
		this.organisation = organisation;
	}

	public List<MembershipDTO> getMemberships() {
		if (memberships==null) {
			memberships= new ArrayList<>();
		}
		return memberships;
	}

	public List<String> getFeatures() {
		if (features==null) {
			features= new ArrayList<>();
		}
		return features;
	}

	public GroupDTO getMaster() {
		return master;
	}

	public void setMaster(GroupDTO master) {
		this.master = master;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
                "ScopeDTO[id=%s, name=%s, status=%s]",
                this.id,
                this.name,
                this.status
        );
    }
}
