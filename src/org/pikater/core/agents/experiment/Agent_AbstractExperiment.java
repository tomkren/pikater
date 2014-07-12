package org.pikater.core.agents.experiment;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.Agent_AgentInfoManager;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;

public abstract class Agent_AbstractExperiment extends PikaterAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7437896274669792147L;

	protected abstract AgentInfo getAgentInfo();
	
	protected void sendAgentInfo(AgentInfo agentInfo) {
		
		 DFAgentDescription template = new DFAgentDescription();
		 ServiceDescription sd = new ServiceDescription();
		 sd.setType(Agent_AgentInfoManager.class.getName());
		 template.addServices(sd);
		 
		 DFAgentDescription[] result = null;
		 try {
			result = DFService.search(this, template);
		} catch (FIPAException e1) {
			logError(e1.getMessage(), e1);
		}
		if (result.length == 0) {
			return;
		}
		 
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
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		} catch (FIPAException e) {
			logError(e.getMessage(), e);
		}

	}
}
