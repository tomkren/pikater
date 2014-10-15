package org.pikater.core.ontology.subtrees.experiment;

import jade.content.Concept;

/**
 * Represents a part of a batch. Experiments are created by {@link Parser}.
 * 
 * @author stepan
 */
public class Experiment implements Concept {

	private static final long serialVersionUID = -2725442756148463745L;

	private int id;
	private String status;
	private int batchID;
	private int workflow;
	private Integer model;

	/**
	 * Get the {@link Experiment} ID
	 */
	public int getId() {
		return id;
	}
	/**
	 * Set the {@link Experiment} ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get the Status
	 */
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get the {@link Batch} ID
	 */
	public int getBatchID() {
		return batchID;
	}
	/**
	 * Set the {@link Batch} ID
	 */
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	/**
	 * Get the workflow
	 */
	public int getWorkflow() {
		return workflow;
	}
	/**
	 * Set the workflow
	 */
	public void setWorkflow(int workflow) {
		this.workflow = workflow;
	}

	/**
	 * Get the model ID
	 */
	public Integer getModel() {
		return model;
	}
	/**
	 * Set the model ID
	 */
	public void setModel(Integer model) {
		this.model = model;
	}

}
