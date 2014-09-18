package org.pikater.core.agents.system.manager;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computation.graph.edges.ErrorEdge;
import org.pikater.core.agents.system.computation.graph.strategies.SearchStartComputationStrategy;
import org.pikater.core.ontology.subtrees.task.Evaluation;

public class StartGettingParametersFromSearch extends AchieveREInitiator {

	private static final long serialVersionUID = 7028866964341806289L;

	private Agent_Manager myAgent;
	private ACLMessage msg;
	private SearchStartComputationStrategy strategy;	
	
	
	public StartGettingParametersFromSearch(Agent_Manager a, ACLMessage req, ACLMessage msg,
			SearchStartComputationStrategy cs) {
		super(a, msg);
		
		this.myAgent = a;
		this.msg = msg;
		this.strategy = cs;
		
        myAgent.logSevere("StartGettingParametersFromSearch behavior created.");
	}

	protected void handleAgree(ACLMessage agree) {
		strategy.getComputationNode().computationFinished();
	}
			
	protected void handleInform(ACLMessage inform) {
		// sending of Options have been finished
		
		myAgent.logSevere("Agent " + inform.getSender().getName()
				+ ": sending of Options have been finished.");
        
        // send subscription to the original agent after each received task
        myAgent.sendSubscription(inform, msg);
        
        
        // get Evaluation from inform
        try {			

			Result r = (Result)myAgent.getContentManager().extractContent(inform);
			ErrorEdge eo = new ErrorEdge((Evaluation)r.getItems().get(0), 0); // TODO! - search in search
	        strategy.getComputationNode().addToOutputAndProcess(eo, "error");
														
		} catch (CodecException e) {
			myAgent.logException(e.getMessage(), e);
			e.printStackTrace();
		} catch (OntologyException e) {
			myAgent.logException(e.getMessage(), e);
			e.printStackTrace();
		}
														
        /* prepare results, send them to GUI?, save to xml
		 * prepareTaskResults(ACLMessage resultmsg, String problemID)
		 *   - asi jenom pro searche?
		 * save resutls to xml file
		 
		 if (!no_xml_output){
			writeXMLResults(results);
		}
		*/	        		
	}
			
	protected void handleRefuse(ACLMessage refuse) {
        myAgent.logSevere("Agent " + refuse.getSender().getName()
				+ " refused to perform the requested action.");
	}

	protected void handleFailure(ACLMessage failure) {
        myAgent.logSevere("Agent "+ failure.getSender().getName()
				+ ": failure while performing the requested action");
	}			
}
