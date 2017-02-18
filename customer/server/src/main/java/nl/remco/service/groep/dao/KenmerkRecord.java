package nl.remco.service.groep.dao;

public class KenmerkRecord {
	private String id;
	private String groepId;
	private String kenmerk;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKenmerk() {
		return kenmerk;
	}
	public void setKenmerk(String kenmerk) {
		this.kenmerk = kenmerk;
	}
	public String getGroepId() {
		return groepId;
	}
	public void setGroepId(String groepId) {
		this.groepId = groepId;
	}
}
