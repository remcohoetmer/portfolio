package nl.remco.group.domain;

import java.util.ArrayList;
import java.util.List;


public class Group  {

	private String id;

	private String name;
	private String status;
	private String beschrijving;
	private String groepscode;
	private String product;
	private String geplandePeriode;
	private Scope scope;
	private Organisation organisatie;
	private List<Membership> memberships;
	private List<String> kenmerken;
	private Group hoofdgroep;
	private String createdBy;


	public String getGroepscode() {
		return groepscode;
	}
	public void setGroepscode(String groepscode) {
		this.groepscode = groepscode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}

	public String getGeplandePeriode() {
		return geplandePeriode;
	}
	public void setGeplandePeriode(String geplandePeriode) {
		this.geplandePeriode = geplandePeriode;
	}
	public Scope getScope() {
		return scope;
	}
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	public Organisation getOrganisatie() {
		return organisatie;
	}
	public void setOrganisatie(Organisation organisatie) {
		this.organisatie = organisatie;
	}

	public List<String> getKenmerken() {
		return kenmerken;
	}
	public void setKenmerken(List<String> kenmerken) {
		this.kenmerken = kenmerken;
	}


	public List<Membership> getMemberships() {
		if (memberships==null) {
			memberships= new ArrayList<Membership>();
		}
		return memberships;
	}
	public void setMemberships(List<Membership> lidmaatschappen) {
		this.memberships = lidmaatschappen;
	}
	public String getName() {
		return name;
	}
	public void setName(String naam) {
		this.name = naam;
	}
	public String getDescription() {
		return beschrijving;
	}
	public void setDescription(String beschrijving) {
		this.beschrijving = beschrijving;
	}
	public Group getHoofdgroep() {
		return hoofdgroep;
	}
	public void setHoofdgroep(Group hoofdgroep) {
		this.hoofdgroep = hoofdgroep;
	}



	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	@Override
	public String toString() {
		return String.format(
				"Group[id=%s, name=%s, status=%s]",
				this.getId(),
				this.name,
				this.getStatus()
				);
	}
    public Group() {}

    private Group(Builder builder) {
        this.name = builder.name;
        this.setStatus(builder.status);
    }

    public static Builder getBuilder() {
        return new Builder();
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public static class Builder {

		private String name;
		private String status;
		private Builder() {}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Group build() {
			Group build = new Group(this);


			return build;
		}
	}
	public void update(String status2, String name2) {
		status= status2;
		name=name2;
		
	}

}
