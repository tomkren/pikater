package org.pikater.core.agents.system;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openml.apiconnector.io.OpenmlConnector;
import org.openml.apiconnector.xml.DataSetDescription;
import org.openml.apiconnector.xml.UploadDataSet;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.OpenMLOntology;
import org.pikater.core.ontology.subtrees.openml.Dataset;
import org.pikater.core.ontology.subtrees.openml.Datasets;
import org.pikater.core.ontology.subtrees.openml.ExportDataset;
import org.pikater.core.ontology.subtrees.openml.ExportedDataset;
import org.pikater.core.ontology.subtrees.openml.GetDatasets;
import org.pikater.core.ontology.subtrees.openml.ImportDataset;
import org.pikater.core.ontology.subtrees.openml.ImportedDataset;

public class Agent_OpenML extends PikaterAgent {

	private static final long serialVersionUID = -501209274309520899L;
	
	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(OpenMLOntology.getInstance());
		return ontologies;
	}

	
	@Override
	protected void setup() {
		
		initDefault();
		registerWithDF();

		File data = new File(CoreConfiguration.getDataFilesPath() + "temp");
		
		if (!data.exists()) {
			logInfo("Creating directory: " +
					CoreConfiguration.getDataFilesPath());
			if (data.mkdirs()) {
				logInfo("Succesfully created directory: " +
						CoreConfiguration.getDataFilesPath());
			} else {
				logSevere("Error creating directory: " +
						CoreConfiguration.getDataFilesPath());
			}
		}

		MessageTemplate mesTemplate =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mesTemplate) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request)
					throws NotUnderstoodException, RefuseException {

				try {
					Action action = (Action) getContentManager()
							.extractContent(request);

					/*
					 * OpenML action
					 */
					if (action.getAction() instanceof GetDatasets) {
						return respondToGetDatasets(request, action);
					}

					if (action.getAction() instanceof ImportDataset) {
						return respondToImportDataset(request, action);
					}
					
					if (action.getAction() instanceof ExportDataset) {
						return respondToExportDataset(request, action);
					}
					
				} catch (OntologyException e) {
					logException(
							"Problem extracting content: " + e.getMessage(), e);
				} catch (CodecException e) {
					logException("Codec problem: " + e.getMessage(), e);
				} catch (Exception e) {
					logException(e.getMessage(), e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logSevere("Failure responding to request: "
						+ request.getConversationId());
				return failure;
			}

		});

	}
	
	private OpenmlConnector connector = null;
	private OpenmlConnector getOpenmlConnector() {
		
		if(connector == null){
			connector = new OpenmlConnector(
					"bc.stepan.balcar@gmail.com","SrapRoPy");
		}
		
		return connector;
	}
	
	private ACLMessage respondToGetDatasets(ACLMessage request,
			Action action) {

		logInfo("OpenML - respondToGetDatasets");

		@SuppressWarnings("unused")
		GetDatasets getDatasets = (GetDatasets) action.getAction();

		
		OpenmlConnector connector = getOpenmlConnector();
		
		Datasets datasets = new Datasets();
		
		
		try {
			JSONObject data = connector.openmlFreeQuery("SELECT * FROM dataset");
			
			JSONArray jsondatasets = data.getJSONArray("data");
			
			for(int i=0;i<jsondatasets.length();i++){
		    	
		    	JSONArray current = (JSONArray) jsondatasets.get(i);
		    	
		    	long did = current.getLong(0);
		    	
		    	String name = current.getString(3);
		    	String type = current.getString(7); // type
		    	//String description = current.getString(6);//extension name
		    	String date = current.getString(31);
		    	
		    	Dataset d = new Dataset();
		    	d.setDid(did);
		    	d.setName(name);
		    	d.setDate(date);
		    	d.setType(type);
		    	
		    	datasets.getDatasets().add(d);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/**
		Dataset d1 = new Dataset();
		d1.setDid(123);
		d1.setDescription("First Dummy Dataset");
		d1.setName("First");

		datasets.getDatasets().add(d1);

		Dataset d2 = new Dataset();
		d2.setDid(12345);
		d2.setDescription("Second Dummy Dataset");
		d2.setName("Second");

		datasets.getDatasets().add(d2);
        **/
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(action, datasets);
		
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;
	}

	protected ACLMessage respondToImportDataset(ACLMessage request,
			Action action) {

		ImportDataset importDataset = (ImportDataset)
				action.getAction();

		int did = importDataset.getDid();
		String dst = importDataset.getDestination();
		
		OpenmlConnector connector = getOpenmlConnector();
		
		ImportedDataset id = new ImportedDataset();
		id.setFlag(ImportedDataset.ERROR_FLAG);
		
		
		FileOutputStream fos = null;
		try {
			DataSetDescription description = connector.openmlDataGet(did);
			
			System.out.println(connector.checkCredentials()?"We're logged in":" Ohhh noooo, we're not logged in");
			String url = description.getUrl();
			
			logInfo("Starting file download from "+url);
			
			URL website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			fos = new FileOutputStream(dst);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
			id.setFlag(ImportedDataset.OK_FLAG);
			id.setPath(dst);
			
		} catch (Exception e1) {
			e1.printStackTrace();
			
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(action, id);
		
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;
	}
	
	protected ACLMessage respondToExportDataset(ACLMessage request,
			Action action) {

		ExportDataset exportDataset = (ExportDataset)
				action.getAction();
		//long datasetID = ... ;//
		
		
		//cache data
		
		
		String name = exportDataset.getName();
		String path = exportDataset.getPath();
		String type = exportDataset.getType();
		String description = exportDataset.getDescription();
		
		OpenmlConnector connector = getOpenmlConnector();
		
		DataSetDescription dataSetDescription =
				new DataSetDescription(name, description, type, "class");
		
		
		File datasetFile = new File(path);
		
		System.out.println("Absolute path :    "+datasetFile.getAbsolutePath());
		
		
		int uploadedID = -1;
		
		try {
			UploadDataSet result = connector.openmlDataUpload( dataSetDescription, datasetFile);
			uploadedID = result.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		ExportedDataset exportedDataset = new ExportedDataset();
		exportedDataset.setDid(uploadedID);
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(action, exportedDataset);
		
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;
	}

}
