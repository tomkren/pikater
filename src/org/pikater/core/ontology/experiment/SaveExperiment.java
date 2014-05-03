package org.pikater.core.ontology.experiment;

import jade.content.AgentAction;

public class SaveExperiment implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3208225049332843678L;
	
	private Experiment experiment;

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

}
