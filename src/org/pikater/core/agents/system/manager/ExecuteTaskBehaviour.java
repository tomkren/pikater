package org.pikater.core.agents.system.manager;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Result;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computation.graph.ModelComputationNode;
import org.pikater.core.agents.system.computation.graph.edges.DataSourceEdge;
import org.pikater.core.agents.system.computation.graph.edges.ErrorEdge;
import org.pikater.core.agents.system.computation.graph.strategies.CAStartComputationStrategy;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;

public class ExecuteTaskBehaviour extends AchieveREInitiator{

	private static final long serialVersionUID = -2044738642107219180L;

	private Agent_Manager myAgent;
	@SuppressWarnings("unused")
	private CAStartComputationStrategy strategy;
    private final ModelComputationNode node;
    // original message sent by whoever wants to
    // compute the task (either search agent or
    // gui agent);
    // to be able to send a reply
    private ACLMessage msg;
	
	public ExecuteTaskBehaviour(Agent_Manager a, ACLMessage req,
                                ACLMessage msg, CAStartComputationStrategy cs, ModelComputationNode node) {
		super(a, req);
		myAgent = a;
        this.msg = msg;
		strategy = cs;
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
		myAgent.logInfo("Agent "+inform.getSender().getName()+" successfully performed the requested action.");
		
		ContentElement content;
		try {
			content = myAgent.getContentManager().extractContent(inform);
			if (content instanceof Result) {
				// get the original task from msg
				Result result = (Result) content;					
				Task t = (Task)result.getValue();
                // ComputationCollectionItem computation = myAgent.getComputation(t.getBatchID()); // unused
                DataSourceEdge labeledData = new DataSourceEdge();
                labeledData.setFile(false);

                if (node.containsOutput("file"))
                {
                    TaskOutput data= t.getOutputByType(Task.InOutType.DATA);
                    if (data==null)
                    {
                        data=t.getOutput().get(0);
                    }
                    labeledData.setDataSourceId(data.getName());
                    node.addToOutputAndProcess(labeledData,"file");
                }
                if (node.containsOutput(CoreConstant.Slot.SLOT_TESTING_DATA.get()))
                {
                    TaskOutput test= t.getOutputByType(Task.InOutType.TEST);
                    if (test==null)
                    {
                        test=t.getOutputByType(Task.InOutType.TRAIN);
                    }
                    labeledData.setDataSourceId(test.getName());
                    node.addToOutputAndProcess(labeledData, CoreConstant.Slot.SLOT_TESTING_DATA.get());
                }
                if (node.containsOutput(CoreConstant.Slot.SLOT_TRAINING_DATA.get()))
                {
                    TaskOutput train= t.getOutputByType(Task.InOutType.TRAIN);

                    labeledData.setDataSourceId(train.getName());
                    node.addToOutputAndProcess(labeledData, CoreConstant.Slot.SLOT_TRAINING_DATA.get());
                }

				// save results to the database										
				if (t.getSaveResults()){
					DataManagerService.saveResult(myAgent, t, t.getExperimentID());
				}
                Task task=(Task)result.getValue();
                ErrorEdge errorEdge=new ErrorEdge(task.getResult(),task.getComputationID());
                node.addToOutputAndProcess(errorEdge,"error");
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
}


