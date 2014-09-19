package org.pikater.core.agents.system.computation.planning;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
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

import java.util.Date;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.subtrees.systemLoad.GetSystemLoad;
import org.pikater.core.ontology.subtrees.systemLoad.SystemLoad;
import org.pikater.core.ontology.subtrees.task.KillTasks;

public class PlannerService
{
	/**
	 * Private constructor the implicit public one. 
	 */
	private PlannerService()
	{
	}
	
	public static SystemLoad getSystemLoad(PikaterAgent agent) {
		
		GetSystemLoad getSystemLoad = new GetSystemLoad();
        
        Ontology ontology = AgentManagementOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(agent.getAID());
		msg.addReceiver(new AID(CoreAgents.PLANNER.getName(), false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        msg.setLanguage(agent.getCodec().getName());
        msg.setOntology(ontology.getName());
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(getSystemLoad);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logSevere(ce.getMessage());
		} catch (OntologyException oe) {
			agent.logSevere(oe.getMessage());
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logSevere(e.getMessage());
		}
		
		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logException(e1.getMessage(), e1);
		} catch (CodecException e1) {
			agent.logException(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logException(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;
			return (SystemLoad) result.getValue();
		} else {
			agent.logSevere("No Result ontology");
		}
		
		return null;
	}
	
	public static void sendKillTasks(PikaterAgent agent, int batchID) {
		
		KillTasks killTasks = new KillTasks();
		killTasks.setBatchID(batchID);
        
        Ontology ontology = BatchOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(agent.getAID());
		msg.addReceiver(new AID(CoreAgents.PLANNER.getName(), false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        msg.setLanguage(agent.getCodec().getName());
        msg.setOntology(ontology.getName());
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(killTasks);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logSevere(ce.getMessage());
		} catch (OntologyException oe) {
			agent.logSevere(oe.getMessage());
		}

		try {
			FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logSevere(e.getMessage());
		}
	}
}