package org.pikater.core.agents.experiment;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;

public abstract class Agent_AbstractExperiment extends PikaterAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7437896274669792147L;

	protected abstract AgentInfo getAgentInfo();
	
	protected void sendAgentInfo(AgentInfo agentInfo) {
		
		AID receiver = new AID(AgentNames.AGENTINFO_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();
		
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(receiver);
        msg.setLanguage(codec.getName());
        msg.setOntology(ontology.getName());
        try {
			getContentManager().fillContent(msg, new Action(receiver, agentInfo));
			
			ACLMessage replyOK = FIPAService.doFipaRequestClient(this, msg, 10000);
			log("Reply: OK");// + replyOK.getContent());
			
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
