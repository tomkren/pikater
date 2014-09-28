package org.pikater.core.agents.system.manager;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computation.graph.ComputationNode;
import org.pikater.core.agents.system.computation.graph.edges.DataSourceEdge;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;

import java.util.ArrayList;

/**
 * Behavior for data processing - pre and postprocessing
 */
public class ExecuteDataProcessingBehaviour extends AchieveREInitiator{

	private static final long serialVersionUID = -2044738642107219180L;

	private Agent_Manager myAgent;
	// private DataProcessingStrategy strategy; // unused
    private final ComputationNode node;
    private ACLMessage msg; // original message sent by whoever wants to
	 						// compute the task (either search agent or
							// gui agent);
							// to be able to send a reply

    /**
     *  @param a Agent manager that will receive this behavior
     * @param req Request to send
     * @param msg Original request
     * @param node Node that created the behavior
     */
	public ExecuteDataProcessingBehaviour(Agent_Manager a, ACLMessage req,
                                          ACLMessage msg, ComputationNode node) {
		super(a, req);
		
		this.myAgent = a;
        this.msg = msg;
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
				Task t = (Task) result.getValue();
                ArrayList<TaskOutput> outputs = t.getOutput();
                for (TaskOutput output : outputs) {
                    DataSourceEdge edge = new DataSourceEdge();
                    edge.setDataSourceId(output.getName());
                    edge.setFile(false);

                    for (String in : node.findOutput(output.getDataType())) {
                        node.addToOutputAndProcess(edge, in, false, true);
                    }
                }
                node.decreaseNumberOfOutstandingTask();
            }

		} catch (CodecException | OntologyException e) {
			myAgent.logException(e.getMessage(), e);
		}

        // send subscription to the original agent after each received task
		myAgent.sendSubscription(inform, msg);
	}
} // end of ExecuteTask ("send request to planner agent") bahavior


