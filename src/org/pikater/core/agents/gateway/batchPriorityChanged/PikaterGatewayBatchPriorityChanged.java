package org.pikater.core.agents.gateway.batchPriorityChanged;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.agents.gateway.Initiator;
import org.pikater.core.agents.gateway.PikaterGateway_General;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.subtrees.batch.BatchPriorityChanged;

public class PikaterGatewayBatchPriorityChanged {

	/**
	 * Constructor
	 */
	private PikaterGatewayBatchPriorityChanged() {
	}
	
	/**
	 * Changes the priority of the Batch
	 * @param batchID
	 * @throws PikaterGatewayException
	 */
	public static void ChangeBatchPriority(int batchID) throws PikaterGatewayException {
				
		try {	        
			BatchPriorityChanged batchPriorityChanged =
					new BatchPriorityChanged();
			batchPriorityChanged.setBatchID(batchID);

			Ontology batchOntology = BatchOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					CoreAgents.PLANNER.getName(), batchOntology,
					batchPriorityChanged);

        	Initiator initiator = new Initiator(msg);

        	PikaterGateway_General.generalRequest(initiator);
        	initiator.getOkResponse();

		} catch (ControllerException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} catch (CodecException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} catch (OntologyException e1) {
			throw new PikaterGatewayException("Failed to make request");
		} 

	}

}