package org.pikater.core.agents.system.metadata;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.subtrees.metadata.NewComputedData;
import org.pikater.core.ontology.subtrees.metadata.NewDataset;

public class MetadataService {
	
	public static void requestMetadataForDataset(PikaterAgent agent,
			int dataSetID, int userID) {
		
		AID receiver = new AID(AgentNames.METADATA_QUEEN, false);

		NewDataset nds = new NewDataset();
		nds.setUserID(userID);
		nds.setDataSetID(dataSetID);
		
		agent.log("Sending request to store metadata for DataSetID: " + dataSetID);

		try {
			Ontology ontology = MetadataOntology.getInstance();

			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(receiver);
			request.setLanguage(agent.getCodec().getName());
			request.setOntology(ontology.getName());
			agent.getContentManager().fillContent(request, new Action(receiver, nds));

			ACLMessage reply = FIPAService.doFipaRequestClient(agent, request, 10000);
			if (reply == null)
				agent.logError("Reply not received.");
			else
				agent.log("Reply received: " + ACLMessage.getPerformative(reply.getPerformative()) + " " + reply.getContent());
		} catch (CodecException e) {
			agent.logError("Codec error occurred: " + e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError("Ontology error occurred: " + e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError("FIPA error occurred: " + e.getMessage(), e);
		}
	}
	
	public static void requestMetadataForComputedData(PikaterAgent agent,
			int computedDataID, int userID) {

		AID receiver = new AID(AgentNames.METADATA_QUEEN, false);

		NewComputedData ncd = new NewComputedData();
		ncd.setUserID(userID);
		ncd.setComputedDataID(computedDataID);
		
		agent.log("Sending request to store metadata for ComputedDataID: " + computedDataID);

		try {
			Ontology ontology = MetadataOntology.getInstance();

			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(receiver);
			request.setLanguage(agent.getCodec().getName());
			request.setOntology(ontology.getName());
			agent.getContentManager().fillContent(request, new Action(receiver, ncd));

			ACLMessage reply = FIPAService.doFipaRequestClient(agent, request);
			if (reply == null)
				agent.logError("Reply not received.");
			else
				agent.log("Reply received: " + ACLMessage.getPerformative(reply.getPerformative()) + " " + reply.getContent());
		} catch (CodecException e) {
			agent.logError("Codec error occurred: " + e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError("Ontology error occurred: " + e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError("FIPA error occurred: " + e.getMessage(), e);
		}
	}
	
}
