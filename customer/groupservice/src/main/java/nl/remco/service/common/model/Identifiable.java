package nl.remco.service.common.model;

import java.util.Date;

public class Identifiable {
	protected String id;
	
	private Date laatstgewijzigd;
	

	public Date getLaatstgewijzigd() {
		return laatstgewijzigd;
	}

	public void setLaatstgewijzigd(Date laatstgewijzigd) {
		this.laatstgewijzigd = laatstgewijzigd;
	}
	public Identifiable() {
	}
	public Identifiable(String l) {
		setId( l);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public boolean equals( Object obj)
	{
		if (getId()==null) {
			return false;
		}

		if (obj!= null && obj instanceof Identifiable) {
			Identifiable org= (Identifiable) obj;
			return getId().equals(org.getId());
		}
		return false;
	}
	@Override
	public String toString()
	{
		return "Object van " + id;
	}
	
	public Identifiable copy()
	{
		Identifiable object= new Identifiable();
		object.setId( this.id);
		object.setLaatstgewijzigd( this.laatstgewijzigd);
		return object;
	}
	
}
