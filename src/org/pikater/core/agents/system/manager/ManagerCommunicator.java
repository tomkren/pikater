package org.pikater.core.agents.system.manager;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import java.util.Date;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batch.SaveBatch;
import org.pikater.core.ontology.subtrees.batch.SavedBatch;
import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.core.ontology.subtrees.experiment.SaveExperiment;
import org.pikater.core.ontology.subtrees.experiment.SavedExperiment;

public class ManagerCommunicator {

	/*
	 * Sends to save the Batch to the Agent_DataManger
	 * Returns Batch-ID
	 */
	public int saveBatch(PikaterAgent agent, Batch batch) {
		
		SaveBatch saveBatch = new SaveBatch();
		saveBatch.setBatch(batch);
        
        Ontology ontology = BatchOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        msg.setLanguage(agent.getCodec().getName());
        msg.setOntology(ontology.getName());
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(saveBatch);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CodecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OntologyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (content instanceof Result) {
			Result result = (Result) content;
			
			SavedBatch savedBatch = (SavedBatch) result.getValue();
			agent.log(savedBatch.getMessage());
			
			return savedBatch.getSavedBatchId();
		} else {
			agent.logError("Error - No Result ontology");
		}
		
		return -1;
	}

	/*
	 * Sends to save the Experiment to the Agent_DataManger
	 * Returns Experiment-ID
	 */
	public int saveExperiment(PikaterAgent agent, Experiment experiment) {
		
		SaveExperiment saveExperiment = new SaveExperiment();
		saveExperiment.setExperiment(experiment);
        
        Ontology ontology = ExperimentOntology.getInstance();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        msg.setLanguage(agent.getCodec().getName());
        msg.setOntology(ontology.getName());
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(saveExperiment);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CodecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OntologyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (content instanceof Result) {
			Result result = (Result) content;
			
			SavedExperiment savedExperiment = (SavedExperiment) result.getValue();
			agent.log(savedExperiment.getMessage());
			
			return savedExperiment.getSavedExperimentId();
		} else {
			agent.logError("Error - No Result ontology");
		}
		
		return -1;
	}
	
	public void updateBatchStatus(PikaterAgent agent, int batchID, String batchStatus) {
	}
	
	public void updateExperimentStatus(PikaterAgent agent, int experimentID, String experimentStatus) {
	}
	
}
