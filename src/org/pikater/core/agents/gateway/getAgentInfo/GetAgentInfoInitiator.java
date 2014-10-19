package org.pikater.core.agents.gateway.getAgentInfo;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.lang.acl.ACLMessage;
import jade.content.onto.basic.Result;

import org.jfree.util.Log;
import org.pikater.core.agents.gateway.Initiator;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfos;

public class GetAgentInfoInitiator extends Initiator {
	
	private static final long serialVersionUID = -364766692797277164L;

	public GetAgentInfoInitiator(ACLMessage msg){
		super(msg);
	}

	public AgentInfos getAgentInfosResponse(Codec codec,
			Ontology agentInfoOntology) throws PikaterGatewayException {

		if (response == null) {
			throw new PikaterGatewayException("No response for Pikater agent action");
		}
		
		if (ACLMessage.FAILURE == response.getPerformative()
				|| ACLMessage.REFUSE == response.getPerformative()) {
			throw new PikaterGatewayException("Pikater agent action failed or refused: "
					+ response.getPerformative() + " " + response.getContent());
		}
		
		ContentManager contentManager = new ContentManager();
		contentManager.registerLanguage(codec);
		contentManager.registerOntology(agentInfoOntology);

		Result result = null;
		try {
			result = (Result) contentManager.extractContent(response);
		} catch (UngroundedException e) {
			Log.error(e.getMessage(), e);
		} catch (CodecException e) {
			Log.error(e.getMessage(), e);
		} catch (OntologyException e) {
			Log.error(e.getMessage(), e);
		}

		AgentInfos agentInfos = null;
		if (result.getValue() instanceof AgentInfos) {
			agentInfos = (AgentInfos) result.getValue();
		} else {
			throw new PikaterGatewayException("Pikater agent action failed or refused: "
					+ response.getPerformative() + " " + response.getContent());
		}

		return agentInfos;
	}
}
