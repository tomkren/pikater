package org.pikater.core.ontology.subtrees.experiment;

import jade.content.AgentAction;

public class UpdateExperimentStatus implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4716415921573648055L;

	private int experimentID;
	private String status;

	public int getExperimentID() {
		return experimentID;
	}

	public void setExperimentID(int experimentID) {
		this.experimentID = experimentID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
