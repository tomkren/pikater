package org.pikater.core.agents.system.planner.dataStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import org.pikater.core.agents.system.Agent_ManagerAgent;
import org.pikater.core.agents.system.Agent_Planner;

public class SlaveServersStructure {

	private List<DFAgentDescription> slaveServers =
			new ArrayList<DFAgentDescription>();
	
	public List<AID> scannNewSlaveServers(Agent_Planner agent) {
		
		 DFAgentDescription template = new DFAgentDescription();
		 ServiceDescription sd = new ServiceDescription();
		 sd.setType(Agent_ManagerAgent.class.getName());
		 template.addServices(sd);
		 
		 DFAgentDescription[] result = null;
		 try {
			result = DFService.search(agent, template);
		} catch (FIPAException e1) {
			agent.logError(e1.getMessage(), e1);
		}
		List<DFAgentDescription> foundDFADescriptions =
				new ArrayList<DFAgentDescription>(Arrays.asList(result));
		
		
		List<AID> newSlaveServers = new ArrayList<AID>();

		for (DFAgentDescription dfaDescriptI : foundDFADescriptions) {
			if (! contains(dfaDescriptI)) {
				newSlaveServers.add(dfaDescriptI.getName());
			}
		}
		
		slaveServers.addAll(foundDFADescriptions);
		
		return newSlaveServers;
	}
	
	private boolean contains(DFAgentDescription slaveServer) {
				
		for (DFAgentDescription dfaDescriptI : slaveServers) {
			String serverNameI = dfaDescriptI.getName().getLocalName();
			String newServerName = slaveServer.getName().getLocalName();
			if (serverNameI.equals(newServerName)) {
				return true;
			}
		}
		
		return false;
	}
	
}
