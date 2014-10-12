package org.pikater.core.agents.system.manager;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computation.graph.ModelComputationNode;
import org.pikater.core.agents.system.computation.graph.edges.DataSourceEdge;
import org.pikater.core.agents.system.computation.graph.edges.ErrorEdge;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;

/**
 * 
 * Behavior executing DM tasks - send them to planner and wait for results
 * 
 */
public class ExecuteTaskBehaviour extends AchieveREInitiator{

	private static final long serialVersionUID = -2044738642107219180L;

	private Agent_Manager myAgent;
    private final ModelComputationNode node;
    // original message sent by whoever wants to
    // compute the task (either search agent or
    // gui agent);
    // to be able to send a reply
    private ACLMessage msg;

    /**
     * Constructor
     * 
     * @param agent Owning manager
     * @param request Request to send
     * @param msg Original request message
     * @param node Computation node with the behavior
     */
	public ExecuteTaskBehaviour(Agent_Manager agent, ACLMessage request,
                                ACLMessage msg, ModelComputationNode node) {
		super(agent, request);
		myAgent = agent;
        this.msg = msg;
        this.node = node;
        node.increaseNumberOfOutstandingTask();
    }

	/**
	 * Handle refuse for Task sending
	 */
	protected void handleRefuse(ACLMessage refuse) {
		myAgent.logSevere("Agent "+refuse.getSender().getName()+" refused.");
	}
	
	/**
	 * Handle failure for Task sending
	 */
	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			myAgent.logSevere("Responder does not exist");
		}
		else {
			myAgent.logSevere("Agent "+failure.getSender().getName()+" failed.");	            
		}
	}
	
	/**
	 * Handle inform for Task sending
	 */
	protected void handleInform(ACLMessage inform) {
		
		myAgent.logInfo("Agent " + inform.getSender().getName() +
				" successfully performed the requested action.");
		
		ContentElement content;
		try {
			content = myAgent.getContentManager().extractContent(inform);
			
			if (content instanceof Result) {
				// get the original task from msg
				Result result = (Result) content;					
				Task task = (Task)result.getValue();
                
                DataSourceEdge labeledData = new DataSourceEdge();
                labeledData.setFile(false);

                if (node.containsOutput("file")) {
                    TaskOutput data= task.getOutputByType(Task.InOutType.DATA);
                    if (data == null) {
                        data = task.getOutput().get(0);
                    }
                    labeledData.setDataSourceId(data.getName());
                    node.addToOutputAndProcess(labeledData, "file");
                }

                CoreConstant.SlotContent testingData =
                		CoreConstant.SlotContent.TESTING_DATA;
                
                if (node.containsOutput(testingData.getSlotName())) {
                    TaskOutput test = task.getOutputByType(Task.InOutType.TEST);
                    if (test == null) {
                        test = task.getOutputByType(Task.InOutType.TRAIN);
                    }
                    labeledData.setDataSourceId(test.getName());
                    node.addToOutputAndProcess(labeledData, testingData.getSlotName());
                }

                CoreConstant.SlotContent trainingData =
                		CoreConstant.SlotContent.TRAINING_DATA;

                if (node.containsOutput(trainingData.getSlotName())) {
                    TaskOutput train = task.getOutputByType(Task.InOutType.TRAIN);

                    labeledData.setDataSourceId(train.getName());
                    node.addToOutputAndProcess(labeledData, trainingData.getSlotName());
                }

				// save results to the database										
				if (task.getSaveResults()) {
					DataManagerService.saveResult(myAgent, task, task.getExperimentID());
				}
                Task taskResult = (Task)result.getValue();
                int taskResultID = taskResult.getComputationID();
                
                ErrorEdge errorEdge = new ErrorEdge(taskResult.getResult(), taskResultID);
                node.addToOutputAndProcess(errorEdge, "error");
                node.decreaseNumberOfOutstandingTask();
			}
		} catch (CodecException | OntologyException e) {
			myAgent.logException(e.getMessage(), e);
		}

        // send subscription to the original agent after each received task
		myAgent.sendSubscription(inform, msg);
	}
}


