package org.pikater.core.agents.system.openml;

import java.util.ArrayList;
import java.util.List;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.OpenMLOntology;
import org.pikater.core.ontology.subtrees.openml.Dataset;
import org.pikater.core.ontology.subtrees.openml.Datasets;
import org.pikater.core.ontology.subtrees.openml.ExportDataset;
import org.pikater.core.ontology.subtrees.openml.ExportedDataset;
import org.pikater.core.ontology.subtrees.openml.GetDatasets;
import org.pikater.core.ontology.subtrees.openml.ImportDataset;
import org.pikater.core.ontology.subtrees.openml.ImportedDataset;

public class OpenMLAgentService {

	static final Codec CODEC = new SLCodec();

	/**
	 * 
	 * @param agent
	 * @param name
	 * @param description
	 * @param path
	 * @param type
	 * @return dataset ID of the uploaded dataset or -1 if error occured
	 */
	public static int exportDataset(PikaterAgent agent, String name,
			String description, String path, String type) {

		AID openMLAgent = new AID(CoreAgents.OpenMLAgent.getName(), false);
		Ontology ontology = OpenMLOntology.getInstance();

		ExportDataset edataset = new ExportDataset();

		edataset.setPath(path);
		edataset.setName(name);
		edataset.setDescription(description);
		edataset.setType(type);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(openMLAgent);
		request.setLanguage(CODEC.getName());
		request.setOntology(ontology.getName());

		Action action = new Action();
		action.setAction(edataset);
		action.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(request, action);

		} catch (CodecException ce) {
			agent.logSevere(ce.getMessage());
		} catch (OntologyException oe) {
			agent.logSevere(oe.getMessage());
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, request);
		} catch (FIPAException e) {
			agent.logSevere(e.getMessage());
		}

		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logException(e1.getMessage(), e1);
		} catch (CodecException e1) {
			agent.logException(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logException(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;
			ExportedDataset exportedDataset = (ExportedDataset) result
					.getValue();

			return exportedDataset.getDid();

		} else {
			agent.logSevere("No Result ontology");
		}

		return -1;

	}
	
	public static String importDataset(PikaterAgent agent, int did, String destination) {
		
		AID openMLAgent = new AID(CoreAgents.OpenMLAgent.getName(), false);
		Ontology ontology = OpenMLOntology.getInstance();

		ImportDataset importDataset = new ImportDataset();
		importDataset.setDid(did);
		importDataset.setDestination(destination);

		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(openMLAgent);
		request.setLanguage(CODEC.getName());
		request.setOntology(ontology.getName());

		Action action = new Action();
		action.setAction(importDataset);
		action.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(request, action);

		} catch (CodecException ce) {
			agent.logSevere(ce.getMessage());
		} catch (OntologyException oe) {
			agent.logSevere(oe.getMessage());
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, request);
		} catch (FIPAException e) {
			agent.logSevere(e.getMessage());
		}

		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logException(e1.getMessage(), e1);
		} catch (CodecException e1) {
			agent.logException(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logException(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;
			ImportedDataset importedDataset =
					(ImportedDataset) result.getValue();

			return importedDataset.getPath();

		} else {
			agent.logSevere("No Result ontology");
		}
		
		return null;

	}

	
	public static List<Dataset> getDatasets(PikaterAgent agent) {

		try {
			AID openMLAgent = new AID(CoreAgents.OpenMLAgent.getName(), false);
			Ontology ontology = OpenMLOntology.getInstance();

			GetDatasets gd = new GetDatasets();
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(openMLAgent);
			request.setLanguage(CODEC.getName());
			request.setOntology(ontology.getName());

			Action action = new Action();
			action.setAction(gd);
			action.setActor(agent.getAID());

			try {
				// Let JADE convert from Java objects to string
				agent.getContentManager().fillContent(request, action);

			} catch (CodecException ce) {
				agent.logSevere(ce.getMessage());
			} catch (OntologyException oe) {
				agent.logSevere(oe.getMessage());
			}

			ACLMessage reply = null;
			try {
				reply = FIPAService.doFipaRequestClient(agent, request);
			} catch (FIPAException e) {
				agent.logSevere(e.getMessage());
			}

			ContentElement content = null;
			try {
				content = agent.getContentManager().extractContent(reply);
			} catch (UngroundedException e1) {
				agent.logException(e1.getMessage(), e1);
			} catch (CodecException e1) {
				agent.logException(e1.getMessage(), e1);
			} catch (OntologyException e1) {
				agent.logException(e1.getMessage(), e1);
			}

			if (content instanceof Result) {
				Result result = (Result) content;
				org.pikater.core.ontology.subtrees.openml.Datasets datasets = (Datasets) result
						.getValue();
				jade.util.leap.List datasetsList = datasets.getDatasets();

				List<Dataset> resultList = new ArrayList<Dataset>();

				Dataset ds;

				for (int i = 0; i < datasetsList.size(); i++) {
					ds = (Dataset) datasetsList.get(i);
					resultList.add(ds);
				}

				return resultList;
			} else {
				agent.logSevere("No Result ontology");
			}

			return null;
		} catch (Exception e) {
			agent.logSevere(e.getMessage());

			return null;
		}
	}

}
