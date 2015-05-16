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
import org.pikater.core.agents.system.manager.ComputationCollectionItem;
import org.pikater.core.agents.system.manager.ParserBehaviour;
import org.pikater.core.agents.system.manager.graph.SearchComputationNode;
import org.pikater.core.agents.system.manager.graph.edges.SolutionEdge;
import org.pikater.core.ontology.*;
import org.pikater.core.ontology.subtrees.search.ExecuteParameters;
import org.pikater.core.ontology.subtrees.search.SearchSolution;

import java.util.*;


/**
 * Agent Manager is a central control unit of the system.
 * It handles requests to compute batches from GUI agents (ParserBehaviour),
 * recommender agents (ParserBehaviour), queries from search agents 
 * (receiveQuery behaviour). It also receives the computations' results 
 * from Planner agent (ParserBehaviour).
 * 
 * @author Klara
 *
 */
public class Agent_Manager extends PikaterAgent {

	private static final long serialVersionUID = -5140758757320827589L;

    protected Set<Subscription> subscriptions = new HashSet<Subscription>();
	protected Map<Integer, ComputationCollectionItem> computationCollection =
			new HashMap<Integer, ComputationCollectionItem>();
	
	public Map<String, ACLMessage> searchMessages =
			new HashMap<String, ACLMessage>();
	
	/**
	 * Returns the specific {@link ComputationCollectionItem} given id.
	*/
	public ComputationCollectionItem getComputation(Integer id){
		return computationCollection.get(id);
	}
	
	
	/**
	 * Adds a {@link ComputationCollectionItem} to computationCollection.
	*/	
    public void addComputation(ComputationCollectionItem item) {
        computationCollection.put(item.getBatchID(),item);
    }

