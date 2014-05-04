package org.pikater.core.agents.system;


import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Date;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.actions.BatchOntology;
import org.pikater.core.ontology.actions.MessagesOntology;
import org.pikater.core.ontology.batch.Batch;
import org.pikater.core.ontology.batch.ExecuteBatch;
import org.pikater.core.ontology.batch.SaveBatch;
import org.pikater.core.ontology.batch.SavedBatch;
import org.pikater.core.ontology.batch.batchStatuses.BatchStatuses;
import org.pikater.core.ontology.description.ComputationDescription;

public class Agent_InsertedBatchesLog extends PikaterAgent {
	
	private static final long serialVersionUID = 7226837600070711675L;


	@Override
	protected void setup() {

	  	System.out.println("Agent: " +getLocalName() + " starts.");

		initDefault();
		registerWithDF(AgentNames.INSERTED_BATCHES_LOG);

		this.getContentManager().registerLanguage(getCodec());
		this.getContentManager().registerOntology(BatchOntology.getInstance());
		this.getContentManager().registerOntology(MessagesOntology.getInstance());

		RecieveExperiment recieveExp =
			new RecieveExperiment(this, getCodec());
		addBehaviour(recieveExp);

	}

	public int sendBatchToSave(Batch batch) {

		SaveBatch saveBatch = new SaveBatch();
		saveBatch.setBatch(batch);
        
        Ontology ontology = BatchOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(this.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
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

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(this, msg);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ContentElement content = null;
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
			
			SavedBatch savedBatch = (SavedBatch) result.getValue();
			this.log(savedBatch.getMessage());
			
			return savedBatch.getSavedBatchId();
		} else {
			this.logError("Error - No Result ontology");
		}
		
		return -1;
	}
}


class RecieveExperiment extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6561793512467823420L;

	private Agent_InsertedBatchesLog agent = null;
	private Codec codec = null;
	
	public RecieveExperiment(Agent_InsertedBatchesLog agent, Codec codec) {
		this.agent = agent;
		this.codec = codec;
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
            batch.setStatus(BatchStatuses.WAITING);
            batch.setPriority(9);
            batch.setOwnerID(klaraID);
            batch.setDescription(compDescription);

            int batchId = agent.sendBatchToSave(batch);
            agent.log("BatchId: " + batchId);

            AID receiver = new AID(AgentNames.COMPUTATION_DESCRIPTION_PARSER, false);		

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


