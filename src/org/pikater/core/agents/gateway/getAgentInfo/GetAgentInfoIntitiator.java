package org.pikater.core.agents.gateway.getAgentInfo;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.content.onto.basic.Result;

import org.jfree.util.Log;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;

public class GetAgentInfoIntitiator extends AchieveREInitiator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -364766692797277164L;

	private ACLMessage response;

	public GetAgentInfoIntitiator(ACLMessage msg) {
		super(null, msg);
	}

	public ACLMessage getResponse() {
		return response;
	}

	public AgentInfos getAgentInfosResponse(Codec codec,
			Ontology agentInfoOntology) throws Exception {

		if (response == null)
			throw new Exception("No response for Pikater agent action");

		if (ACLMessage.FAILURE == response.getPerformative()
				|| ACLMessage.REFUSE == response.getPerformative())
			throw new Exception("Pikater agent action failed or refused: "
					+ response.getPerformative() + " " + response.getContent());

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
			throw new Exception("Pikater agent action failed or refused: "
					+ response.getPerformative() + " " + response.getContent());
		}

		return agentInfos;
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		response = inform;
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		response = refuse;
	}

	@Override
	protected void handleFailure(ACLMessage failure) {
		response = failure;
	}
}