    /**
     * Returns list of all ontologies that are used by manager agent.
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
		ontologies.add(OpenMLOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		
		return ontologies;
	}
	
	
	/**
	 * Initializes Agent_Manager agent.
	 * <p>
	 * Registers manager with yellow pages and starts its three behaviours:
	 *   <ul>
	 *     <li>{@link ParserBehaviour} that deals with requests from GUI agents
	 *     <li>{@link SubscriptionResponder} behaviour (for sending subscriptions messages about
	 *       the results to registered subscribers)
	 *     <li>{@link ReceiveQuery} behaviour that receives queries from search agents
	 *   </ul>    
	 */
	protected void setup() {

    	initDefault();

    	registerWithDF(CoreAgents.MANAGER.getName());
		doWait(30000);
		
		MessageTemplate subscriptionTemplate = 
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE),
								MessageTemplate.MatchPerformative(ACLMessage.CANCEL));
		
		addBehaviour(new ParserBehaviour(this));
				
		addBehaviour(new SubscriptionResponder(this, subscriptionTemplate, new subscriptionManager()));
		
		addBehaviour(new ReceiveQuery(this));
		
	} // end setup
					
	
	/**
	 * A JADE behaviour that handles a requests (query messages) made by
	 * search agents. 
	 * <p>
	 * A FIPA_QUERY protocol is used, a QUERY_REF message with 
	 * ExecuteParameters ontology is expected.
	 * IDs used in the graph held inside the Manager are coded 
	 * into the conversationId in the following order and are split by "_":
	 * <ol>
	 * <li>batchID
	 * <li>nodeId
	 * <li>computationId
	 * </ol>	
	 * 
	 * @author Klara
	 *
	 */
	protected class ReceiveQuery extends CyclicBehaviour {

		private static final long serialVersionUID = -6257623790759885083L;

		Ontology ontology = SearchOntology.getInstance();
		
		MessageTemplate getSchemaFromSearchTemplate =
				MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY),
				MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF),
				MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
				MessageTemplate.MatchOntology(ontology.getName()))));

		
		public ReceiveQuery(Agent agent) {
			super(agent);
		}

		/**
		 * The core method of the ReceiveQuery behaviour. It receives a query
		 * from a search agent and processes it, i.e. assigns the requested 
		 * task to the right computation node (the node is identified by the 
		 * conversation ID of the message received) and fills the queues. 
		 */
		@Override 
		public void action() {
			ACLMessage query = receive(getSchemaFromSearchTemplate);
			
			if (query != null) {
				logInfo(": a query message received from " + query.getSender().getName());
				
				searchMessages.put(query.getConversationId(), query);				
				
				try {
					
					ContentElement content =
							getContentManager().extractContent(query);
					Concept action = ((Action) content).getAction();
					
					if (action instanceof ExecuteParameters) {

						// manager received new options from search to execute
						ExecuteParameters ep = (ExecuteParameters) action;

						// => fill CA's queue
						String[] ids = query.getConversationId().split("_");
						int batchID = Integer.parseInt(ids[0]);
						int nodeId = Integer.parseInt(ids[1]);
						int computationId = Integer.parseInt(ids[2]);
											
						SearchComputationNode searchNode = 
								(SearchComputationNode) computationCollection
								.get(batchID).getProblemGraph()
								.getNode(nodeId); 

                        for (SearchSolution ssI : ep.getSolutions()) {
                            SolutionEdge se = new SolutionEdge();
                            se.setComputationID(computationId);
                            se.setOptions(ssI);
                            searchNode.addToOutputAndProcess(se, "searchedoptions");
                        }
				    } else{
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
		}
	}

	
	/**
	 * A JADE class for registering to receiving "subscription" messages 
	 * about the (partial) computation results. 
	 * A typical subscriber is a GUI agent.
	 * 
	 */
	public class subscriptionManager implements SubscriptionManager {
		public boolean register(Subscription s) {
			subscriptions.add(s);
			return true;
		}

		public boolean deregister(Subscription s) {
			subscriptions.remove(s);
			return true;
		}
	}

	
	/**
	 * A method for sending "subscription" messages about the (partial) 
	 * computation results to whoever is subscribed to it. 
	 * Typically the subscriber is a GUI agent. 
	 * <p>
	 * Note that it is also possible for an agent to get the results directly
	 * from the database, where they are being stored as soon as the single 
	 * computations are finished. 
	 * In this case a subscription can be used as a cue that there have been 
	 * a progress in a processing of a batch.
	 *  
	 * @param result			the message containing a result of 
	 * 							a computation
	 * @param originalMessage	the message that the subscriber originally 
	 * 							sent to the Manager, used to create 
	 * 							a subscription message
	 */
	public void sendSubscription(ACLMessage result, ACLMessage originalMessage) {
		// Prepare the subscription message to the request originator
		ACLMessage msgOut = originalMessage.createReply();
		msgOut.setPerformative(result.getPerformative());
		
		/*
		// copy content of inform message to a subscription
		try {
			@SuppressWarnings("unused")
            ContentElement content = getContentManager().extractContent(result);

			//getContentManager().fillContent(msgOut, content );

		} catch (UngroundedException e) {
			logException(e.getMessage(), e);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}
		*/

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
	// end sendSubscription
	
	
	/**
	 * Returns an agent of the required type.
	 * If the agent doesn't exist, it is created.
 
	 * @param agentType		agent type to be created
	 * @return				AID of an agent of the required type 
	 */
	public AID getAgentByType(String agentType) {
		return (AID)getAgentByType(agentType, 1).get(0);
	}

	
	/**
	 * Returns a list of agents of the required type.
	 * If the agents don't exist, they are created.
	 * 
	 * @param agentType		agent type to be created
	 * @param n				the number of agents that are required
	 * @return 				list of AIDs of agents
	 */
	public List<AID> getAgentByType(String agentType, int n) {
		
		// List of AIDs
		List<AID> agents = new ArrayList<AID>();
		
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
				if (agents.size() < n){
					agents.add(aid);
				}
			}
			
			while (agents.size() < n) {
				// create agent
				AID aid = createAgent(agentType);
				agents.add(aid);
			}
		} catch (FIPAException fe) {
			logException(fe.getMessage(), fe);
			return new ArrayList<AID>();
		}
		
		return agents;
		
	}
	// end getAgentByType
}
