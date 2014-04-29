package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JPAFilemapping {
		
	private int userid;
	private String externalfilename;

    @Id
	private String internalfilename;
	
		
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	public String getExternalfilename() {
		return externalfilename;
	}
	public void setExternalfilename(String externalfilename) {
		this.externalfilename = externalfilename;
	}
	
	public String getInternalfilename() {
		return internalfilename;
	}
	public void setInternalfilename(String internalfilename) {
		this.internalfilename = internalfilename;
	}
}

