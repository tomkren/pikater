package org.pikater.core.agents.system.manager;

import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies.SearchStartComputationStrategy;

public class StartGettingParametersFromSearch extends AchieveREInitiator {

	private static final long serialVersionUID = 7028866964341806289L;

	private Agent_Manager myAgent;
	private ACLMessage msg;
	private SearchStartComputationStrategy strategy;	
	
	
	public StartGettingParametersFromSearch(Agent_Manager a, ACLMessage req, ACLMessage msg,
			SearchStartComputationStrategy cs) {
		super(a, msg);
		this.myAgent = a;
		this.strategy = cs;
		this.msg = msg;
		
        myAgent.log("StartGettingParametersFromSearch behavior created.", 2);
	}

	protected void handleAgree(ACLMessage agree) {
		strategy.getComputationNode().computationFinished();
	}
			
	protected void handleInform(ACLMessage inform) {
		// sending of Options have been finished
		
		myAgent.log("Agent " + inform.getSender().getName()
				+ ": sending of Options have been finished.", 2);
        
        strategy.finished();
        
        // send subscription to the original agent after each received task
        myAgent.sendSubscription(inform, msg);
        

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
        myAgent.log("Agent " + refuse.getSender().getName()
				+ " refused to perform the requested action.", 1);
	}

	protected void handleFailure(ACLMessage failure) {
        myAgent.log("Agent "+ failure.getSender().getName()
				+ ": failure while performing the requested action", 1);
	}			
}
