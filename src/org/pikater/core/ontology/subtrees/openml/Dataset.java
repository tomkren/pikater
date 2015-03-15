package org.pikater.core.ontology.subtrees.openml;

import jade.content.Concept;

public class Dataset  implements Concept {

	private static final long serialVersionUID = 6416588079335103563L;

	private String name;
	private String date;
	private long did;
    private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public long getDid() {
		return did;
	}
	public void setDid(long did) {
		this.did = did;
	}
}
