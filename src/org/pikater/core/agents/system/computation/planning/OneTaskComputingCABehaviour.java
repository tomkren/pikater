package org.pikater.core.agents.system.computation.planning;

import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import org.pikater.core.agents.system.Agent_Planner;

public class OneTaskComputingCABehaviour extends AchieveREInitiator {
	
	private static final long serialVersionUID = 1572211801881987607L;
	private Agent_Planner agent;

	public OneTaskComputingCABehaviour(Agent_Planner agent, ACLMessage msg) {
		super(agent, msg);
		this.agent = agent;
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		agent.respondToFinishedTask(inform);
		agent.logInfo("Agent " + inform.getSender().getName()
				+ " successfully performed the requested action");
	}

	@Override
	protected void handleAgree(ACLMessage inform) {
		agent.logInfo("Execute was Agreed");
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		agent.logSevere("Execute was refused");
	}

	@Override
	protected void handleFailure(ACLMessage failure) {
		agent.logSevere("Agent " + failure.getSender().getName()
				+ " failed to perform the requested action: "
				+ failure.getContent());
	}

}