package org.pikater.core.agents.gateway.getAgentInfo;

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;

public class PikaterGateway_GetAgentInfo {

	public static AgentInfos getAgentInfos() throws Exception {

		AgentInfos agentInfos = null;
		try {
	        JadeGateway.init(Agent_PikaterGateway.class.getName(), null);
	        
			AgentAction getAgentInfos = new GetAgentInfos();

			Codec codec = new SLCodec();
			Ontology agentInfoOntology = AgentInfoOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					AgentNames.AGENTINFO_MANAGER, agentInfoOntology,
					getAgentInfos);

			GetAgentInfoIntitiator initiator = new GetAgentInfoIntitiator(msg);
			JadeGateway.execute(initiator, 10000);

			agentInfos = initiator.getAgentInfosResponse(codec,
					agentInfoOntology);

			JadeGateway.shutdown();

		} catch (CodecException | OntologyException e1) {
			throw new Exception("Failed to make request");
		} catch (ControllerException | InterruptedException e) {
			Exception x = new Exception("JadeGateway.execute() failed");
			x.setStackTrace(e.getStackTrace());
			throw x;
		}

		return agentInfos;
	}

}
