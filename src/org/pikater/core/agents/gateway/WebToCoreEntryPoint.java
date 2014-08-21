package org.pikater.core.agents.gateway;

import java.io.File;
import java.io.IOException;

import org.pikater.core.agents.gateway.batch.PikaterGateway_KillBatch;
import org.pikater.core.agents.gateway.batch.PikaterGateway_NewBatch;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.agents.gateway.getAgentInfo.PikaterGateway_GetAgentInfo;
import org.pikater.core.agents.gateway.newAgent.PikaterGateway_NewAgent;
import org.pikater.core.agents.gateway.newDataset.PikaterGateway_NewDataset;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.shared.database.jpa.daos.DAOs;

public class WebToCoreEntryPoint {

	/**
	 * Gets required and relevant information about all agents used in the core
	 * system that can act as "boxes" in web's experiment editor.
	 * @return
	 * @throws PikaterGatewayException
	 */
	public static AgentInfos getAgentInfos() throws PikaterGatewayException {
		return PikaterGateway_GetAgentInfo.getAgentInfos();
	}

	/**
	 * Notifies the core system about a new scheduled batch. The batch was
	 * scheduled by a user on the website. 
	 * @param IDNewBatch ID of the newly scheduled batch
	 * @throws PikaterGatewayException
	 */
	public static void notify_newBatch(int IDNewBatch) throws PikaterGatewayException {
		PikaterGateway_NewBatch.newBatch(IDNewBatch);
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
	 * @param IDnewDataset
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
	 * Get information about run of Pikater-Core.
	 */
	public static boolean isPikaterCoreRunning() {
		try {
			PikaterGateway_GetAgentInfo.getAgentInfos();
			return true;
		} catch (PikaterGatewayException t) {
			return false;
		}
	}

	/*
	 * Test
	 */
	public static void main(String[] args) throws Exception {
		
		PikaterGateway_General.MASTER_PORT=2100;//1099;
		
		AgentInfos agentInfos = WebToCoreEntryPoint.getAgentInfos();

		for (AgentInfo agentInfoI : agentInfos.getAgentInfos()) {
			System.out.println("AgentInfo: " + agentInfoI.getName());
		}

		int userID = 5859;
		
		WebToCoreEntryPoint.uploadDataset("core/datasets/weather.arff",  userID, "weather");
		WebToCoreEntryPoint.uploadDataset("core/datasets/iris.arff", userID, "iris");
		WebToCoreEntryPoint.uploadDataset("core/datasets/glass.arff", userID, "glass");
		WebToCoreEntryPoint.uploadDataset("core/datasets/autos.arff", userID, "autos");
		WebToCoreEntryPoint.uploadDataset("core/datasets/mushroom.arff", userID, "mushroom");
		WebToCoreEntryPoint.uploadDataset("core/datasets/ionosphere.arff", userID, "ionosphere");
		
	}
	
	private static void uploadDataset(String filename, int userID, String description) throws IOException{
		
		int id=DAOs.dataSetDAO.storeNewDataSet(new File(filename), description, userID);
		System.out.println("Dataset uploaded with ID : "+id);
		System.out.println("Demanding metadata computation...");
		try{
			WebToCoreEntryPoint.notify_newDataset(id);
			System.out.println("Metadata computation finished");
			
		}catch(PikaterGatewayException pgwe){
			System.err.println("Error during metadata computation");
		}
		
	}
}
