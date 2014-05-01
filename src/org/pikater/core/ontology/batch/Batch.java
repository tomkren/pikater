package org.pikater.core.ontology.batch;

import org.pikater.core.ontology.description.ComputationDescription;

import jade.content.Concept;

public class Batch implements Concept {

	private static final long serialVersionUID = -7028457864866356063L;

	private Long id;

	private String name;
	private String note;

	private long xmlOID;
	
	private int ownerID;

	private int priority;
	
	private ComputationDescription description;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public long getXmlOID() {
		return xmlOID;
	}
	public void setXmlOID(long xmlOID) {
		this.xmlOID = xmlOID;
	}

	public int getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ComputationDescription getDescription() {
		return description;
	}
	public void setDescription(ComputationDescription description) {
		this.description = description;
	}
}