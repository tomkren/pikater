package org.pikater.core.agents.experiment;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.management.GetComputerInfo;

public abstract class Agent_AbstractExperiment extends PikaterAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7437896274669792147L;

	protected abstract AgentInfo getAgentInfo();
	
	protected void addAgentInfoBehaviour(AgentInfo agentInfo) {
		
		
		Ontology ontology = AgentInfoOntology.getInstance();
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.MatchOntology(ontology.getName()),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));


		addBehaviour(new AchieveREResponder(this, mt) {

			private static final long serialVersionUID = 7L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request)
					throws NotUnderstoodException, RefuseException {
				try {
					Action a = (Action) getContentManager()
							.extractContent(request);

					if (a.getAction() instanceof GetComputerInfo) {
					
						return respondToGetComputerInfo(request);
					}

				} catch (OntologyException e) {
					logError("Problem extracting content: " + e.getMessage(), e);
				} catch (CodecException e) {
					logError("Codec problem: " + e.getMessage(), e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logError("Failure responding to request: "
						+ request.getContent());
				return failure;
			}
		});

	}
	
	private ACLMessage respondToGetComputerInfo(ACLMessage request) {
		
		AgentInfo agentInfo = getAgentInfo();
		AID receiver = new AID(AgentNames.AGENTINFO_MANAGER, false);
		
		ACLMessage agentInfoMsg = request.createReply();
		
        try {
			getContentManager().fillContent(
					agentInfoMsg,
					new Action(receiver, agentInfo));
			
			log("Reply: OK");
			
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}
        
		return agentInfoMsg;
	}
	
}
