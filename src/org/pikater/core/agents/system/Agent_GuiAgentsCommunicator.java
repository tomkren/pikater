package org.pikater.core.agents.system;


import java.util.ArrayList;
import java.util.List;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.manager.ManagerService;
import org.pikater.core.ontology.AccountOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batch.ExecuteBatch;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.shared.database.jpa.status.JPABatchStatus;

/**
 * 
 * Communicator for GUI-Agents
 *
 */
public class Agent_GuiAgentsCommunicator extends PikaterAgent {
	
	private static final long serialVersionUID = 7226837600070711675L;

	/**
	 * Get ontologies which is using this agent
	 */
	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AccountOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());		
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		return ontologies;
	}

	/**
	 * Agent setup
	 */
	@Override
	protected void setup() {

		initDefault();
		logInfo("is starting up...");
		registerWithDF(CoreAgents.GUI_AGENTS_COMMUNICATOR.getName());

		MessageTemplate template =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		
		addBehaviour(new AchieveREResponder(this, template) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request
					) throws NotUnderstoodException, RefuseException {
				
				try {
					Action action = (Action)
							getContentManager().extractContent(request);
					
					/*
					 * ExecuteBatch actions
					 */
					if (action.getAction() instanceof ExecuteBatch) {
						return respondToExecuteBatch(request, action);
					}
				
				} catch (OntologyException e) {
					logException("Problem extracting content: " +
						e.getMessage(), e);
				} catch (CodecException e) {
					logException("Codec problem: " + e.getMessage(), e);
				}
	
				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				
				logSevere("Failure responding to request: " +
					request.getContent());
				
				return failure;
			}
	
		});
	}
	
	/**
	 * Save a new Batch by using {@link Agent_DataManager} and send
	 * inform message to {@link Agent_Manager}
	 * 
	 * @param request
	 * @param action
	 * @return - OK message
	 */
	private ACLMessage respondToExecuteBatch(ACLMessage request,
			Action action) {
		
		ExecuteBatch exeBatch = (ExecuteBatch) action.getAction();
        ComputationDescription compDescription = exeBatch.getDescription();

        String batchName = null;
        String batchNote = null;
        int batchOwnerID = -1;
        int batchPriority = -1;

		AID klaraGUI = new AID(CoreAgents.GUI_KLARA_AGENT.getName(), false);
		String senderName = request.getSender().getLocalName() + "Agent";
		
			if (senderName.equals(klaraGUI.getLocalName()) ) {
				
				batchName = "Klara's Batch";
				batchNote = "Inputed by GuiKlara Agent";
				batchOwnerID = DataManagerService.getUserID(this, "klara");
				batchPriority = 9;
			} else {
				
				logSevere("Not permitted sender");
				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				return failure;
			}
			           
			logInfo("Agent recieved ComputingDescription from " +
					request.getSender().getName() );
			

            Batch batch = new Batch();
            batch.setName(batchName);
            batch.setNote(batchNote);
            batch.setStatus(JPABatchStatus.WAITING.name());
            batch.setPriority(batchPriority);
            batch.setOwnerID(batchOwnerID);
            batch.setDescription(compDescription);

            // send received ComputationDescription as Batch
            // to DataManger to save to DB
            int batchId =  DataManagerService.saveBatch(this, batch);
            logInfo("BatchId: " + batchId + " saved");

            
            // send only NewBatch
            ManagerService.sendNewBatchInfoToManager(this, batchOwnerID, batchId);
            		
            
            // send reply to User-GuiAgent
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("OK");
            return reply;
	}
	
}



