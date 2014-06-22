package org.pikater.core.agents.experiment.virtual;

import jade.content.onto.Ontology;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;

public abstract class Agent_VirtualBoxProvider extends Agent_AbstractExperiment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7569621739765373832L;

	@Override
	public java.util.List<Ontology> getOntologies() {

		java.util.List<Ontology> ontologies = new java.util.ArrayList<Ontology>();
		ontologies.add(AgentInfoOntology.getInstance());

		return ontologies;
	}

	@Override
	protected void setup() {

		initDefault();

		registerWithDF(AgentNames.VIRTUAL_BOX_PROVIDER);

		AgentInfo agentInfo = getAgentInfo();
		sendAgentInfo(agentInfo);

	}

}
