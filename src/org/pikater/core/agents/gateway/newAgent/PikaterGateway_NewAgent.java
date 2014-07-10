package org.pikater.core.agents.gateway.newAgent;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.agents.gateway.newBatch.NewBatchInitiator;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agent.NewAgent;

public class PikaterGateway_NewAgent {

	public static void newAgent(Class<?> agentClass) throws Exception {

		try {
	        JadeGateway.init(Agent_PikaterGateway.class.getName(), null);
	        
	        NewAgent newAgent = new NewAgent();
	        newAgent.setAgentClassName(agentClass.getName());

			Ontology agentInfoOntology = AgentInfoOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					AgentNames.AGENTINFO_MANAGER, agentInfoOntology,
					newAgent);

			NewAgentInfoInitiator initiator = new NewAgentInfoInitiator(msg);
			JadeGateway.execute(initiator, 10000);
			initiator.getOkResponse();

			JadeGateway.shutdown();

		} catch (CodecException | OntologyException e1) {
			throw new Exception("Failed to make request");
		} catch (ControllerException | InterruptedException e) {
			Exception x = new Exception("JadeGateway.execute() failed");
			x.setStackTrace(e.getStackTrace());
			throw x;
		}

	}

}
