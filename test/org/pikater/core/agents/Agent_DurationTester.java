package org.pikater.core.agents;

import jade.content.onto.Ontology;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TaskOntology;


public class Agent_DurationTester extends PikaterAgent {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6420194571183822412L;

	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		return ontologies;
	}

	@Override
    protected void setup() {
        initDefault();

		registerWithDF("DurationTester");
		
		//TODO: test
        
        logInfo("DurationTester ending");
        doDelete();
	}

}
