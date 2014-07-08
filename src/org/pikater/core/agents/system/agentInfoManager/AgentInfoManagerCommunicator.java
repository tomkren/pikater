package org.pikater.core.agents.system.agentInfoManager;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.system.Agent_AgentInfoManager;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;
import org.pikater.core.ontology.subtrees.model.GetModels;
import org.pikater.core.ontology.subtrees.model.Models;

public class AgentInfoManagerCommunicator {

	private Agent_AgentInfoManager agent;
	
	public AgentInfoManagerCommunicator(Agent_AgentInfoManager agent) {
		this.agent = agent;
	}

	public AgentInfos getAgentInfos() {
		
		AID receiver = new AID(AgentNames.DATA_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(receiver);
		request.setLanguage(agent.getCodec().getName());
		request.setOntology(ontology.getName());
	        
		try {
			agent.getContentManager().fillContent(request,
					new Action(receiver, new GetAgentInfos()));
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}
		
		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, request, 10000);
			if (reply == null) {
				return null;
			}
			
		} catch (FIPAException e) {
			agent.logError(e.getMessage());
		}

		AgentInfos agentInfos = null;
		try {
			Result r = (Result) agent.getContentManager().extractContent(reply);
	
			agentInfos = (AgentInfos) r.getValue();
	
		} catch (UngroundedException e) {
			agent.logError(e.getMessage(), e);
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		return agentInfos;
	}

	public Models getAllModels() {
		
		AID receiver = new AID(AgentNames.DATA_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(receiver);
		request.setLanguage(agent.getCodec().getName());
		request.setOntology(ontology.getName());
	        
		try {
			agent.getContentManager().fillContent(request,
					new Action(receiver, new GetModels()));
		} catch (CodecException e) {
			agent.logError(e.getMessage());
		} catch (OntologyException e) {
			agent.logError(e.getMessage());
		}
		
		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, request, 10000);
			if (reply == null) {
				return null;
			}
			
		} catch (FIPAException e) {
			agent.logError(e.getMessage());
		}

		Models models = null;
		try {
			Result r = (Result) agent.getContentManager().extractContent(reply);
	
			models = (Models) r.getValue();
	
		} catch (UngroundedException e) {
			agent.logError(e.getMessage());
		} catch (CodecException e) {
			agent.logError(e.getMessage());
		} catch (OntologyException e) {
			agent.logError(e.getMessage());
		}

		return models;
	}

}
