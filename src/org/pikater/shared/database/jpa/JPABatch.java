package org.pikater.shared.database.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JPABatch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String note;

	private long xmlOID;
	
	private JPAUser owner;

	private int priority;
	private int totalPriority;
	
	private List<JPAExperiment> experiments = new ArrayList<JPAExperiment>();


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
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

	public JPAUser getOwner() {
		return owner;
	}
	public void setOwner(JPAUser owner) {
		this.owner = owner;
	}

	public void setPriority(int priority){
		this.priority=priority;
	}
	public int getPriority(){
		return this.priority;
	}
	
	public void setTotalPriority(int totalPriority){
		this.totalPriority=totalPriority;
	}
	public int getTotalPriority(){
		return this.totalPriority;
	}
	
	public List<JPAExperiment> getExperiments() {
		return experiments;
	}
	public void setExperiments(List<JPAExperiment> experiments) {
		this.experiments = experiments;
	}
	public void addExperiment(JPAExperiment experiment) {
		this.experiments.add(experiment);
	}
}
