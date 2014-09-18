package org.pikater.core.agents.system.manager;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Result;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.ArrayList;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.DataProcessingStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;

public class ExecuteDataProcessingBehaviour extends AchieveREInitiator{

	private static final long serialVersionUID = -2044738642107219180L;

	private Agent_Manager myAgent;
	// private DataProcessingStrategy strategy; // unused
    private final ComputationNode node;
    private ACLMessage msg; // original message sent by whoever wants to
	 						// compute the task (either search agent or
							// gui agent);
							// to be able to send a reply

	public ExecuteDataProcessingBehaviour(Agent_Manager a, ACLMessage req,
                                          ACLMessage msg, DataProcessingStrategy cs, ComputationNode node) {
		super(a, req);
		
		this.myAgent = a;
        this.msg = msg;
		// this.strategy = cs;
        this.node = node;
        
        node.increaseNumberOfOutstandingTask();
    }

	protected void handleRefuse(ACLMessage refuse) {
		myAgent.logSevere("Agent "+refuse.getSender().getName()+" refused.");
	}
	
	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			myAgent. logSevere("Responder does not exist");
		}
		else {
			myAgent.logSevere("Agent "+failure.getSender().getName()+" failed.");	            
		}
	}
	
	protected void handleInform(ACLMessage inform) {
		myAgent.logInfo("Agent "+inform.getSender().getName()+" successfully performed the requested action.");

		
		ContentElement content;
		try {
			content = myAgent.getContentManager().extractContent(inform);
			if (content instanceof Result) {
				// get the original task from msg
				Result result = (Result) content;					
				Task t = (Task)result.getValue();
                // ComputationCollectionItem computation = myAgent.getComputation(t.getBatchID()); // unused
                ArrayList<TaskOutput> outputs = t.getOutput();
                for (int i=0;i<outputs.size();i++)
                {
                    TaskOutput output=outputs.get(i);
                    DataSourceEdge edge=new DataSourceEdge();
                    edge.setDataSourceId(output.getName());
                    edge.setFile(false);
                    
                    for (String in : node.findOutput(output.getDataType())){                    	
                    	node.addToOutputAndProcess(edge, in, false, true);
                    } 
                }
                node.decreaseNumberOfOutstandingTask();
            }

		} catch (UngroundedException e) {
			myAgent.logException(e.getMessage(), e);
		} catch (CodecException e) {
			myAgent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			myAgent.logException(e.getMessage(), e);
		}
		
		// send subscription to the original agent after each received task
		myAgent.sendSubscription(inform, msg);
	}
} // end of ExecuteTask ("send request to planner agent") bahavior


