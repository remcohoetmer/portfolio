package nl.remco.group.service;

import java.util.List;

public final class GroupDTO {
	private String id;
    private String name;
    private String status;
	private String description;
	private String code;
	private String product;
	private String period;
	private ScopeDTO scope;
	private OrganisationDTO organisation;
	private List<MembershipDTO> memberships;
	private List<String> features;
	private GroupDTO master;
	private String createdBy;

    public GroupDTO() {
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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
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
		return memberships;
	}

	public void setMemberships(List<MembershipDTO> memberships) {
		this.memberships = memberships;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
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
