package org.pikater.core.agents.gateway;

import java.io.File;

import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.agents.gateway.getAgentInfo.PikaterGateway_GetAgentInfo;
import org.pikater.core.agents.gateway.newAgent.PikaterGateway_NewAgent;
import org.pikater.core.agents.gateway.newBatch.PikaterGateway_NewBatch;
import org.pikater.core.agents.gateway.newDataset.PikaterGateway_NewDataset;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.utils.ResultFormatter;

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
	 * TODO: should validity be checked for on the web (before passing the information
	 * to core)?
	 * @param agentClass class representation of the uploaded jar's agent 
	 * @throws PikaterGatewayException
	 */
	public static void notify_newAgent(Class<?> agentClass) throws PikaterGatewayException {
		PikaterGateway_NewAgent.newAgent(agentClass);
	}

	/**
	 * Notifies the core system about a new user-uploaded dataset. 
	 * @param IDnewDataset
	 * @throws PikaterGatewayException
	 */
	public static void notify_newDataset(int IDnewDataset) throws PikaterGatewayException {
		PikaterGateway_NewDataset.newDataset(IDnewDataset);
	}

	/*
	 * Test
	 */
	public static void main(String[] args) throws Exception {

		PikaterGateway_General.MASTER_PORT=2100;
		
		AgentInfos agentInfos = WebToCoreEntryPoint.getAgentInfos();

		for (AgentInfo agentInfoI : agentInfos.getAgentInfos()) {
			System.out.println("AgentInfo: " + agentInfoI.getName());
		}

		JPAUser owner=new ResultFormatter<JPAUser>(DAOs.userDAO.getByLogin("sp")).getSingleResultWithNull();
		JPADataSetLO init= new JPADataSetLO(owner, "ionosphere");
		DAOs.dataSetDAO.storeNewDataSet(new File("core/datasets/ionosphere.arff"),init);
		System.out.println("Dataset uploaded with ID : "+init.getId());
		System.out.println("Demanding metadata computation...");
		try{
			WebToCoreEntryPoint.notify_newDataset(init.getId());
			System.out.println("Metadata computation finished");
			
		}catch(PikaterGatewayException pgwe){
			System.err.println("Error during metadata computation");
		}
		
	}
}
