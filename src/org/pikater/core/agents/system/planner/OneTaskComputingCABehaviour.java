package org.pikater.core.agents.system.planner;

import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import org.pikater.core.agents.PikaterAgent;

public class OneTaskComputingCABehaviour extends AchieveREInitiator {
	
	private static final long serialVersionUID = 1572211801881987607L;
	private PikaterAgent agent;

	public OneTaskComputingCABehaviour(PikaterAgent agent, ACLMessage msg) {
		super(agent, msg);
		this.agent = agent;
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		agent.log("Agent " + inform.getSender().getName()
				+ " successfully performed the requested action");
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		agent.logError("Execute was refused");
	}

	@Override
	protected void handleFailure(ACLMessage failure) {
		agent.logError("Agent " + failure.getSender().getName()
				+ " failed to perform the requested action: "
				+ failure.getContent());
	}

}