package org.pikater.core.agents.system.guiAgentsCommunicator;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AccountOntology;
import org.pikater.core.ontology.subtrees.account.GetUserID;

public class GuiCommunicator {

	/*
	 * Sends the request to userID to the Agent_DataManger
	 * Returns User-ID
	 */
	public int getUserID(PikaterAgent agent, String login) {
		
		GetUserID getUserID = new GetUserID();
		getUserID.setLogin(login);
        
        Ontology ontology = AccountOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        msg.setLanguage(new SLCodec().getName());
        msg.setOntology(ontology.getName());

		Action a = new Action();
		a.setAction(getUserID);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logError(ce.getMessage());
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage());
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logError(e.getMessage());
		}
		
		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logError(e1.getMessage());
		} catch (CodecException e1) {
			agent.logError(e1.getMessage());
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage());
		}

		if (content instanceof Result) {
			Result result = (Result) content;
			
			int userID = Integer.parseInt((String)result.getValue());
			agent.log("Recieved ID " + userID);
			
			return userID;
		} else {
			agent.logError("No Result ontology");
		}
		
		return -1;
	}
}
