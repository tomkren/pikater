package org.pikater.shared.database.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="Batch_20140430")
public class JPABatch extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
	private String name;
	private String note;
	private String XML;
	@ManyToOne
	private JPAUser owner;
	private int priority;
	private int totalPriority;
	@Enumerated(EnumType.STRING)
	private BatchStatus status;
	
	@OneToMany
	private List<JPAExperiment> experiments = new ArrayList<JPAExperiment>();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar finished;

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

	public String getXML() {
		return XML;
	}
	public void setXML(String xML) {
		XML = xML;
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

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}
	public Calendar getFinished() {
		return finished;
	}
	public void setFinished(Calendar finished) {
		this.finished = finished;
	}
	public BatchStatus getStatus() {
		return status;
	}
	public void setStatus(BatchStatus status) {
		this.status = status;
	}
	@Override
	public String getEntityName() {
		return "Batch";
	}
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPABatch updateValues=(JPABatch)newValues;
		this.created=updateValues.getCreated();
		this.experiments=updateValues.getExperiments();
		this.finished=updateValues.getFinished();
		this.name=updateValues.getName();
		this.note=updateValues.getNote();
		this.owner=updateValues.getOwner();
		this.priority=updateValues.getPriority();
		this.status=updateValues.getStatus();
		this.totalPriority=updateValues.getTotalPriority();
		this.XML=updateValues.getXML();
	}
}
