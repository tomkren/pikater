package org.pikater.core.agents.gateway.newAgent;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.agents.gateway.Initiator;
import org.pikater.core.agents.gateway.PikaterGateway_General;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agent.NewAgent;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;
import org.pikater.shared.database.jpa.daos.DAOs;

public class PikaterGateway_NewAgent {

	public static void newAgent(int externalAgentID) throws PikaterGatewayException {
		try {
	        NewAgent newAgent = new NewAgent();
	        JPAExternalAgent ea = DAOs.externalAgentDAO.getByID(externalAgentID, EmptyResultAction.NULL);
	        if(ea!=null){
	        	newAgent.setAgentClassName(ea.getAgentClass());

	        	Ontology agentInfoOntology = AgentInfoOntology.getInstance();

	        	ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
	        			CoreAgents.AGENTINFO_MANAGER.getName(), agentInfoOntology,
	        			newAgent);

	        	Initiator initiator = new Initiator(msg);

	        	PikaterGateway_General.generalRequest(initiator);
	        	initiator.getOkResponse();
	        }

		} catch (ControllerException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} catch (CodecException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} catch (OntologyException e1) {
			throw new PikaterGatewayException("Failed to make request");
		}

	}

}
