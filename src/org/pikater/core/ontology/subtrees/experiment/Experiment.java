package org.pikater.core.ontology.subtrees.experiment;

import jade.content.Concept;

public class Experiment implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2725442756148463745L;

	private int id;
	private String status;
	private int batchID;
	private int workflow;
	private int modelID;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public int getBatchID() {
		return batchID;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	public int getWorkflow() {
		return workflow;
	}
	public void setWorkflow(int workflow) {
		this.workflow = workflow;
	}

	public int getModelID() {
		return modelID;
	}
	public void setModelID(int modelID) {
		this.modelID = modelID;
	}

}
