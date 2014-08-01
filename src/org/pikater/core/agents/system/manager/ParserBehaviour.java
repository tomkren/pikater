package org.pikater.core.agents.system.manager;

import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.Parser;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationGraph;
import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batch.ExecuteBatchDebug;
import org.pikater.core.ontology.subtrees.batch.NewBatch;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.shared.database.jpa.status.JPABatchStatus;

public class ParserBehaviour extends AchieveREResponder {
	
	private static final long serialVersionUID = 4754473043512463873L;
	
	private Agent_Manager agent;


    public ParserBehaviour(Agent_Manager agent_Manager) {
    	super(agent_Manager, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
    	
		this.agent = agent_Manager;

    }
    
    @Override
    protected ACLMessage handleRequest(final ACLMessage request) throws NotUnderstoodException, RefuseException {
   
    	Concept concept = null;
 
    	try {
            concept = ((Action)(agent.getContentManager().extractContent(request))).getAction();
        } catch (UngroundedException e) {
			agent.logError(e.getMessage(), e);
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}
           
    	
    	/*
    	 * ExecuteBatchDebug is deprecated
    	 */
    	if (concept instanceof ExecuteBatchDebug) {
    		
    		ExecuteBatchDebug executeExperiment =
    				(ExecuteBatchDebug) concept;
    		ComputationDescription comDescription =
					executeExperiment.getDescription();
    		int batchID = executeExperiment.getBatchID();
    		
    		return respondToNewBatch(comDescription, batchID, request);
        }
    	
    	/*
    	 * NewBatch inform
    	 */
    	if (concept instanceof NewBatch) {
    		
    		NewBatch newBatch = (NewBatch) concept;
    		
    		ManagerCommunicator communicator = new ManagerCommunicator();
    		Batch batch = communicator.loadBatch(agent, newBatch.getBatchId());
    		
    		ComputationDescription comDescription = batch.getDescription();
    		int batchID = batch.getId();
    		
    		return respondToNewBatch(comDescription, batchID, request);
    		
    	}
   
		ACLMessage failure = request.createReply();
		failure.setPerformative(ACLMessage.FAILURE);
		agent.logError("Failure responding to request: " + request.getContent());
		
		return failure;
    }
    
    private ACLMessage respondToNewBatch(ComputationDescription comDescription,
    		int batchID, ACLMessage request) {
    	
		Parser parser = new Parser(agent);
		parser.parseRoots(comDescription, batchID);
		
		ComputationGraph computationGraph = parser.getComputationGraph();
        ComputationCollectionItem item = new ComputationCollectionItem(computationGraph, request);
        agent.computationCollection.put(1, item);
        
        // change status to computing and log to database
        ManagerCommunicator communicator = new ManagerCommunicator();
        communicator.updateBatchStatus(agent, batchID, JPABatchStatus.COMPUTING.name());
        
		computationGraph.startBatchComputation();
		
		
    	ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        reply.setContent("OK");
        
        return reply;
    }
    
}