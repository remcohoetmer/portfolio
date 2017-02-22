package nl.remco.service.common.model;

public abstract class LifeCycleBeheer extends Identifiable {
	public enum Status { Actief, Passief, Verwijderd};

	protected Status status;

	protected LifeCycleBeheer(String id) {
		super(id);
	}
	protected LifeCycleBeheer() {
		super();
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

}
