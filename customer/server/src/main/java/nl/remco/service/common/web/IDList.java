package nl.remco.service.common.web;

import java.util.ArrayList;
import java.util.List;


public class IDList {

	private List<String> ids;
	public IDList()
	{
		
	}
	public IDList(String id)
	{
		setIds(new ArrayList<String>());
		getIds().add( id);
	}
	
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	
	public static List<String> create( String id)
	{
		List<String> ids= new ArrayList<String>();
		ids.add( id);
		return ids;
	}

}
