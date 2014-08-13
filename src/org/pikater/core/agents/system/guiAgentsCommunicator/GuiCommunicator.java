package org.pikater.core.agents.system.guiAgentsCommunicator;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.subtrees.batch.NewBatch;

public class GuiCommunicator {
	
	/*
	 * Sends ID of new batch as inform to agent Manager
	 */
	public void sendNewBatchInfoToManager(PikaterAgent agent, int userID, int newBatchID) {
		
		NewBatch newBatch = new NewBatch();
		newBatch.setUserId(userID);
		newBatch.setBatchId(newBatchID);
        
        Ontology ontology = BatchOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        msg.setLanguage(new SLCodec().getName());
        msg.setOntology(ontology.getName());

		Action a = new Action();
		a.setAction(newBatch);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logError(ce.getMessage(), ce);
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage(), oe);
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
			agent.log("Reply to NewBatch: " + reply.getContent());
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
		
	}
	
}
