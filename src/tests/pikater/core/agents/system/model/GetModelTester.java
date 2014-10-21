package tests.pikater.core.agents.system.model;

import jade.content.onto.Ontology;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.subtrees.model.Model;

public class GetModelTester extends PikaterAgent {
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
		Model res = DataManagerService.getModel(this, 70704);
		logInfo("got response, agent size "+res.getSerializedAgent().length+", result ID "+res.getResultID());

		logInfo("GetModelTester ending");
		doDelete();
	}
}
