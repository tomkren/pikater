package org.pikater.core.agents.gateway.getAgentInfo;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.agents.gateway.PikaterGateway_General;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;

public class PikaterGateway_GetAgentInfo {

	public static AgentInfos getAgentInfos(int userID) throws PikaterGatewayException {
		
		AgentInfos agentInfos = null;
		
		try {	        
			GetAgentInfos getAgentInfos = new GetAgentInfos();
			getAgentInfos.setUserID(userID);

			Codec codec = new SLCodec();
			Ontology agentInfoOntology = AgentInfoOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					AgentNames.AGENTINFO_MANAGER, agentInfoOntology,
					getAgentInfos);

			GetAgentInfoInitiator initiator = new GetAgentInfoInitiator(msg);
			PikaterGateway_General.generalRequest(initiator);

			agentInfos = initiator.getAgentInfosResponse(codec,
					agentInfoOntology);

		} catch (ControllerException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} catch (CodecException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} catch (OntologyException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} 

		return agentInfos;
	}

}
