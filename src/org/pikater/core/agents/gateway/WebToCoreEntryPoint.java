package org.pikater.core.agents.gateway;

import org.pikater.core.agents.gateway.batch.PikaterGateway_KillBatch;
import org.pikater.core.agents.gateway.batch.PikaterGateway_NewBatch;
import org.pikater.core.agents.gateway.batchPriorityChanged.PikaterGatewayBatchPriorityChanged;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.agents.gateway.getAgentInfo.PikaterGateway_GetAgentInfo;
import org.pikater.core.agents.gateway.newDataset.PikaterGateway_NewDataset;
import org.pikater.core.agents.gateway.newagent.PikaterGateway_NewAgent;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfos;

public class WebToCoreEntryPoint {

	/**
	 * Constructor
	 */
	private WebToCoreEntryPoint(){
	}
	
	/**
	 * Gets required and relevant information about all agents used in the core
	 * system that can act as "boxes" in web's experiment editor.
	 * @throws PikaterGatewayException
	 */
	public static AgentInfos getAgentInfosVisibleForUser(int userID) throws PikaterGatewayException {
		return PikaterGateway_GetAgentInfo.getAgentInfosVisibleForUser(userID);
	}

	/**
	 * Notifies the core system about a new scheduled batch. The batch was
	 * scheduled by a user on the website. 
	 * @param IDNewBatch ID of the newly scheduled batch
	 * @throws PikaterGatewayException
	 */
	public static void notify_newBatch(int IDNewBatch, int userID) throws PikaterGatewayException {
		PikaterGateway_NewBatch.newBatch(IDNewBatch, userID);
	}
	
	/**
	 * Notifies the core system about a new batch priority. 
	 * @param BatchID ID of the batch
	 * @throws PikaterGatewayException
	 */
	public static void notify_batchPriorityChanged(int batchID) throws PikaterGatewayException {
		PikaterGatewayBatchPriorityChanged.changeBatchPriority(batchID);
	}
	
	/**
	 * Notifies the core system about a new user-uploaded agent. 
	 * @param externalAgentID ID of external agent's JPA entity 
	 * @throws PikaterGatewayException
	 */
	public static void notify_newAgent(int externalAgentID) throws PikaterGatewayException {
		PikaterGateway_NewAgent.newAgent(externalAgentID);
	}

	/**
	 * Notifies the core system about a new user-uploaded dataset. 
	 * @throws PikaterGatewayException
	 */
	public static void notify_newDataset(int IDnewDataset) throws PikaterGatewayException {
		PikaterGateway_NewDataset.newDataset(IDnewDataset);
	}
	
	/**
	 * Notifies Agent Planner to stop executing the batch.
	 * @param batchID ID of the batch to be stopped
	 * @throws PikaterGatewayException
	 */
	public static void notify_killBatch(int batchID) throws PikaterGatewayException{
		PikaterGateway_KillBatch.killBatch(batchID);
	}
	
	/**
	 * Is pikater core running locally on the same machine?
	 * @throws PikaterGatewayException 
	 */
	public static void checkLocalConnection() throws PikaterGatewayException 
	{
		PikaterGateway_GetAgentInfo.getAgentInfosVisibleForUser(-1);
	}
	
}
