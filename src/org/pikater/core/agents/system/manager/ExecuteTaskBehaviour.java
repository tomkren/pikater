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
 * Behavior executing DM tasks - send them to planner and wait for results
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
     *
     * @param a Owning manager
     * @param req Request to send
     * @param msg Original request message
     * @param node Computation node with the behavior
     */
	public ExecuteTaskBehaviour(Agent_Manager a, ACLMessage req,
                                ACLMessage msg, ModelComputationNode node) {
		super(a, req);
		myAgent = a;
        this.msg = msg;
        this.node = node;
        node.increaseNumberOfOutstandingTask();
    }

	protected void handleRefuse(ACLMessage refuse) {
		myAgent.logSevere("Agent "+refuse.getSender().getName()+" refused.");
	}
	
	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			myAgent.logSevere("Responder does not exist");
		}
		else {
			myAgent.logSevere("Agent "+failure.getSender().getName()+" failed.");	            
		}
	}
	
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
                    node.addToOutputAndProcess(labeledData,"file");
                }
                if (node.containsOutput(CoreConstant.SlotContent.TESTING_DATA.getSlotName())) {
                    TaskOutput test = task.getOutputByType(Task.InOutType.TEST);
                    if (test == null) {
                        test = task.getOutputByType(Task.InOutType.TRAIN);
                    }
                    labeledData.setDataSourceId(test.getName());
                    node.addToOutputAndProcess(labeledData, CoreConstant.SlotContent.TESTING_DATA.getSlotName());
                }
                if (node.containsOutput(CoreConstant.SlotContent.TRAINING_DATA.getSlotName())) {
                    TaskOutput train = task.getOutputByType(Task.InOutType.TRAIN);

                    labeledData.setDataSourceId(train.getName());
                    node.addToOutputAndProcess(labeledData, CoreConstant.SlotContent.TRAINING_DATA.getSlotName());
                }

				// save results to the database										
				if (task.getSaveResults()){
					DataManagerService.saveResult(myAgent, task, task.getExperimentID());
				}
                Task taskResult = (Task)result.getValue();
                ErrorEdge errorEdge = new ErrorEdge(taskResult.getResult(),taskResult.getComputationID());
                node.addToOutputAndProcess(errorEdge,"error");
                node.decreaseNumberOfOutstandingTask();
			}
		} catch (CodecException | OntologyException e) {
			myAgent.logException(e.getMessage(), e);
		}

        // send subscription to the original agent after each received task
		myAgent.sendSubscription(inform, msg);
	}
}


