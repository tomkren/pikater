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
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;
import org.pikater.core.agents.system.computationDescriptionParser.edges.ErrorEdge;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.task.Task;

public class ExecuteTaskBehaviour extends AchieveREInitiator{

	private static final long serialVersionUID = -2044738642107219180L;

	private Agent_Manager myAgent;
	private CAStartComputationStrategy strategy;
	private ACLMessage msg; // original message sent by whoever wants to
	 						// compute the task (either search agent or 
							// gui agent);
							// to be able to send a reply
	
	public ExecuteTaskBehaviour(Agent_Manager a, ACLMessage req, 
			ACLMessage msg, CAStartComputationStrategy cs) {
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
		
		// when all tasks' results are sent, send reply-inform to gui agent
		if (isLastTask()){			
			myAgent.log("All results sent.");				

			ACLMessage msgOut = msg.createReply();
			msgOut.setPerformative(ACLMessage.INFORM);
			msgOut.setContent("Finished");

			myAgent.send(msgOut);
			
			
			/* TODO: prepare results, send them to GUI?, save to xml
			 * prepareTaskResults(ACLMessage resultmsg, String problemID)
			 *   - asi jenom pro searche?
			 * save resutls to xml file
			 
			 if (!no_xml_output){
				writeXMLResults(results);
			}
			*/				
		}						
		
		ContentElement content;
		try {
			content = myAgent.getContentManager().extractContent(inform);
			if (content instanceof Result) {
				// get the original task from msg
				Result result = (Result) content;					
				Task t = (Task)result.getValue();
                ComputationCollectionItem computation= myAgent.getComputation(t.getGraphId());
                ComputationNode node= computation.getProblemGraph().getNode(t.getNodeId());
                if (node.ContainsOutput("file"))
                {
                    DataSourceEdge labeledData =new DataSourceEdge();
                    node.addToOutputAndProcess(labeledData,"file");
                }

				// save results to the database										
				if (t.isSave_results()){
					DataManagerService.saveResult(myAgent, t);
				}
                ErrorEdge errorEdge=new ErrorEdge(((Task)result.getValue()).getResult());
                node.addToOutputAndProcess(errorEdge,"error");
                node.computationFinished();
				// fill the right queues in problem graph
                //TODO: add after search strategy is completed
//				if (t.getOutputByName(Task.InOutType.ERRORS) != null){
//					@SuppressWarnings("unchecked")
//					ArrayList<Eval> errors = (ArrayList<Eval>) t.getOutputByName(Task.InOutType.ERRORS);
//
//					strategy.processError(errors);
//				}
				     //TODO: what is this? just send labeled data?
//				if (t.getOutputByName(Task.InOutType.VALIDATION) != null){
//					String dataSourceName = (String) t.getOutputByName(Task.InOutType.VALIDATION);
//
//					strategy.processValidation(dataSourceName);
//				}
			}

		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
		// send subscription to the original agent after each received task
		myAgent.sendSubscription(inform, msg);
	}
		
	private boolean isLastTask(){
		// TODO - return true if there is not anything to compute in the graph
		
		return false;
	}

} // end of ExecuteTask ("send request to planner agent") bahavior


