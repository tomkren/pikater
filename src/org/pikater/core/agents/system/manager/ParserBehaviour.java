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
import org.pikater.core.ontology.subtrees.batch.ExecuteBatch;
import org.pikater.core.ontology.subtrees.batch.NewBatch;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;

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
			agent.logError(e.getMessage());
			e.printStackTrace();
		} catch (CodecException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		}
           

    	ACLMessage reply = request.createReply();
    	
    	if (concept instanceof ExecuteBatch) {
    		
    		ExecuteBatch executeExperiment =
    				(ExecuteBatch) concept;
    		ComputationDescription comDescription =
					executeExperiment.getDescription();
    		
    		respondToNewBatch(comDescription, request);

            reply.setPerformative(ACLMessage.CONFIRM);
            reply.setContent("OK");
        }
    	
    	if (concept instanceof NewBatch) {
    		
    		NewBatch newBatch = (NewBatch) concept;
    		
    		ManagerCommunicator communicator = new ManagerCommunicator();
    		Batch batch = communicator.loadBatch(agent, newBatch.getBatchId());
    		ComputationDescription comDescription = batch.getDescription();
    		
    		respondToNewBatch(comDescription, request);
    		
            reply.setPerformative(ACLMessage.CONFIRM);
            reply.setContent("OK");
    	}
   
        return reply;

    }
    
    private void respondToNewBatch(ComputationDescription comDescription, ACLMessage request) {
    	
		Parser parser = new Parser(agent);
		parser.parseRoots(comDescription);
		
		ComputationGraph computationGraph = parser.getComputationGraph();
        ComputationCollectionItem item = new ComputationCollectionItem(computationGraph, request);
        agent.computationCollection.put(1,item);
        //TODOStepan: change status to computing and log to database
		computationGraph.startBatchComputation();
    }
    
}