package nl.remco.service.scope.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nl.remco.service.jsonutils.CustomJsonTimestampDeserializer;
import nl.remco.service.jsonutils.CustomJsonTimestampSerializer;


public class Scope {
	public static enum Status { Active, Inactive};
	private String id;
	private String name;
	private Status status;
	private Date modified_time;
	private String created_user;

	
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

	@JsonSerialize(using = CustomJsonTimestampSerializer.class)
	public Date getModified_time() {
		return modified_time;
	}
	@JsonDeserialize(using = CustomJsonTimestampDeserializer.class)
	public void setModified_time(Date modified_time) {
		this.modified_time = modified_time;
	}
	public String getCreated_user() {
		return created_user;
	}
	public void setCreated_user(String created_user) {
		this.created_user = created_user;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}


}
