package org.pikater.core.agents.gateway.newDataset;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.gateway.Agent_PikaterGateway;
import org.pikater.core.agents.gateway.Initiator;
import org.pikater.core.agents.gateway.PikaterGateway_General;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.subtrees.metadata.NewDataset;

public class PikaterGateway_NewDataset {

	public static void newDataset(int IDNewDataset) throws PikaterGatewayException {

		try {
			NewDataset newDataset = new NewDataset();
			newDataset.setDataSetID(IDNewDataset);

			Ontology metadataOntology = MetadataOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					AgentNames.FREDDIE, metadataOntology, newDataset);

			Initiator initiator = new Initiator(msg);
			PikaterGateway_General.generalRequest(initiator);
			initiator.getOkResponse();

		}catch (ControllerException | CodecException | OntologyException e1) {
			throw new PikaterGatewayException("Failed to make request");
		}

	}
}
