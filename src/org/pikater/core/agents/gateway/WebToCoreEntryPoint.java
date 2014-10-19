package org.pikater.core.agents.gateway;

import java.io.File;
import java.io.IOException;

import org.pikater.core.agents.gateway.batch.PikaterGateway_KillBatch;
import org.pikater.core.agents.gateway.batch.PikaterGateway_NewBatch;
import org.pikater.core.agents.gateway.batchPriorityChanged.PikaterGatewayBatchPriorityChanged;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.agents.gateway.getAgentInfo.PikaterGateway_GetAgentInfo;
import org.pikater.core.agents.gateway.newDataset.PikaterGateway_NewDataset;
import org.pikater.core.agents.gateway.newagent.PikaterGateway_NewAgent;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfos;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPADatasetSource;

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

	/*
	 * Test
	 */
	public static void main(String[] args) throws Exception {
		
		AgentInfos agentInfos = WebToCoreEntryPoint.getAgentInfosVisibleForUser(-1);

		for (AgentInfo agentInfoI : agentInfos.getAgentInfos()) {
			System.out.println("AgentInfo: " + agentInfoI.getName());
		}
		
		int userID = 5859;
		
		WebToCoreEntryPoint.uploadDataset("weather.arff",  userID, "weather");
		WebToCoreEntryPoint.uploadDataset("iris.arff", userID, "iris");
		WebToCoreEntryPoint.uploadDataset("glass.arff", userID, "glass");
		WebToCoreEntryPoint.uploadDataset("autos.arff", userID, "autos");
		WebToCoreEntryPoint.uploadDataset("mushroom.arff", userID, "mushroom");
		WebToCoreEntryPoint.uploadDataset("ionosphere.arff", userID, "ionosphere");
		
	}
	
	private static void uploadDataset(String filename, int userID, String description) throws IOException{
		
		int id=DAOs.dataSetDAO.storeNewDataSet(new File("core/datasets/" + filename), filename, description, userID,JPADatasetSource.USER_UPLOAD);
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
