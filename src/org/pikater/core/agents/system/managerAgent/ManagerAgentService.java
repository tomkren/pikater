package org.pikater.core.agents.system.managerAgent;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.configuration.Arguments;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.subtrees.management.CreateAgent;
import org.pikater.core.ontology.subtrees.management.KillAgent;
import org.pikater.core.ontology.subtrees.management.LoadAgent;
import org.pikater.core.ontology.subtrees.model.Model;

/**
 * Created with IntelliJ IDEA. User: Kuba Date: 25.8.13 Time: 9:21 To change
 * this template use File | Settings | File Templates.
 */
public class ManagerAgentService {
	
	public static AID createAgent(PikaterAgent agent,
			String type, String name, Arguments options) {
		return ManagerAgentService.createAgent(
				agent, type, name, options, new AID(AgentNames.MANAGER_AGENT, false));
	}

	public static AID createAgent(PikaterAgent agent,
			String type, String name, Arguments options, AID agentManagerAID) {

		Ontology ontology = AgentManagementOntology.getInstance();

		ACLMessage msgCreateA = new ACLMessage(ACLMessage.REQUEST);
		msgCreateA.addReceiver(agentManagerAID);
		msgCreateA.setLanguage(agent.getCodec().getName());
		msgCreateA.setOntology(ontology.getName());

		CreateAgent ca = new CreateAgent();
		if (name != null) {
			ca.setName(name);
		}
		if (options != null) {
			ca.setArguments(options);
		}
		ca.setType(type);

		Action action = new Action(agent.getAID(), ca);

		AID aid = null;
		try {
			agent.getContentManager().fillContent(msgCreateA, action);
			ACLMessage msgRetursName = FIPAService
					.doFipaRequestClient(agent, msgCreateA);

			aid = new AID(msgRetursName.getContent(), AID.ISLOCALNAME);
		} catch (FIPAException e) {
			agent.logError(agent.getLocalName()
					+ ": Exception while adding agent " + type + ": "
					+ e.getMessage(), e);
		} catch (Codec.CodecException e) {
			agent.logError(agent.getLocalName() + ": " + e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(agent.getLocalName() + ": " + e.getMessage(), e);
		}

		return aid;
	}
	
	public boolean killAgent(PikaterAgent agentKiller, String agentNameToKill) {
		
		if (agentKiller == null) {
			throw new IllegalArgumentException(
					"Argument agentKiller can't be null");
		}
		if (agentNameToKill == null || agentNameToKill.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Argument agentNameToKill can't be null or empty");
		}


		AID agentManagerAID = new AID(AgentNames.MANAGER_AGENT, false);
		Ontology ontology = AgentManagementOntology.getInstance();

		ACLMessage msgKillA = new ACLMessage(ACLMessage.REQUEST);
		msgKillA.addReceiver(agentManagerAID);
		msgKillA.setSender(agentKiller.getAID());
		msgKillA.setLanguage(agentKiller.getCodec().getName());
		msgKillA.setOntology(ontology.getName());

		KillAgent ka = new KillAgent();
		ka.setName(agentNameToKill);
		
		Action action = new Action(agentKiller.getAID(), ka);
		
		try {
			agentKiller.getContentManager().fillContent(msgKillA, action);
			ACLMessage msgRetursName = FIPAService
					.doFipaRequestClient(agentKiller, msgKillA);

			if (msgRetursName.getPerformative() == ACLMessage.INFORM) {
				return true;
			}
			if (msgRetursName.getPerformative() == ACLMessage.FAILURE) {
				return false;
			}
			
		} catch (FIPAException e) {
			agentKiller.logError(agentKiller.getName(), e);
		} catch (Codec.CodecException e) {
			agentKiller.logError(agentKiller.getName(), e);
		} catch (OntologyException e) {
			agentKiller.logError(agentKiller.getName(), e);
		}
		
		return false;
	}
	
	public static AID loadAgent(PikaterAgent agent, int modelId) {
		return loadAgent(agent, modelId, new AID(AgentNames.MANAGER_AGENT, false));
	}
	
	public static AID loadAgent(PikaterAgent agent, int modelId, AID manager) {
		Model m = DataManagerService.getModel(agent, modelId);
		agent.log("got model, agent size "+m.getSerializedAgent().length+", result ID "+m.getResultID());

		LoadAgent act = new LoadAgent();
		act.setObject(m.getSerializedAgent());
		act.setFilename(m.getAgentClassName());

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(manager);
		request.setOntology(AgentManagementOntology.getInstance().getName());
		request.setLanguage(agent.getCodec().getName());
		try {
			agent.getContentManager().fillContent(request, new Action(agent.getAID(), act));
			ACLMessage response = FIPAService.doFipaRequestClient(agent, request, 10000);
			if (response == null) {
				agent.logError("did not receive LoadAgent response for model "+modelId+" in time");
			} else {
				agent.log("LoadAgent for model "+ modelId +" finished, AID="+response.getContent());
				return new AID(response.getContent(), AID.ISLOCALNAME);
			}
		} catch (CodecException e) {
			agent.logError("LoadAgent service failure", e);
		} catch (OntologyException e) {
			agent.logError("LoadAgent service failure", e);
		} catch (FIPAException e) {
			agent.logError("LoadAgent service failure", e);
		}
		throw new IllegalStateException("LoadAgent for model "+modelId+" failed");
	}
}
