package org.pikater.core.agents.system;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.computationDescriptionParser.Parser;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ProblemGraph;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.MessagesOntology;
import org.pikater.core.ontology.subtrees.batch.ExecuteBatch;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.experiment.Solve;
import org.pikater.core.ontology.subtrees.file.TranslateFilename;
import org.pikater.core.ontology.subtrees.oldPikaterMessages.Problem;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;


public class Agent_ComputationDescriptionParser extends PikaterAgent {
	
	private static final long serialVersionUID = 7116837600070411675L;
	
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(MessagesOntology.getInstance());
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(FilenameTranslationOntology.getInstance());
		
		return ontologies;
	}
	
	@Override
	protected void setup() {
		
		System.out.println("Agent: " +getLocalName() + " starts.");
	  	
		initDefault();
		registerWithDF(AgentNames.COMPUTATION_DESCRIPTION_PARSER);

		Ontology ontology = MessagesOntology.getInstance();

		ComputingManagerBehaviour compBehaviour =
				new ComputingManagerBehaviour(this, getCodec(), ontology);
        addBehaviour(compBehaviour);

	}
	
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
}




class ComputingManagerBehaviour extends AchieveREResponder {


	private static final long serialVersionUID = 4754473043512463873L;
	
	private Agent_ComputationDescriptionParser agent;
	private Codec codec = null;
	private Ontology ontology = null;

    public ComputingManagerBehaviour(
    		Agent_ComputationDescriptionParser agent, Codec codec, Ontology ontology) {
    	super(agent, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
    	
		this.agent = agent;
		this.codec = codec;
		this.ontology = ontology;
    }
    
    @Override
    protected ACLMessage handleRequest(final ACLMessage request) throws NotUnderstoodException, RefuseException {
   
    	Concept object = null;
 
    	try {
        	//Serializable object = request.getContentObject();
            object = ((Action)(agent.getContentManager().extractContent(request))).getAction();
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
            
  
        ACLMessage reply = request.createReply();
        
    	if (object instanceof ExecuteBatch) {
    		
    		ExecuteBatch executeExperiment =
    				(ExecuteBatch) object;
    		ComputationDescription comDescription =
					executeExperiment.getDescription();
    		
    		Parser parser = new Parser(this.agent);
    		parser.process(comDescription);
    		
    		// This will be graph - now only one problem
    		ProblemGraph dependencyGraph = parser.getProblemGraph();

    		// Problem parsed - reply OK
            reply.setPerformative(ACLMessage.INFORM);
            reply.setLanguage(codec.getName());
            reply.setOntology(ontology.getName());
            reply.setContent("OK");


            // TODO: Spusti pouze prvni problem
    		Problem problem = dependencyGraph.getProblems().get(0).getProblem();
    		//dependencyGraph.areAllProblemsFinished();
    		//dependencyGraph.getAllIndependetWaitingProblems();
    		
    		// Sending Problem
            sendingProblem(problem);
    		
        }
   
        return reply;

    }
    
    
    
    private void sendingProblem(Problem problem) {
    	
		System.out.println("Sending Solve ontology ID:" + problem.getId());

		Solve solve = new Solve();
		solve.setProblem(problem);
		
		// create a request message with SendProblem content
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(agent.getAID());
		msg.addReceiver(new AID("manager", false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		// We want to receive a reply in 30 secs
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));
		msg.setConversationId(problem.getGui_id() + agent.getLocalName());

		Action a = new Action();
		a.setAction(solve);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}

		agent.addBehaviour(new SendProblemToManager(agent, msg));
    }
}








class SendProblemToManager extends AchieveREInitiator {

	private static final long serialVersionUID = 8923548223375000884L;

	PikaterAgent agent;
	
	public SendProblemToManager(Agent agent, ACLMessage msg) {
		super(agent, msg);
		this.agent = (PikaterAgent) agent;
	}

	protected void handleAgree(ACLMessage agree) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ agree.getSender().getName() + " agreed.");
	}

	protected void handleInform(ACLMessage inform) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ inform.getSender().getName() + " replied.");
	}

	protected void handleRefuse(ACLMessage refuse) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ refuse.getSender().getName()
				+ " refused to perform the requested action");
	}

	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver
			// does not exist
			System.out.println(agent.getLocalName() + ": Responder does not exist");
		} else {
			System.out.println(agent.getLocalName() + ": Agent " + failure.getSender().getName()
					+ " failed to perform the requested action");
		}
	}

}

