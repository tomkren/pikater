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

public class PikaterCoreInterface {

	/*
	 * Public interface Returns the ontology AgentInfos which contains the
	 * agentInfo of all in the Pikater known agents
	 */
	public static AgentInfos getAgentInfos() throws PikaterGatewayException {
		return PikaterGateway_GetAgentInfo.getAgentInfos();
	}

	/*
	 * Public interface Send the information to PikaterCore by using the
	 * NewBatch ontology which contains only ID-batch, that the new Batch was
	 * saved into the database.
	 */
	public static void newBatch(int IDNewBatch) throws PikaterGatewayException {
		PikaterGateway_NewBatch.newBatch(IDNewBatch);
	}

	/*
	 * Public interface Send the information to PikaterCore by using the
	 * NewAgent ontology which contains only agent className.
	 */
	public static void newAgent(Class<?> agentClass) throws PikaterGatewayException {
		PikaterGateway_NewAgent.newAgent(agentClass);
	}

	/*
	 * Public interface Send the information to PikaterCore by using the
	 * NewDataset ontology which contains only ID-dataset, that the new Dataset
	 * was saved into the database.
	 */
	public static void newDataset(int IDnewDataset) throws PikaterGatewayException {
		PikaterGateway_NewDataset.newDataset(IDnewDataset);
	}

	/*
	 * Test
	 */
	public static void main(String[] args) throws Exception {

		PikaterGateway_General.MASTER_PORT=2100;
		
		AgentInfos agentInfos = PikaterCoreInterface.getAgentInfos();

		for (AgentInfo agentInfoI : agentInfos.getAgentInfos()) {
			System.out.println("AgentInfo: " + agentInfoI.getName());
		}

		JPAUser owner=new ResultFormatter<JPAUser>(DAOs.userDAO.getByLogin("sp")).getSingleResultWithNull();
		JPADataSetLO init= new JPADataSetLO(owner, "ionosphere");
		DAOs.dataSetDAO.storeNewDataSet(new File("core/datasets/ionosphere.arff"),init);
		System.out.println("Dataset uploaded with ID : "+init.getId());
		System.out.println("Demanding metadata computation...");
		try{
			PikaterCoreInterface.newDataset(init.getId());
			System.out.println("Metadata computation finished");
			
		}catch(PikaterGatewayException pgwe){
			System.err.println("Error during metadata computation");
		}
		
	}
}
