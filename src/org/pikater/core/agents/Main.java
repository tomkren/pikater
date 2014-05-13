package org.pikater.core.agents;

import jade.wrapper.gateway.JadeGateway;

import org.pikater.core.agents.gateway.Agent_PikaterGateway;


public class Main {

	public static void main(String [ ] args) {
		
		Class<Agent_PikaterGateway> gatewayClass = Agent_PikaterGateway.class;
		
		JadeGateway.init(gatewayClass.getName(), null);
		
	}
}
