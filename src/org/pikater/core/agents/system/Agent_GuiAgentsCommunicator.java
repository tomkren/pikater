package org.pikater.core.agents.system;


import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.manager.ManagerCommunicator;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batch.ExecuteBatch;
import org.pikater.core.ontology.subtrees.batch.batchStatuses.BatchStatuses;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;

public class Agent_GuiAgentsCommunicator extends PikaterAgent {
	
	private static final long serialVersionUID = 7226837600070711675L;

	@Override
	public java.util.List<Ontology> getOntologies() {
		java.util.List<Ontology> ontologies = new java.util.ArrayList<Ontology>();
		ontologies.add(MetadataOntology.getInstance());		
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		return ontologies;
	}

	@Override
	protected void setup() {

	  	System.out.println("Agent: " +getLocalName() + " starts.");

		initDefault();
		registerWithDF(AgentNames.GUI_AGENTS_COMMUNICATOR);

		RecieveBatch recieveExp =
			new RecieveBatch(this, getCodec());
		addBehaviour(recieveExp);
	}
	
	
	public int sendBatchToSave(Batch batch) {

		ManagerCommunicator comunicator =
				new ManagerCommunicator();
		return comunicator.saveBatch(this, batch);
	}
}


class RecieveBatch extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6561793512467823420L;

	private Agent_GuiAgentsCommunicator agent = null;
	private Codec codec = null;
	
	public RecieveBatch(Agent_GuiAgentsCommunicator agent, Codec codec) {
		this.agent = agent;
		this.codec = codec;
	}

	public void action() {

		MessageTemplate reguestTemplate = 
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage request= this.agent.receive(reguestTemplate);
		
		if (request != null && agent.getContentManager() != null) {
			
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

            AID receiver = new AID(AgentNames.MANAGER, false);		

            Ontology ontology = BatchOntology.getInstance();
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(receiver);
            msg.setLanguage(codec.getName());
            msg.setOntology(ontology.getName());
            try {
    			agent.getContentManager().fillContent(msg, new Action(receiver, exeExperiment));
    			agent.send(msg);
    			//ACLMessage replyOK = FIPAService.doFipaRequestClient(agent, msg); //, 1000);
    		    //System.out.println("Reply: " + replyOK.getContent());
    			
    		} catch (CodecException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (OntologyException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
			}

		}
	}
	
	
}


