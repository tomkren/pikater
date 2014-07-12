package org.pikater.core.agents.gateway.newBatch;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.subtrees.batch.NewBatch;

public class PikaterGateway_NewBatch {

	public static void newBatch(int IDNewBatch) throws Exception {

		try {
	        JadeGateway.init(Agent_PikaterGateway.class.getName(), null);
	        
	        NewBatch newBatch = new NewBatch();
	        newBatch.setBatchId(IDNewBatch);

			Ontology batchOntology = BatchOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					AgentNames.MANAGER, batchOntology,
					newBatch);

			NewBatchInitiator initiator = new NewBatchInitiator(msg);
			JadeGateway.execute(initiator, 10000);
			initiator.getOkResponse();

			JadeGateway.shutdown();

		} catch (CodecException | OntologyException e1) {
			throw new Exception("Failed to make request");
		} catch (ControllerException | InterruptedException e) {
			Exception x = new Exception("JadeGateway.execute() failed");
			x.setStackTrace(e.getStackTrace());
			throw x;
		}

	}

}
