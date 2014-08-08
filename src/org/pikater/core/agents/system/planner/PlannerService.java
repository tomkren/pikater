package org.pikater.core.agents.system.planner;

import org.pikater.core.agents.PikaterAgent;

public class PlannerService {

	private PikaterAgent agent;
	
	public PlannerService(PikaterAgent agent) {
		this.agent = agent;
	}
	
	public void sendGetSystemLoad() {
		agent.logError("Not implemented");
	}

	public void sendKillTasks() {
		agent.logError("Not implemented");
	}
	
}
