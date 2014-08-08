package org.pikater.core.agents.gateway.newAgent;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.agents.gateway.Initiator;
import org.pikater.core.agents.gateway.PikaterGateway_General;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agent.NewAgent;

public class PikaterGateway_NewAgent {

	public static void newAgent(Class<?> agentClass) throws PikaterGatewayException {
		try {
	        NewAgent newAgent = new NewAgent();
	        newAgent.setAgentClassName(agentClass.getName());

			Ontology agentInfoOntology = AgentInfoOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					AgentNames.AGENTINFO_MANAGER, agentInfoOntology,
					newAgent);

			Initiator initiator = new Initiator(msg);

			PikaterGateway_General.generalRequest(initiator);
			initiator.getOkResponse();

		} catch (ControllerException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} catch (CodecException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} catch (OntologyException e1) {
			throw new PikaterGatewayException("Failed to make request");
		}

	}

}
