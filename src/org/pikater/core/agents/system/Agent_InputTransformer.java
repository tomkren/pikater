package org.pikater.core.agents.system;


import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.DescriptionOntology;
import org.pikater.core.ontology.messages.Batch;
import org.pikater.core.ontology.messages.ExecuteExperiment;
import org.pikater.core.ontology.messages.MessagesOntology;
import org.pikater.core.ontology.messages.SaveBatch;
import org.pikater.shared.database.experiment.UniversalComputationDescription;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.utilities.pikaterDatabase.Database;


public class Agent_InputTransformer extends PikaterAgent {
	
	private static final long serialVersionUID = 7226837600070711675L;


	@Override
	protected void setup() {

	  	System.out.println("Agent: " +getLocalName() + " starts.");

		initDefault();
		registerWithDF("InputTransformer");

		this.getContentManager().registerLanguage(getCodec());
		this.getContentManager().registerOntology(DescriptionOntology.getInstance());
		this.getContentManager().registerOntology(MessagesOntology.getInstance());

		RecieveExperiment recieveExp =
			new RecieveExperiment(this, getCodec(), this.emf);
		addBehaviour(recieveExp);

	}


	@Override
	protected String getAgentType(){
		return "InputTransformer";
	}

}


class RecieveExperiment extends CyclicBehaviour {

	private Agent agent = null;
	private Codec codec = null;
	private EntityManagerFactory emf = null;
	
	public RecieveExperiment(Agent agent, Codec codec, EntityManagerFactory emf) {
		this.agent = agent;
		this.codec = codec;
		this.emf = emf;
	}

	public void action() {

		MessageTemplate reguestTemplate = 
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage request= this.agent.receive(reguestTemplate);
		
		if (request != null) {
			
			ContentElement ce = null;
			try {
				ce = agent.getContentManager().extractContent(request);
			} catch (CodecException | OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Action act = (Action)ce;
			
			if (!(act.getAction() instanceof ExecuteExperiment)) {
				return;
			}
           
			System.out.println(agent.getName() + ": Agent recieved ComputingDescription from " + request.getSender().getName() );
			ExecuteExperiment exeExperiment = (ExecuteExperiment) act.getAction();
            ComputationDescription compDescription = exeExperiment.getDescription();



            // send received ComputationDescription as Batch to DataManger to save to DB
            int klaraID = 6;

            Batch batch = new Batch();
            batch.setName("Klara's Batch");
            batch.setNote("Inputed by GuiKlara Agent");
            batch.setPriority(9);
            batch.setOwnerID(klaraID);
            batch.setDescription(compDescription);
            
            this.sendBatchToSave(batch);


            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("OK");
            agent.send(reply);


            AID receiver = new AID("ComputationDescriptionParser", false);		

            Ontology ontology = DescriptionOntology.getInstance();
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(receiver);
            msg.setLanguage(codec.getName());
            msg.setOntology(ontology.getName());
            try {
    			agent.getContentManager().fillContent(msg, new Action(receiver, exeExperiment));
    			agent.addBehaviour(new SendProblemToCompManager(agent, msg) );
    			
    			ACLMessage replyOK = FIPAService.doFipaRequestClient(agent, msg, 10000);
    			System.out.println("Reply: " + replyOK.getContent());
    			
    		} catch (CodecException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (OntologyException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	private void sendBatchToSave(Batch batch) {

		SaveBatch saveBatch = new SaveBatch();
		saveBatch.setBatch(batch);
        
        Ontology ontology = MessagesOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(this.agent.getAID());
		msg.addReceiver(new AID("dataManager", false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		
        msg.setLanguage(codec.getName());
        msg.setOntology(ontology.getName());
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(saveBatch);
		a.setActor(this.agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			this.agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}

	}
	
}



class SendProblemToCompManager extends AchieveREInitiator {

	private static final long serialVersionUID = 8923548223375000884L;

	String gui_id;
	PikaterAgent agent;
	
	public SendProblemToCompManager(Agent agent, ACLMessage msg) {
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

