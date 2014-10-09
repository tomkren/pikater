package org.pikater.core.agents.system;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.computation.graph.SearchComputationNode;
import org.pikater.core.agents.system.computation.graph.edges.SolutionEdge;
import org.pikater.core.agents.system.manager.ComputationCollectionItem;
import org.pikater.core.agents.system.manager.ParserBehaviour;
import org.pikater.core.ontology.*;
import org.pikater.core.ontology.subtrees.search.ExecuteParameters;
import org.pikater.core.ontology.subtrees.search.SearchSolution;

import java.util.*;


public class Agent_Manager extends PikaterAgent {

	private static final long serialVersionUID = -5140758757320827589L;

    protected Set<Subscription> subscriptions = new HashSet<Subscription>();
	protected HashMap<Integer, ComputationCollectionItem> computationCollection =
			new HashMap<Integer, ComputationCollectionItem>();
	
	public HashMap<String, ACLMessage> searchMessages =
			new HashMap<String, ACLMessage>();
	
	public ComputationCollectionItem getComputation(Integer id){
		return computationCollection.get(id);
	}
	
    public void addComputation(ComputationCollectionItem item) {
        computationCollection.put(item.getBatchID(),item);
    }

	/**
	 * Get ontologies which is using this agent
	 */
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AccountOntology.getInstance());
		ontologies.add(RecommendOntology.getInstance());
		ontologies.add(SearchOntology.getInstance());
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(FilenameTranslationOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		
		return ontologies;
	}
	
	/**
	 * Agent setup
	 */
	protected void setup() {

    	initDefault();

    	registerWithDF(CoreAgents.MANAGER.getName());
		doWait(30000);
		
		MessageTemplate subscriptionTemplate = 
				MessageTemplate.or(
						MessageTemplate.MatchPerformative(
								ACLMessage.SUBSCRIBE),
						MessageTemplate.MatchPerformative(
								ACLMessage.CANCEL));
		
		ParserBehaviour parserBehaviour = new ParserBehaviour(this);
		addBehaviour(parserBehaviour);
		
		SubscriptionResponder subscriptionResponder =
				new SubscriptionResponder(this,
						subscriptionTemplate,
						new ManagerSubscriptionManager());
		addBehaviour(subscriptionResponder);
		
		addBehaviour(new RequestServer(this));
		// TODO - prijimani zprav od Searche (pamatovat si id nodu), od Planera
		
	}
		
			
	public class ManagerSubscriptionManager implements SubscriptionManager {
		public boolean register(Subscription s) {
			subscriptions.add(s);
			return true;
		}

		public boolean deregister(Subscription s) {
			subscriptions.remove(s);
			return true;
		}
	}
	
	
	protected class RequestServer extends CyclicBehaviour {

		private static final long serialVersionUID = -6257623790759885083L;

		Ontology ontology = SearchOntology.getInstance();
		
		MessageTemplate getSchemaFromSearchTemplate =
				MessageTemplate.and(
				MessageTemplate.MatchProtocol(
						FIPANames.InteractionProtocol.FIPA_QUERY),
				MessageTemplate.and(
						MessageTemplate.MatchPerformative(
								ACLMessage.QUERY_REF),
				MessageTemplate.and(
						MessageTemplate.MatchLanguage(codec.getName()),
				MessageTemplate.MatchOntology(ontology.getName()))));

		
		public RequestServer(Agent agent) {			
			super(agent);
		}

		@Override 
		public void action() {
			ACLMessage query = receive(getSchemaFromSearchTemplate);
			
			if (query != null) {
				logInfo(": a query message received from " +
						query.getSender().getName());
				
				searchMessages.put(query.getConversationId(), query);				
				
				try {
					ContentElement content = getContentManager().extractContent(query);
					Concept action = ((Action) content).getAction();
					
					if (action instanceof ExecuteParameters) {
						// manager received new options from search to execute
						ExecuteParameters ep = (ExecuteParameters) (((Action) content).getAction());

						// => fill CA's queue
						String[] ids = query.getConversationId().split("_");
						int batchID = Integer.parseInt(ids[0]);
						int nodeId = Integer.parseInt(ids[1]);
						int computationId = Integer.parseInt(ids[2]);
											
						SearchComputationNode searchNode = 
								(SearchComputationNode) computationCollection.get(batchID)
									.getProblemGraph().getNode(nodeId); 


                        for (SearchSolution searchSolutionI : ep.getSolutions()) {
                            SolutionEdge se = new SolutionEdge();
                            se.setComputationID(computationId);
                            se.setOptions(searchSolutionI);
                            searchNode.addToOutputAndProcess(se, "searchedoptions");
                        }
				    } else {
						logSevere("unknown message received.");
					}
				} catch (UngroundedException e1) {
					logException(e1.getMessage(), e1);
				} catch (CodecException e1) {
					logException(e1.getMessage(), e1);
				} catch (OntologyException e1) {
					logException(e1.getMessage(), e1);
				}

			} else {
				block();
			}

			return;

		}
	}

	
	public void sendSubscription(ACLMessage result, ACLMessage originalMessage) {
        //TODO: get rid of this, probable te information will be taken form somewhere else
        if (true)
        {
            return;
        }
		// Prepare the subscription message to the request originator
		@SuppressWarnings("unused")
		ACLMessage msgOut = originalMessage.createReply();
		msgOut.setPerformative(result.getPerformative());
		
		// copy content of inform message to a subscription
		try {
            ContentElement content= getContentManager().extractContent(result);
            //TODO: bad ontology
			getContentManager().fillContent(msgOut, content );
		} catch (UngroundedException e) {
			logException(e.getMessage(), e);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		// go through every subscription
		java.util.Iterator<Subscription> it = subscriptions.iterator();
		while (it.hasNext()) {
			Subscription subscription = (Subscription) it.next();

			if (subscription.getMessage().getConversationId().equals(
					"subscription" + originalMessage.getConversationId())) {
				subscription.notify(msgOut);
			}
		}
		
	}
	
	public AID getAgentByType(String agentType) {
		return (AID)getAgentByType(agentType, 1).get(0);
	}

	
	public List<AID> getAgentByType(String agentType, int n) {
		
		List<AID> Agents = new ArrayList<AID>(); // List of AIDs
		
		// Make the list of agents of given type
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(agentType);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			logInfo("Found the following " + agentType + " agents:");
			
			for (int i = 0; i < result.length; ++i) {
				AID aid = result[i].getName();
				if (Agents.size() < n){
					Agents.add(aid);
				}
			}
			
			while (Agents.size() < n) {
				// create agent
				AID aid = createAgent(agentType);
				Agents.add(aid);
			}
		} catch (FIPAException fe) {
			logException(fe.getMessage(), fe);
			return new ArrayList<AID>();
		}
		
		return Agents;
		
	}
}
