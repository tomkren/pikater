package org.pikater.shared.database.jpa;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.pikater.core.ontology.subtrees.experiment.experimentStatuses.ExperimentStatuses;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;

@Entity
@Table(name="Experiment")
@NamedQueries({
	@NamedQuery(name="Experiment.getAll",query="select exp from JPAExperiment exp"),
	@NamedQuery(name="Experiment.getByID",query="select exp from JPAExperiment exp where exp.id=:id"),
	@NamedQuery(name="Experiment.getByBatch",query="select exp from JPAExperiment exp where exp.batch=:batch"),
	@NamedQuery(name="Experiment.getByStatus",query="select exp from JPAExperiment exp where exp.status=:status")
})
public class JPAExperiment extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	// nejsem si jisty, jaky je ucel tohohle a jestli BINARY(1) v diagramu znamena boolean nebo byte
    @Column(nullable = false)
	private byte workflow;
    @Enumerated(EnumType.STRING)
	private JPAExperimentStatus status;
    @ManyToOne
	private JPABatch batch;
	private JPAModel model;
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<JPAResult> results;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar started;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar finished;


	public int getId() {
        return id;
    }

    public byte getWorkflow() {
        return workflow;
    }
    public void setWorkflow(byte workflow) {
        this.workflow = workflow;
    }

	public JPAExperimentStatus getStatus() {
		return status;
	}
	public void setStatus(JPAExperimentStatus status) {
		this.status = status;
	}
	public void setStatus(String status) {

		if ( status.equals(ExperimentStatuses.WAITING) ) {
			this.status = JPAExperimentStatus.WAITING;
		} else if ( status.equals(ExperimentStatuses.COMPUTING) ) {
			this.status = JPAExperimentStatus.STARTED;
		} else if ( status.equals(ExperimentStatuses.FINISHED) ) {
			this.status = JPAExperimentStatus.FINISHED;
		} else if ( status.equals(ExperimentStatuses.FAILED) ) {
			this.status = JPAExperimentStatus.FAILED;
		} else {
			throw new IllegalArgumentException("Experiment status is not valid");
		}
	}
	
	public JPABatch getBatch() {
		return batch;
	}
	public void setBatch(JPABatch batch) {
		this.batch = batch;
	}

	public JPAModel getModel() {
		return model;
	}
	public void setModel(JPAModel model) {
		this.model = model;
	}

	public List<JPAResult> getResults() {
		return results;
	}
	public void setResults(List<JPAResult> results) {
		this.results = results;
	}
	public void addResult(JPAResult result) {
		if(this.results==null){
			this.results=new LinkedList<JPAResult>();
		}
		this.results.add(result);
	}

	public Calendar getCreated() {
		return created;
	}
	public void setCreated(Calendar created) {
		this.created = created;
	}
	public Calendar getStarted() {
		return started;
	}

	public void setStarted(Calendar started) {
		this.started = started;
	}
	public Calendar getFinished() {
		return finished;
	}
	public void setFinished(Calendar finished) {
		this.finished = finished;
	}

	@Override
	public String getEntityName() {
		return "Experiment";
	}
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAExperiment updateValues=(JPAExperiment)newValues;
		this.batch=updateValues.getBatch();
		this.created=updateValues.getCreated();
		this.finished=updateValues.getFinished();
		this.model=updateValues.getModel();
		this.results=updateValues.getResults();
		this.started=updateValues.getStarted();
		this.status=updateValues.getStatus();
		this.workflow=updateValues.getWorkflow();
	}

}
