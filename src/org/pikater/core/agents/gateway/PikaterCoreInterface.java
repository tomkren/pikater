package org.pikater.core.agents.gateway;

import org.pikater.core.agents.gateway.getAgentInfo.PikaterGateway_GetAgentInfo;
import org.pikater.core.agents.gateway.newAgent.PikaterGateway_NewAgent;
import org.pikater.core.agents.gateway.newBatch.PikaterGateway_NewBatch;
import org.pikater.core.agents.gateway.newDataset.PikaterGateway_NewDataset;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;

public class PikaterCoreInterface {

	/*
	 * Public interface Returns the ontology AgentInfos which contains the
	 * agentInfo of all in the Pikater known agents
	 */
	public static AgentInfos getAgentInfos() throws Exception {
		return PikaterGateway_GetAgentInfo.getAgentInfos();
	}

	/*
	 * Public interface Send the information to PikaterCore by using the
	 * NewBatch ontology which contains only ID-batch, that the new Batch was
	 * saved into the database.
	 */
	public static void newBatch(int IDNewBatch) throws Exception {
		PikaterGateway_NewBatch.newBatch(IDNewBatch);
	}

	/*
	 * Public interface Send the information to PikaterCore by using the
	 * NewAgent ontology which contains only agent className.
	 */
	public static void newAgent(Class<?> agentClass) throws Exception {
		PikaterGateway_NewAgent.newAgent(agentClass);
	}

	/*
	 * Public interface Send the information to PikaterCore by using the
	 * NewDataset ontology which contains only ID-dataset, that the new Dataset
	 * was saved into the database.
	 */
	public static void newDataset(int IDnewDataset) throws Exception {
		PikaterGateway_NewDataset.newDataset(IDnewDataset);
	}

	/*
	 * Test
	 */
	public static void main(String[] args) throws Exception {

		AgentInfos agentInfos = PikaterCoreInterface.getAgentInfos();

		for (AgentInfo agentInfoI : agentInfos.getAgentInfos()) {
			System.out.println("AgentInfo: " + agentInfoI.toString());
		}

		System.in.read();
	}
}
