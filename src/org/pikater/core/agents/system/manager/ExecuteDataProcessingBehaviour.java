package org.pikater.core.agents.system.manager;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Result;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.CAStartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.DataProcessingStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.ErrorEdge;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.task.Task;

public class ExecuteDataProcessingBehaviour extends AchieveREInitiator{

	private static final long serialVersionUID = -2044738642107219180L;

	private Agent_Manager myAgent;
	private DataProcessingStrategy strategy;
	private ACLMessage msg; // original message sent by whoever wants to
	 						// compute the task (either search agent or
							// gui agent);
							// to be able to send a reply

	public ExecuteDataProcessingBehaviour(Agent_Manager a, ACLMessage req,
                                          ACLMessage msg, DataProcessingStrategy cs) {
		super(a, req);
		myAgent = a;
        this.msg = msg;
		strategy = cs;
	}

	protected void handleRefuse(ACLMessage refuse) {
		myAgent.log("Agent "+refuse.getSender().getName()+" refused.", 1);
	}
	
	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			myAgent. log("Responder does not exist", 1);
		}
		else {
			myAgent.log("Agent "+failure.getSender().getName()+" failed.", 1);	            
		}
	}
	
	protected void handleInform(ACLMessage inform) {
		myAgent.log("Agent "+inform.getSender().getName()+" successfully performed the requested action.");

		
		ContentElement content;
		try {
			content = myAgent.getContentManager().extractContent(inform);
			if (content instanceof Result) {
				// get the original task from msg
				Result result = (Result) content;					
				Task t = (Task)result.getValue();
                ComputationCollectionItem computation =
                		myAgent.getComputation(t.getGraphID());
                ComputationNode node =
                		computation.getProblemGraph().getNode(t.getNodeID());
                if (node.ContainsOutput("file"))
                {
                    DataSourceEdge labeledData = new DataSourceEdge();
                    node.addToOutputAndProcess(labeledData,"file");
                }

				// save results to the database										
				if (t.getSaveResults()){
					DataManagerService.saveResult(myAgent, t, t.getExperimentID());
				}
                Task task=(Task)result.getValue();
                ErrorEdge errorEdge=new ErrorEdge(task.getResult(),task.getComputationID());
                node.addToOutputAndProcess(errorEdge,"error");
                node.computationFinished();
			}

		} catch (UngroundedException e) {
			myAgent.logError(e.getMessage(), e);
		} catch (CodecException e) {
			myAgent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			myAgent.logError(e.getMessage(), e);
		}
		
		// send subscription to the original agent after each received task
		myAgent.sendSubscription(inform, msg);
	}
} // end of ExecuteTask ("send request to planner agent") bahavior


