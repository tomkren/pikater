package org.pikater.core.ontology.experiment;

import jade.content.AgentAction;

public class SavedExperiment implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 852902583851957441L;

	private int savedExperimentId;
	private String message;

	public int getSavedExperimentId() {
		return savedExperimentId;
	}
	public void setSavedExperimentId(int savedExperimentId) {
		this.savedExperimentId = savedExperimentId;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
