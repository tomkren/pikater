package org.pikater.shared.database.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class JPABatch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private int ownerId;
	private int priority;
	private int totalPriority;
	
	private List<JPAExperiment> experiments = new ArrayList<JPAExperiment>();

//	@Transient
//	private static final String PERSISTENCE_UNIT_NAME = "batches";
//	@Transient
//	private static EntityManagerFactory factory;
	
	
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
	
	public void setOwnerID(int id){
		this.ownerId=id;
	}
	public int getOwnerID(){
		return this.ownerId;
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
