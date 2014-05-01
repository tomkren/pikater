package org.pikater.core.ontology.batch;


import jade.content.AgentAction;

public class SaveBatch implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9121350435414635720L;

	private Batch batch;

	public Batch getBatch() {
		return batch;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
}