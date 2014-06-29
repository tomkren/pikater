package org.pikater.core.agents.system.manager;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Result;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.leap.List;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.CAStartComputationStrategy;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Task;

import java.util.ArrayList;

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

				// save results to the database										
				if (t.isSave_results()){
					DataManagerService.saveResult(myAgent, t);
				}
				
				// fill the right queues in problem graph
				if (t.getOutputByName(Task.InOutType.ERRORS) != null){
					@SuppressWarnings("unchecked")
					ArrayList<Eval> errors = (ArrayList<Eval>) t.getOutputByName(Task.InOutType.ERRORS);
					
					strategy.processError(errors);
				}				
				
				if (t.getOutputByName(Task.InOutType.VALIDATION) != null){
					String dataSourceName = (String) t.getOutputByName(Task.InOutType.VALIDATION);
					
					strategy.processValidation(dataSourceName);
				}				
				
				// kdyz to bylo od searche, zmerguj; nebo searche resit vlastnim jadovskym chovanim?
				
				
				/*
				// send evaluation to search agent
				if (msg.getPerformative() == ACLMessage.QUERY_REF){
					// the original message was a query (sender of the task 
					// was s search agent)
				
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);

					ContentElement query_content = getContentManager().extractContent(msg);
					
					Result reply_result = new Result((Action) query_content, t.getResult());
					getContentManager().fillContent(reply, reply_result);
					
					send(reply);								
				}
				
				*/
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


