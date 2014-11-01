package org.pikater.core.agents;

import jade.content.onto.Ontology;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.manager.ManagerAgentService;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ModelOntology;

public class LoadModelTester extends PikaterAgent {
	private static final long serialVersionUID = 1677484717124329173L;

	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(ModelOntology.getInstance());
		return ontologies;
	}

	@Override
	protected void setup() {
		initDefault();

		logInfo("doing request");
		ManagerAgentService.loadAgent(this, 83554);
		logInfo("load returned");

		logInfo("LoadModelTester ending");
		doDelete();
	}
}
