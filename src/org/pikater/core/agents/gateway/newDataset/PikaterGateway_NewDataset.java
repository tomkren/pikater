package org.pikater.core.agents.gateway.newDataset;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.agents.gateway.newBatch.NewBatchInitiator;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.subtrees.metadata.NewDataset;

public class PikaterGateway_NewDataset {

	public static void newDataset(int IDNewDataset) throws Exception {

		try {
			Class<Agent_PikaterGateway> gateway = Agent_PikaterGateway.class;
			JadeGateway.init(gateway.getName(), null);

			NewDataset newDataset = new NewDataset();
			newDataset.setDataSetID(IDNewDataset);

			Ontology metadataOntology = MetadataOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					AgentNames.FREDDIE, metadataOntology, newDataset);

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
