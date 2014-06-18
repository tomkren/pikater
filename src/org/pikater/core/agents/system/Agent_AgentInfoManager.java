package org.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;
import org.pikater.shared.database.jpa.daos.DAOs;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class Agent_AgentInfoManager extends PikaterAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6530381228391705988L;

	final private List<AgentInfo> agentInfoList = new ArrayList<AgentInfo>();

	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentInfoOntology.getInstance());

		return ontologies;
	}

	@Override
	protected void setup() {
		initDefault();

		registerWithDF(AgentNames.AGENTINFO_MANAGER);

		for (Ontology ontologyI : getOntologies()) {
			getContentManager().registerOntology(ontologyI);
		}

		log("Agent " + getName() + " started");

		MessageTemplate newAgentInfoTemplate = MessageTemplate.or(
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));

		this.addBehaviour(new AchieveREResponder(this, newAgentInfoTemplate) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 324270572581200118L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {

				Action action = null;
				try {
					action = (Action) getContentManager().extractContent(
							request);
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (action.getAction() instanceof GetAgentInfos) {
					return respondToGetAgentInfos(request, action);
				}

				if (action.getAction() instanceof AgentInfo) {
					return respondToAgentInfo(request, action);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				log("Failure responding to request: " + request.getContent());
				return failure;
			}

		});
	}

	private ACLMessage respondToGetAgentInfos(ACLMessage request, Action action) {

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		AgentInfos agInfos = new AgentInfos();
		agInfos.setAgentInfos(agentInfoList);

		Result r = new Result(action, agInfos);
		try {
			getContentManager().fillContent(reply, r);
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reply;

	}

	protected ACLMessage respondToAgentInfo(ACLMessage request, Action action) {

		AgentInfo agentInfo = (AgentInfo) action.getAction();

		this.agentInfoList.add(agentInfo);
		log("Agent " + getName() + ": recieved AgentInfo from "
				+ agentInfo.getAgentClassName());

		DAOs.agentInfoDAO.storeAgentInfoOntology(agentInfo);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result r = new Result(action, "OK");
		try {
			getContentManager().fillContent(reply, r);
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reply;
	}

}
