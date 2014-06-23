package org.pikater.core.agents.system;

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

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.SearchComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.edges.SolutionEdge;
import org.pikater.core.agents.system.manager.ComputationCollectionItem;
import org.pikater.core.agents.system.manager.ParserBehaviour;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.MessagesOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.search.ExecuteParameters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Agent_Manager extends PikaterAgent {

	private static final long serialVersionUID = -5140758757320827589L;
	
	private Ontology ontology = MessagesOntology.getInstance();
	
	private final String NO_XML_OUTPUT ="no_xml_output";
	private boolean no_xml_output = true;
	protected Set<Subscription> subscriptions = new HashSet<Subscription>();
	private int problem_i = 0;
	public HashMap<Integer, ComputationCollectionItem> computationCollection =
			new HashMap<Integer, ComputationCollectionItem>();
	
	
	public Ontology getOntology() {
		return ontology;
	}

	public void setOntology(Ontology ontology) {
		this.ontology = ontology;
	}

	public ComputationCollectionItem getComputation(Integer id){
		return computationCollection.get(id);
	}
	
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(MessagesOntology.getInstance());
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(FilenameTranslationOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		
		return ontologies;
	}
	
	
	protected void setup() {

    	initDefault();

    	registerWithDF(AgentNames.MANAGER);

		if (containsArgument(NO_XML_OUTPUT)) {
			if (isArgumentValueTrue(NO_XML_OUTPUT)){
				no_xml_output = true;
			}
			else{
				no_xml_output = false;
			}
		}

		doWait(3000);
				
		MessageTemplate subscriptionTemplate = 
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE),
								MessageTemplate.MatchPerformative(ACLMessage.CANCEL));
		
		addBehaviour(new ParserBehaviour(this));
				
		addBehaviour (new SubscriptionResponder(this, subscriptionTemplate, new subscriptionManager()));
		
		addBehaviour (new RequestServer(this)); // TODO - prijimani zprav od Searche (pamatovat si id nodu), od Planera
		
	} // end setup
		
			
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
	
	
	protected class RequestServer extends CyclicBehaviour {

		private static final long serialVersionUID = -6257623790759885083L;

		MessageTemplate getSchemaFromSearchTemplate = 
				MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY),
				MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF),
				MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
				MessageTemplate.MatchOntology(ontology.getName()))));

		
		public RequestServer(Agent agent) {			
			super(agent);
		}

		@Override 
		public void action() {
			ACLMessage query = receive(getSchemaFromSearchTemplate);
			
			if (query != null) {
				log(": a query message received from " + query.getSender().getName());

				try {
					ContentElement content = getContentManager().extractContent(query);
					if (((Action) content).getAction() instanceof ExecuteParameters) {
						// manager received new options from search to execute
						ExecuteParameters ep = (ExecuteParameters) content;

						// => fill CA's queue
						String[] ids = query.getConversationId().split("_");
						int graphId = Integer.parseInt(ids[0]);
						int nodeId = Integer.parseInt(ids[1]);
						SearchComputationNode searchNode = 
								(SearchComputationNode) computationCollection.get(graphId)
									.getProblemGraph().getNode(nodeId); 

						SolutionEdge se = new SolutionEdge();
						se.setOptions(ep.getSolutions());
				    	searchNode.addToOutputAndProcess(se, "searchedoptions");						
				    }
					else{
						logError("unknown message received.");
					}
				} catch (UngroundedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (CodecException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (OntologyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				block();
			}

			/*
			ACLMessage result_msg = request.createReply();
			result_msg.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			send(result_msg);
			*/
			return;

		}
	}

	
	public void sendSubscription(ACLMessage result, ACLMessage originalMessage) {			
		// Prepare the subscription message to the request originator
		ACLMessage msgOut = originalMessage.createReply();
		msgOut.setPerformative(result.getPerformative());
		
		// copy content of inform message to a subscription
		try {
			getContentManager().fillContent(msgOut, getContentManager().extractContent(result));
		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
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
		
	} // end sendSubscription
		

	public AID getAgentByType(String agentType) {
		return (AID)getAgentByType(agentType, 1).get(0);
	}

	
	public List getAgentByType(String agentType, int n) {
		// returns list of AIDs
		
		List Agents = new ArrayList(); // List of AIDs
		
		// Make the list of agents of given type
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(agentType);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			log("Found the following " + agentType + " agents:");
			
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
			fe.printStackTrace();
			return null;
		}
		
		return Agents;
		
	} // end getAgentByType
		
	
	private String getXMLValue(float value) {
		if (value < 0) {
			return "NA";
		}
		return Double.toString(value);
	}

	
    private String getDateTimeXML() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    

	protected String generateProblemID() {		
		return Integer.toString(problem_i++);
	}
	
}
