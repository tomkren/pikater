package org.pikater.core.agents.system;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;
import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.management.ManagerAgentCommunicator;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.MessagesOntology;
import org.pikater.core.ontology.subtrees.option.Option;

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
	protected HashMap<Integer, ComputationCollectionItem> computationCollection = 
			new HashMap<Integer, ComputationCollectionItem>();
	
	
	public ComputationCollectionItem getComputation(Integer id){
		return computationCollection.get(id);
	}
	
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(MessagesOntology.getInstance());
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(FilenameTranslationOntology.getInstance());
		
		return ontologies;
	}
	
	
	protected void setup() {

    	initDefault();
    
    	registerWithDF("manager");
    			
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
		
        addBehaviour(new ParserBehaviour(this, getCodec(), ontology));
				
		addBehaviour (new SubscriptionResponder(this, subscriptionTemplate, new subscriptionManager()));
		
		// addBehaviour (new RequestServer(this));
		
	} // end setup
		
	
	public String getHashOfFile(String nameOfFile) {
		
		TranslateFilename translate = new TranslateFilename();
		translate.setExternalFilename(nameOfFile);
		translate.setUserID(1);

		// create a request message with SendProblem content
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(this.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		msg.setLanguage(codec.getName());
		msg.setOntology(FilenameTranslationOntology.getInstance().getName());
		// We want to receive a reply in 30 secs
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));
		//msg.setConversationId(problem.getGui_id() + agent.getLocalName());

		Action a = new Action();
		a.setAction(translate);
		a.setActor(this.getAID());

		try {
			// Let JADE convert from Java objects to string
			this.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}


		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(this, msg);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ContentElement content = null;
		String fileHash = null;

		try {
			content = getContentManager().extractContent(reply);
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

		if (content instanceof Result) {
			Result result = (Result) content;
			
			fileHash = (String) result.getValue();
		}
		
		return fileHash;
	}
			
		
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

	
	
	
	protected void sendSubscription(ACLMessage result, ACLMessage originalMessage) {			
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
	
	
	public ACLMessage execute2Message(Execute execute) {		
		// create ACLMessage from Execute ontology action
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setLanguage(codec.getName());
		request.setOntology(ontology.getName());
		request.addReceiver(getAgentByType("Planner"));
		
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	
		Action a = new Action();
		a.setAction(execute);
		a.setActor(this.getAID());

		try {
			getContentManager().fillContent(request, a);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
		return request;
	}
	

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
				AID aid = createAgent(agentType, null, null);
				Agents.add(aid);
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		}
		
		return Agents;
		
	} // end getAgentByType
	
	
	public AID createAgent(String type, String name, List<Option> options) {
        ManagerAgentCommunicator communicator=new ManagerAgentCommunicator("agentManager");
        AID aid=communicator.createAgent((PikaterAgent)this,type,name,options);
		return aid;		
	}
	
	
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
