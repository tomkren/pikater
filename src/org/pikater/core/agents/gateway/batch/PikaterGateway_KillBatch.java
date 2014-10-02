package org.pikater.core.agents.gateway.batch;

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
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.task.KillTasks;

public class PikaterGateway_KillBatch {

	public static void killBatch(int batchID) throws PikaterGatewayException {

		try {
			
	        KillTasks killBatch = new KillTasks();
	        killBatch.setBatchID(batchID);

			Ontology ontology = TaskOntology.getInstance();

			ACLMessage msg = Agent_PikaterGateway.makeActionRequest(
					CoreAgents.PLANNER.getName(), ontology,
					killBatch);

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
