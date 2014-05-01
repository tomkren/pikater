package org.pikater.core.agents.system;


import java.util.Date;

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
import org.pikater.core.ontology.actions.BatchOntology;
import org.pikater.core.ontology.batch.Batch;
import org.pikater.core.ontology.batch.ExecuteBatch;
import org.pikater.core.ontology.batch.SaveBatch;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.messages.MessagesOntology;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
<<<<<<< HEAD
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.utilities.pikaterDatabase.Database;
=======
>>>>>>> FETCH_HEAD
=======
>>>>>>> FETCH_HEAD
>>>>>>> Stashed changes


public class Agent_InputTransformer extends PikaterAgent {
	
	private static final long serialVersionUID = 7226837600070711675L;


	@Override
	protected void setup() {

	  	System.out.println("Agent: " +getLocalName() + " starts.");

		initDefault();
		registerWithDF("InputTransformer");

		this.getContentManager().registerLanguage(getCodec());
		this.getContentManager().registerOntology(BatchOntology.getInstance());
		this.getContentManager().registerOntology(MessagesOntology.getInstance());

		RecieveExperiment recieveExp =
			new RecieveExperiment(this, getCodec(), this.emf);
		addBehaviour(recieveExp);

	}


	@Override
	protected String getAgentType(){
		return "InputTransformer";
	}

	public void sendBatchToSave(Batch batch) {

		SaveBatch saveBatch = new SaveBatch();
		saveBatch.setBatch(batch);
        
        //Ontology ontology = BatchOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(this.getAID());
		msg.addReceiver(new AID("dataManager", false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        msg.setLanguage(codec.getName());
        msg.setOntology(ontology.getName());
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(saveBatch);
		a.setActor(this.getAID());

		try {
			// Let JADE convert from Java objects to string
			this.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}

	}
}


class RecieveExperiment extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6561793512467823420L;

	private Agent_InputTransformer agent = null;
	private Codec codec = null;
	private EntityManagerFactory emf = null;
	
	public RecieveExperiment(Agent_InputTransformer agent, Codec codec, EntityManagerFactory emf) {
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
			
			if (!(act.getAction() instanceof ExecuteBatch)) {
				return;
			}
           
			System.out.println(agent.getName() + ": Agent recieved ComputingDescription from " + request.getSender().getName() );
			ExecuteBatch exeExperiment = (ExecuteBatch) act.getAction();
            ComputationDescription compDescription = exeExperiment.getDescription();

            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("OK");
            agent.send(reply);



            // send received ComputationDescription as Batch to DataManger to save to DB
            int klaraID = 6;

            Batch batch = new Batch();
            batch.setName("Klara's Batch");
            batch.setNote("Inputed by GuiKlara Agent");
            batch.setPriority(9);
            batch.setOwnerID(klaraID);
            batch.setDescription(compDescription);
            
            //this.agent.sendBatchToSave(batch);


            AID receiver = new AID("ComputationDescriptionParser", false);		

            Ontology ontology = BatchOntology.getInstance();
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(receiver);
            msg.setLanguage(codec.getName());
            msg.setOntology(ontology.getName());
            try {
    			agent.getContentManager().fillContent(msg, new Action(receiver, exeExperiment));
    			
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
	
	
}


