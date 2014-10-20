package org.pikater.shared.database.jpa;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.pikater.shared.database.jpa.status.JPAExperimentStatus;
import org.pikater.shared.database.jpa.status.JPAModelStrategy;

/**
 * Class {@link JPAExperiment} represents record about the experiment, that
 * is part of a batch.
 * <p>
 * This class also contains a list of results, that were created by the certain experiment.
 */
@Entity
@Table(name = "Experiment")
@NamedQueries({
		@NamedQuery(name = "Experiment.getAll", query = "select exp from JPAExperiment exp"),
		@NamedQuery(name = "Experiment.getByBatch", query = "select exp from JPAExperiment exp where exp.batch=:batch"),
		@NamedQuery(name = "Experiment.getByBatchWithModel", query = "select exp from JPAExperiment exp where exp.batch=:batch and exists (select res from JPAResult res where res member of exp.results and res.createdModel is not null)"),
		@NamedQuery(name = "Experiment.getByBatchWithModel.count", query = "select count(exp) from JPAExperiment exp where exp.batch=:batch and exists (select res from JPAResult res where res member of exp.results and res.createdModel is not null)"),
		@NamedQuery(name = "Experiment.getByStatus", query = "select exp from JPAExperiment exp where exp.status=:status") })
public class JPAExperiment extends JPAAbstractEntity {

	@Enumerated(EnumType.STRING)
	private JPAExperimentStatus status;
	@ManyToOne
	private JPABatch batch;
	@ManyToOne
	private JPAModel usedModel;
	@Enumerated(EnumType.STRING)
	private JPAModelStrategy modelStrategy;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<JPAResult> results;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date started;
	@Temporal(TemporalType.TIMESTAMP)
	private Date finished;

	/**
	 * Constructor for JPA Compatibility
	 */
	public JPAExperiment() {
	}

	/**
	 * Constructor for new Experiment for the given Batch, but with no
	 * model defined
	 * @param parentBatch The batch, where the Experiment belongs
	 */
	public JPAExperiment(JPABatch parentBatch) {
		this.setBatch(parentBatch);
		this.created = new Date();
		this.started = this.created;
		this.modelStrategy = JPAModelStrategy.CREATION;
	}

	/**
	 * Constructor for new Experiment for the given Batch and Model, that is
	 * used for the Experiment
	 * @param parentBatch The batch, where the experiment belongs
	 * @param model The model used for the experiment
	 */
	public JPAExperiment(JPABatch parentBatch, JPAModel model) {
		this.setBatch(parentBatch);
		this.created = new Date();
		this.started = this.created;
		this.modelStrategy = JPAModelStrategy.EXISTING;
		this.usedModel = model;
	}

	public JPAExperimentStatus getStatus() {
		return status;
	}

	public void setStatus(JPAExperimentStatus status) {
		this.status = status;
	}

	public void setStatus(String status) {
		this.setStatus(JPAExperimentStatus.valueOf(status));
	}

	public JPABatch getBatch() {
		return batch;
	}

	public void setBatch(JPABatch batch) {
		this.batch = batch;
	}

	public JPAModel getUsedModel() {
		return usedModel;
	}

	public void setUsedModel(JPAModel usedModel) {
		this.usedModel = usedModel;
	}

	public JPAModelStrategy getModelStrategy() {
		return modelStrategy;
	}

	public void setModelStrategy(JPAModelStrategy modelStrategy) {
		this.modelStrategy = modelStrategy;
	}

	public List<JPAResult> getResults() {
		return results;
	}

	public void setResults(List<JPAResult> results) {
		this.results = results;
	}

	public void addResult(JPAResult result) {
		if (this.results == null) {
			this.results = new LinkedList<JPAResult>();
		}
		this.results.add(result);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

	@Transient
	public static final String ENTITYNAME = "Experiment";

	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAExperiment updateValues = (JPAExperiment) newValues;
		this.batch = updateValues.getBatch();
		this.created = updateValues.getCreated();
		this.finished = updateValues.getFinished();
		this.usedModel = updateValues.getUsedModel();
		this.modelStrategy = updateValues.getModelStrategy();
		this.results = updateValues.getResults();
		this.started = updateValues.getStarted();
		this.status = updateValues.getStatus();
	}

}
