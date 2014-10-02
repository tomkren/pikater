package org.pikater.core.agents.system.computation.planning.structures;

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
import org.pikater.core.agents.system.manager.ManagerAgentService;

public class SlaveServersStructure {

	private List<AID> slaveServers = new ArrayList<AID>();
	
	public List<AID> checkForNewSlaveServers(Agent_Planner agent) {
		
		 DFAgentDescription template = new DFAgentDescription();
		 ServiceDescription sd = new ServiceDescription();
		 sd.setType(Agent_ManagerAgent.class.getName());
		 template.addServices(sd);
		 
		 DFAgentDescription[] result = null;
		 try {
			result = DFService.search(agent, template);
		} catch (FIPAException e1) {
			agent.logException(e1.getMessage(), e1);
			return null;
		}
		List<DFAgentDescription> foundDFADescriptions =
				new ArrayList<DFAgentDescription>(Arrays.asList(result));
		
		List<AID> newSlaveServers = new ArrayList<AID>();

		for (DFAgentDescription dfaDescriptI : foundDFADescriptions) {
			AID aidI = dfaDescriptI.getName();
			if (! contains(aidI)) {
				newSlaveServers.add(aidI);
			}
		}
		
		slaveServers.addAll(newSlaveServers);
		
		return newSlaveServers;
	}

	public List<AID> checkForDeadSlaveServers(Agent_Planner agent) {
		
		List<AID> deadServers = new ArrayList<AID>();
		
		for (AID slaveServerAidI : slaveServers) {
						
			if (! ManagerAgentService.isPingOK(agent, slaveServerAidI)) {
				deadServers.add(slaveServerAidI);
			}
		}
		
		return deadServers;
	}
	
	private boolean contains(AID slaveServer) {
				
		for (AID dfaDescriptI : slaveServers) {
			String serverNameI = dfaDescriptI.getName();
			String newServerName = slaveServer.getName();

			if (serverNameI.equals(newServerName)) {
				return true;
			}
		}
		
		return false;
	}
	
}
