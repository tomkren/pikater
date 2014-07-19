package org.pikater.core.agents.system;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAgentInfo;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPAModelStrategy;
import org.pikater.shared.database.pglargeobject.PostgreLargeObjectReader;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.shared.database.ConnectionProvider;
import org.pikater.shared.utilities.logging.PikaterLogger;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataTransferService;
import org.pikater.core.agents.system.metadata.reader.JPAMetaDataReader;
import org.pikater.core.ontology.AccountOntology;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.ResultOntology;
import org.postgresql.PGConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.pikater.core.ontology.subtrees.account.GetUser;
import org.pikater.core.ontology.subtrees.account.GetUserID;
import org.pikater.core.ontology.subtrees.account.User;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.SaveAgentInfo;
import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batch.LoadBatch;
import org.pikater.core.ontology.subtrees.batch.SaveBatch;
import org.pikater.core.ontology.subtrees.batch.SavedBatch;
import org.pikater.core.ontology.subtrees.batch.UpdateBatchStatus;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.NewModel;
import org.pikater.core.ontology.subtrees.dataset.SaveDataset;
import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.core.ontology.subtrees.experiment.SaveExperiment;
import org.pikater.core.ontology.subtrees.experiment.SavedExperiment;
import org.pikater.core.ontology.subtrees.experiment.UpdateExperimentStatus;
import org.pikater.core.ontology.subtrees.externalAgent.GetExternalAgentJar;
import org.pikater.core.ontology.subtrees.file.DeleteTempFiles;
import org.pikater.core.ontology.subtrees.file.GetFile;
import org.pikater.core.ontology.subtrees.file.GetFileInfo;
import org.pikater.core.ontology.subtrees.file.GetFiles;
import org.pikater.core.ontology.subtrees.file.ImportFile;
import org.pikater.core.ontology.subtrees.file.PrepareFileUpload;
import org.pikater.core.ontology.subtrees.file.TranslateFilename;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.GetTheBestAgent;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.SaveMetadata;
import org.pikater.core.ontology.subtrees.model.GetModel;
import org.pikater.core.ontology.subtrees.model.GetModels;
import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.core.ontology.subtrees.model.Models;
import org.pikater.core.ontology.subtrees.model.SaveModel;
import org.pikater.core.ontology.subtrees.newOption.NewOptionList;
import org.pikater.core.ontology.subtrees.result.LoadResults;
import org.pikater.core.ontology.subtrees.result.SaveResults;
import org.pikater.core.ontology.subtrees.result.SavedResult;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;

import com.google.common.io.Files;

public class Agent_DataManager extends PikaterAgent {

	private final String DEFAULT_CONNECTION_PROVIDER = "defaultConnection";
	private static final String CONNECTION_ARG_NAME = "connection";
	private String connectionBean;
	private ConnectionProvider connectionProvider;
	private static final long serialVersionUID = 1L;
	Connection db;

	public static String dataFilesPath =
			"core" + System.getProperty("file.separator") +
			"data" + System.getProperty("file.separator") +
			"files" + System.getProperty("file.separator");
	public static String datasetsPath =
			"core" + System.getProperty("file.separator") +
			"data" + System.getProperty("file.separator");
	public static String externalAgentJarsPath =
			"core" + System.getProperty("file.separator") +
			"ext_agents" + System.getProperty("file.separator");
	
	@Override
	public java.util.List<Ontology> getOntologies() {
			
		java.util.List<Ontology> ontologies =
				new java.util.ArrayList<Ontology>();
		ontologies.add(AccountOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(FilenameTranslationOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(ModelOntology.getInstance());
		
		return ontologies;
	}
	
	@Override
	protected void setup() {
		try {
			initDefault();
			registerWithDF();
			
			if (containsArgument(CONNECTION_ARG_NAME)) {
				connectionBean = getArgumentValue(CONNECTION_ARG_NAME);
			} else {
				connectionBean = DEFAULT_CONNECTION_PROVIDER;
			}
			connectionProvider = (ConnectionProvider) context.getBean(connectionBean);

			log("Connecting to " + connectionProvider.getConnectionInfo() + ".");
			openDBConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		File data = new File(dataFilesPath + "temp");
		if (!data.exists()) {
			log("Creating directory: " + Agent_DataManager.dataFilesPath);
			if (data.mkdirs()) {
				log("Succesfully created directory: " + dataFilesPath);
			} else {
				logError("Error creating directory: " + dataFilesPath);
			}
		}

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mt) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
				try {
					Action a = (Action) getContentManager().extractContent(request);

					/**
					 * Acount action
					 */
					if (a.getAction() instanceof GetUserID) {
						return respondToGetUserID(request, a);
					}
					if (a.getAction() instanceof GetUser) {
						return respondToGetUser(request, a);
					}
					
					/**
					 * LogicalNameTraslate actions
					 */
					if (a.getAction() instanceof TranslateFilename) {
						return respondToTranslateFilename(request, a);
					}

					/**
					 * AgentInfo actions
					 */
					if (a.getAction() instanceof SaveAgentInfo) {
						return respondToSaveAgentInfo(request, a);
					}
					if (a.getAction() instanceof GetAgentInfos) {
						return respondToGetAgentInfos(request, a);
					}

					/**
					 * Batch actions
					 */
					if (a.getAction() instanceof SaveBatch) {
						return respondToSaveBatch(request, a);
					}
					if (a.getAction() instanceof LoadBatch) {
						return respondToLoadBatch(request, a);
					}
					if (a.getAction() instanceof UpdateBatchStatus) {
						return respondToUpdateBatchStatus(request, a);
					}
					
					/**
					 * Experiment actions
					 */
					if (a.getAction() instanceof SaveExperiment) {
						return respondToSaveExperiment(request, a);
					}
					if (a.getAction() instanceof UpdateExperimentStatus) {
						return respondToUpdateBatchStatus(request, a);
					}
					
					/**
					 * Results actions
					 */
					if (a.getAction() instanceof SaveResults) {
						return respondToSaveResults(request, a);
					}
					if (a.getAction() instanceof LoadResults) {
						return respondToLoadResults(request, a);
					}

					/**
					 * Dataset actions
					 */
					if (a.getAction() instanceof SaveDataset) {
						return respondToSaveDatasetMessage(request, a);
					}
					
					/**
					 * Metadata actions
					 */
					if (a.getAction() instanceof SaveMetadata) {
						return respondToSaveMetadataMessage(request, a);
					}
					if (a.getAction() instanceof GetMetadata) {
						return replyToGetMetadata(request, a);
					}
					if (a.getAction() instanceof GetAllMetadata) {
						return respondToGetAllMetadata(request, a);
					}
					if (a.getAction() instanceof GetTheBestAgent) {
						return respondToGetTheBestAgent(request, a);
					}
					
					///SaVEMODEL NATRENOVANY AGENT
					///ONTOLOGIE K TOMU
					///ONTOLOGIE NA SMAZANI NA ZAKLADE NEJAKYCH PODMINKE NAPR . PO 2 MESICICH
					/// SMAZANI 
					/**
					 * Model actions
					 */
					if (a.getAction() instanceof SaveModel ) {
						return respondToSaveModel(request, a);
					}
					if (a.getAction() instanceof GetModel) {
						return respondToGetModel(request, a);
					}
					if (a.getAction() instanceof GetModels) {
						return respondToGetModels(request, a);
					}
					
					/**
					 * Files actions
					 */
					if (a.getAction() instanceof ImportFile) {
						return respondToImportFile(request, a);
					}
					if (a.getAction() instanceof GetFileInfo) {
						return respondToGetFileInfo(request, a);
					}
					if (a.getAction() instanceof GetFiles) {
						return respondToGetFiles(request, a);
					}
					if (a.getAction() instanceof DeleteTempFiles) {
						return respondToDeleteTempFiles(request);
					}
					if (a.getAction() instanceof GetFile) {
						return respondToGetFile(request, a);
					}
					if (a.getAction() instanceof GetExternalAgentJar) {
						return respondToGetExternalAgentJar(request, a);
					}
					if (a.getAction() instanceof PrepareFileUpload) {
						return respondToPrepareFileUpload(request, a);
					}

				} catch (OntologyException e) {
					e.printStackTrace();
					logError("Problem extracting content: " + e.getMessage());
				} catch (CodecException e) {
					e.printStackTrace();
					logError("Codec problem: " + e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
					logError("SQL error: " + e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logError("Failure responding to request: " + request.getContent());
				return failure;
			}

		});

	}

	private ACLMessage respondToGetUserID(ACLMessage request, Action a) {
		
		log("respondToGetUserID");
		
		GetUserID getUserID = (GetUserID)a.getAction();
		
		JPAUser userJPA = DAOs.userDAO.getByLogin(getUserID.getLogin()).get(0);
		
		if (userJPA == null) {
			ACLMessage failure = request.createReply();
			failure.setPerformative(ACLMessage.FAILURE);
			logError("UserLogin " + getUserID.getLogin() + " doesn't exist");
			return failure;
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		Result result = new Result(a, userJPA.getId());
		result.setValue(String.valueOf(userJPA.getId()));
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage());
		} catch (OntologyException e) {
			logError(e.getMessage());
		}

		return reply;
	}

	private ACLMessage respondToGetUser(ACLMessage request, Action a) {

		log("respondToGetUser");
		
		GetUser getUser = (GetUser)a.getAction();
		
		JPAUser userJPA = DAOs.userDAO.getByID(getUser.getUserID());
		
		if (userJPA == null) {
			ACLMessage failure = request.createReply();
			failure.setPerformative(ACLMessage.FAILURE);
			logError("UserID " + getUser.getUserID() + " doesn't exist");
			return failure;
		}
		
		User user = new User();
		user.setId(userJPA.getId());
		user.setLogin(userJPA.getLogin());
		user.setPriorityMax(userJPA.getPriorityMax());
		user.setEmail(userJPA.getEmail());
		user.setCreated(userJPA.getCreated());
		user.setLastLogin(userJPA.getLastLogin());
		
		ACLMessage reply = request.createReply();
		Result result = new Result(a, user);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage());
		} catch (OntologyException e) {
			logError(e.getMessage());
		}
			
		return reply;
	}
	
	private ACLMessage respondToTranslateFilename(ACLMessage request, Action a) throws CodecException, OntologyException {
		
		TranslateFilename transtateFile = (TranslateFilename) a.getAction();
						
		String translatedName = "error";
		
		if (transtateFile.getExternalFilename() != null &&
				transtateFile.getInternalFilename() == null) {

			log("respondToTranslateFilename External File Name " + transtateFile.getExternalFilename());
			
			java.util.List <JPAFilemapping> files =
					DAOs.filemappingDAO.getByExternalFilename(transtateFile.getExternalFilename());
			if(files.size() > 0) {
				translatedName = files.get(0).getInternalfilename();
			} else {
				String pathPrefix = dataFilesPath + "temp" + System.getProperty("file.separator");

				String tempFileName = pathPrefix + transtateFile.getExternalFilename();
				if (new File(tempFileName).exists())
					translatedName = "temp" + System.getProperty("file.separator") + transtateFile.getExternalFilename();
			}
			
		} else if (transtateFile.getInternalFilename() != null &&
				transtateFile.getExternalFilename() == null) {
			
			log("respondToTranslateFilename Internal File Name " + transtateFile.getInternalFilename());

			java.util.List <JPAFilemapping> files =
					DAOs.filemappingDAO.getByExternalFilename(transtateFile.getInternalFilename());
			if(files.size() > 0) {
				translatedName = files.get(0).getExternalfilename();
			} else {
				String pathPrefix = dataFilesPath + "temp" + System.getProperty("file.separator");

				String tempFileName = pathPrefix + transtateFile.getExternalFilename();
				if (new File(tempFileName).exists())
					translatedName = "temp" + System.getProperty("file.separator") + transtateFile.getExternalFilename();
			}
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		Result result = new Result(transtateFile, translatedName);
		getContentManager().fillContent(reply, result);

		return reply;
	}

	protected ACLMessage respondToGetAgentInfos(ACLMessage request, Action a) {
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		java.util.List<JPAAgentInfo> agentInfoList = DAOs.agentInfoDAO.getAll();
		
		AgentInfos agentInfos = new AgentInfos();
		for (JPAAgentInfo jpaAgentInfoI : agentInfoList) {
			
			AgentInfo agentInfoI = AgentInfo.importXML(jpaAgentInfoI.getInformationXML());		
			agentInfos.addAgentInfo(agentInfoI);
		}

		Result result = new Result(a, agentInfos);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage());
		} catch (OntologyException e) {
			logError(e.getMessage());
		}
			
		return reply;

	}

	protected ACLMessage respondToSaveAgentInfo(ACLMessage request, Action a) {
		
		log("RespondToSaveAgentInfo");

		SaveAgentInfo saveAgentInfo = (SaveAgentInfo) a.getAction();
		AgentInfo newAgentInfo = saveAgentInfo.getAgentInfo();

		ACLMessage reply = request.createReply();
		
		java.util.List<JPAAgentInfo> agentInfoList = DAOs.agentInfoDAO.getAll();
		for (JPAAgentInfo jpaAgentInfoI : agentInfoList) {
			
			AgentInfo agentInfoI = AgentInfo.importXML(jpaAgentInfoI.getInformationXML());		
			if (agentInfoI.equals(newAgentInfo)) {
				reply.setPerformative(ACLMessage.FAILURE);
				reply.setContent("AgentInfo has been  already stored in the database");
				return reply;
			}
		}

		DAOs.agentInfoDAO.storeAgentInfoOntology(newAgentInfo);
		

		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");

		return reply;
	}

	private ACLMessage respondToSaveModel(ACLMessage request, Action a) {

		SaveModel sm=(SaveModel)a.getAction();
		ACLMessage reply = request.createReply();

		JPAResult creatorResult = DAOs.resultDAO.getByID(sm.getModel().getResultID(), EmptyResultAction.LOG_NULL);
		if(creatorResult!=null){
			JPAModel model=new JPAModel();
			model.setAgentClassName(sm.getModel().getAgentClassName());
			model.setCreated(new Date());
			model.setCreatorResult(creatorResult);
			model.setSerializedAgent(sm.getModel().getSerializedAgent());
			DAOs.modelDAO.storeEntity(model);
			creatorResult.setCreatedModel(model);
			System.out.println("Saved Model ID: "+model.getId());
			DAOs.resultDAO.updateEntity(creatorResult);
			reply.setPerformative(ACLMessage.INFORM);
		}else{
			logError("No result found in the database for the given result ID in the model description");
			reply.setPerformative(ACLMessage.FAILURE);	
		}	

		return reply;
	}

	private ACLMessage respondToGetModel(ACLMessage request, Action a) {
		GetModel gm=(GetModel)a.getAction();
		
		JPAModel savedModel=DAOs.modelDAO.getByID(gm.getModelID());
		ACLMessage reply = request.createReply();
		if(savedModel==null){
			reply.setPerformative(ACLMessage.FAILURE);
		}else{
			Model retrModel=new Model();
			retrModel.setAgentClassName(savedModel.getAgentClassName());
			retrModel.setResultID(savedModel.getCreatorResult().getId());
			retrModel.setSerializedAgent(savedModel.getSerializedAgent());
			reply.setPerformative(ACLMessage.INFORM);

			Result result = new Result(a, retrModel);
			try {
				getContentManager().fillContent(reply, result);
			} catch (CodecException e) {
				logError(e.getMessage());
			} catch (OntologyException e) {
				logError(e.getMessage());
			}
		}
		return reply;
	}

	private ACLMessage respondToGetModels(ACLMessage request, Action a) {
		//GetModels gm=(GetModels)a.getAction();
		
		java.util.List<JPAModel> savedModels=DAOs.modelDAO.getAll();
		
		Models models = new Models();
		for (JPAModel modelJPA : savedModels) {
			
			Model retrModel = new Model();
			retrModel.setAgentClassName(modelJPA.getAgentClassName());
			retrModel.setResultID(modelJPA.getCreatorResult().getId());
			retrModel.setSerializedAgent(modelJPA.getSerializedAgent());
			
			models.addModel(retrModel);
		}
		
		ACLMessage reply = request.createReply();

		Result result = new Result(a, models);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;
	}

	private ACLMessage respondToSaveBatch(ACLMessage request, Action a) {
		
		log("RespondToSaveBatch");

		SaveBatch saveBatch = (SaveBatch) a.getAction();
		Batch batch = saveBatch.getBatch();
		ComputationDescription description = batch.getDescription();
		
        UniversalComputationDescription uDescription =
        		description.exportUniversalComputationDescription();

		
		JPAUser user=DAOs.userDAO.getByID(batch.getOwnerID());
		
        String batchXml = uDescription.toXML();		
        
        int totalPriority = 10*user.getPriorityMax() + batch.getPriority();


        JPABatch batchJpa = new JPABatch();
        batchJpa.setName(batch.getName());
        batchJpa.setNote(batch.getNote());
		batchJpa.setStatus(batch.getStatus());
        batchJpa.setPriority(batch.getPriority());
        batchJpa.setTotalPriority(totalPriority);
		batchJpa.setXML(batchXml);
		batchJpa.setOwner(user);
		batchJpa.setCreated(new Date());
        
        DAOs.batchDAO.storeEntity(batchJpa);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		SavedBatch savedBatch = new SavedBatch();
		savedBatch.setSavedBatchId(batchJpa.getId());
		savedBatch.setMessage("OK");
		
		Result result = new Result(a, savedBatch);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage());
		} catch (OntologyException e) {
			logError(e.getMessage());
		}

		return reply;
	}

	private ACLMessage respondToLoadBatch(ACLMessage request, Action a) {
		
		log("respondToLoadBatch");
		
		LoadBatch loadBatch = (LoadBatch) a.getAction();
		
		JPABatch batchJPA = DAOs.batchDAO.getByID(loadBatch.getBatchID());
		
        UniversalComputationDescription uDescription =
        		UniversalComputationDescription.fromXML(batchJPA.getXML());

		ComputationDescription compDescription =
				ComputationDescription.importUniversalComputationDescription(uDescription);
		
		Batch batch = new Batch();
		batch.setId(batchJPA.getId());
		batch.setName(batchJPA.getName());
		batch.setStatus(batchJPA.getStatus().toString()); //TODO:
		batch.setOwnerID(batchJPA.getOwner().getId());
		batch.setPriority(batchJPA.getPriority());
		batch.setDescription(compDescription);
		
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		Result result = new Result(a, batch);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage());
		} catch (OntologyException e) {
			logError(e.getMessage());
		}

		return reply;
	}
	
	protected ACLMessage respondToUpdateBatchStatus(ACLMessage request, Action a) {

		log("respondToUpdateBatchStatus");
		
		UpdateBatchStatus updateBatchStatus = (UpdateBatchStatus) a.getAction();
		
		JPABatch batchJPA = DAOs.batchDAO.getByID(updateBatchStatus.getBatchID());
		batchJPA.setStatus(updateBatchStatus.getStatus());
		DAOs.batchDAO.updateEntity(batchJPA);
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
	}
	
	private ACLMessage respondToSaveExperiment(ACLMessage request,
			Action a) {
		
		log("respondToSaveExperiment");
		
		SaveExperiment saveExperiment = (SaveExperiment) a.getAction();
		Experiment experiment = saveExperiment.getExperiment();
		
		int batchID = experiment.getBatchID();
		JPABatch batch=DAOs.batchDAO.getByID(batchID);
		
		JPAExperiment jpaExperiment = new JPAExperiment();
		jpaExperiment.setBatch(batch);
		jpaExperiment.setStatus(experiment.getStatus());
		
		if(experiment.getModel() instanceof NewModel){
			jpaExperiment.setModelStrategy(JPAModelStrategy.CREATION);
			jpaExperiment.setUsedModel(null);
		}
		
		DAOs.experimentDAO.storeEntity(jpaExperiment);
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		SavedExperiment savedExperiment = new SavedExperiment();
		savedExperiment.setSavedExperimentId(jpaExperiment.getId());
		savedExperiment.setMessage("OK");
		
		Result result = new Result(a, savedExperiment);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage());
		} catch (OntologyException e) {
			logError(e.getMessage());
		}
		
		return reply;
	}

	protected ACLMessage respondToUpdateExperimentStatus(ACLMessage request, Action a) {

		log("respondToUpdateExperimentStatus");
		
		UpdateExperimentStatus updateExperimentStatus = (UpdateExperimentStatus) a.getAction();
		
		JPAExperiment experimentJPA = DAOs.experimentDAO.getByID(updateExperimentStatus.getExperimentID());
		experimentJPA.setStatus(updateExperimentStatus.getStatus());
		DAOs.experimentDAO.updateEntity(experimentJPA);
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		
		return reply;
	}
	
	@SuppressWarnings("serial")
	private ACLMessage respondToPrepareFileUpload(ACLMessage request, Action a) throws CodecException, OntologyException, IOException {
		final String hash = ((PrepareFileUpload)a.getAction()).getHash();
		log("respondToPrepareFileUpload");

		final ServerSocket serverSocket = new ServerSocket();
		serverSocket.setSoTimeout(15000);
		serverSocket.bind(null);
		log("Listening on port: " + serverSocket.getLocalPort());

		addBehaviour(new ThreadedBehaviourFactory().wrap(new OneShotBehaviour() {
			@Override
			public void action() {
				try {
					DataTransferService.handleUploadConnection(serverSocket, hash);
				} catch (IOException e) {
					logError("Data upload failed");
					e.printStackTrace();
				}
			}
		}));

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent(Integer.toString(serverSocket.getLocalPort()));
		return reply;
	}

	private ACLMessage respondToGetExternalAgentJar(ACLMessage request, Action a) throws CodecException, OntologyException, ClassNotFoundException, SQLException, FailureException {
		String type = ((GetExternalAgentJar)a.getAction()).getType();
		log("getting JAR for agent type "+type);
		
		JPAExternalAgent ea = DAOs.externalAgentDAO.getByClass(type);

		if (ea == null) {
			throw new FailureException("Agent jar for type "+type+" not found in DB");
		}else{
			String jarname = type.replace(".", "_")+".jar";
			String jarpath = externalAgentJarsPath+jarname;

			File dest = new File(jarpath);
			File tmp = new File(jarpath+".tmp");
			try {
				FileUtils.writeByteArrayToFile(tmp, ea.getJar());
				Files.move(tmp, dest);
			} catch (IOException e) {
				throw new FailureException("Failed to write/move agent JAR");
			}
		}
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result r = new Result(a, "OK");
		getContentManager().fillContent(reply, r);

		return reply;
	}

	private ACLMessage respondToGetFile(ACLMessage request, Action a) throws CodecException, OntologyException, ClassNotFoundException, SQLException {
		String hash = ((GetFile)a.getAction()).getHash();
		log(new Date().toString()+" DataManager.GetFile");
		
		java.util.List<JPADataSetLO> dslos=DAOs.dataSetDAO.getByHash(hash);
		if(dslos.size()>0){
			
			try {
				JPADataSetLO dslo=dslos.get(0);
				log(new Date().toString()+" Found DSLO: "+dslo.getDescription()+" - "+dslo.getOID()+" - "+dslo.getHash());
				openDBConnection();
				PostgreLargeObjectReader reader = new PostgreLargeObjectReader((PGConnection)db,dslo.getOID());
				log(reader.toString());
				File temp = new File(dataFilesPath + "temp" + System.getProperty("file.separator") + hash);
				FileOutputStream out = new FileOutputStream(temp);
				try {
					byte[] buf = new byte[100*1024];
					int read;
					while ((read = reader.read(buf, 0, buf.length)) > 0) {
						out.write(buf, 0, read);
					}
					
					File target=new File(dataFilesPath + hash);
					log(new Date()+" Moving file to: "+target.getAbsolutePath());
					if(temp.renameTo(target)){
						log(new Date()+"File was successfully moved");
					}else{
						logError(new Date()+" Error while moving file with hash "+dslo.getHash()+" to new location "+target.getAbsolutePath());
					}
				} finally {
					reader.close();
					out.close();
					db.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			logError("DataSet file with hash "+hash+" not found.");
		}
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result r = new Result(a, "OK");
		getContentManager().fillContent(reply, r);

		return reply;
	}

	/************************************************************************************************
	 * Obsolete methods
	 * 
	 */
	private ACLMessage respondToImportFile(ACLMessage request, Action a) throws IOException, CodecException, OntologyException, SQLException, ClassNotFoundException {
		ImportFile im = (ImportFile) a.getAction();

		String pathPrefix = dataFilesPath + "temp" + System.getProperty("file.separator");

		if (im.isTempFile()) {

			FileWriter fw = new FileWriter(pathPrefix + im.getExternalFilename());
			fw.write(im.getFileContent());
			fw.close();

			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);

			Result r = new Result(im, pathPrefix + im.getExternalFilename());
			getContentManager().fillContent(reply, r);

			return reply;

		}

		if (im.getFileContent() == null) {
			String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "core" + System.getProperty("file.separator");
			path += "incoming" + System.getProperty("file.separator") + im.getExternalFilename();

			String internalFilename = md5(path);

			
			/**
			 * CREATE??? a new DataSet with empty metadata
			 */
			emptyMetadataToDB(internalFilename, im.getExternalFilename());

			File f = new File(path);

			if(DAOs.filemappingDAO.fileExists(internalFilename)){
				f.delete();
				PikaterLogger.getLogger(Agent_DataManager.class).warn("File " + internalFilename + " already present in the database");
			}else{
				JPAFilemapping fm=new JPAFilemapping();
				fm.setUser(DAOs.userDAO.getByID(im.getUserID(),EmptyResultAction.LOG_NULL));
				fm.setInternalfilename(internalFilename);
				fm.setExternalfilename(im.getExternalFilename());
				
				DAOs.filemappingDAO.storeEntity(fm);

				String newName = Agent_DataManager.dataFilesPath + internalFilename;
				move(f, new File(newName));
			}
			
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);

			Result r = new Result(im, internalFilename);
			getContentManager().fillContent(reply, r);

			return reply;
		} else {

			String fileContent = im.getFileContent();
			String fileName = im.getExternalFilename();
			String internalFilename = DigestUtils.md5Hex(fileContent);

			emptyMetadataToDB(internalFilename, fileName);


			if(DAOs.filemappingDAO.fileExists(internalFilename)){
				PikaterLogger.getLogger(Agent_DataManager.class).warn("File " + internalFilename + " already present in the database");
			}else{
				JPAFilemapping fm=new JPAFilemapping();
				fm.setUser(DAOs.userDAO.getByID(im.getUserID(),EmptyResultAction.LOG_NULL));
				fm.setInternalfilename(internalFilename);
				fm.setExternalfilename(im.getExternalFilename());
				
				DAOs.filemappingDAO.storeEntity(fm);

				String newName = Agent_DataManager.dataFilesPath + internalFilename;

				FileWriter file = new FileWriter(newName);
				file.write(fileContent);
				file.close();

				PikaterLogger.getLogger(Agent_DataManager.class).info("Created file: " + newName);
			}

			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);

			Result r = new Result(im, internalFilename);
			getContentManager().fillContent(reply, r);

			db.close();
			return reply;
		}
	}

	
	private ACLMessage respondToSaveResults(ACLMessage request, Action a) {
		
		SaveResults saveResult = (SaveResults) a.getAction();
		Task task = saveResult.getTask();
		NewOptionList options = new NewOptionList(task.getAgent().getOptions());
	
		try {
			JPAResult jparesult = new JPAResult();

			jparesult.setAgentName(task.getAgent().getName());
			jparesult.setOptions(options.exportXML());

			float Error_rate = Float.MAX_VALUE;
			float Kappa_statistic = Float.MAX_VALUE;
			float Mean_absolute_error = Float.MAX_VALUE;
			float Root_mean_squared_error = Float.MAX_VALUE;
			float Relative_absolute_error = Float.MAX_VALUE; // percent
			float Root_relative_squared_error = Float.MAX_VALUE; // percent
			
			@SuppressWarnings("unused")
			int duration = Integer.MAX_VALUE; // miliseconds
			@SuppressWarnings("unused")
			float durationLR = Float.MAX_VALUE;

			for ( Eval next_eval: task.getResult().getEvaluations() ) {

				if (next_eval.getName().equals("error_rate")) {
					Error_rate = next_eval.getValue();
				}

				if (next_eval.getName().equals("kappa_statistic")) {
					Kappa_statistic = next_eval.getValue();
				}

				if (next_eval.getName().equals("mean_absolute_error")) {
					Mean_absolute_error = next_eval.getValue();
				}

				if (next_eval.getName().equals("root_mean_squared_error")) {
					Root_mean_squared_error = next_eval.getValue();
				}

				if (next_eval.getName().equals("relative_absolute_error")) {
					Relative_absolute_error = next_eval.getValue();
				}

				if (next_eval.getName().equals("root_relative_squared_error")) {
					Root_relative_squared_error = next_eval.getValue();
				}

				if (next_eval.getName().equals("duration")) {
					duration = (int) next_eval.getValue();
				}
				if (next_eval.getName().equals("durationLR")) {
					durationLR = (float) next_eval.getValue();
				}
			}

			String start = getDateTime();
			String finish = getDateTime();
			
			if (task.getStart() != null) { start = task.getStart(); }
			if (task.getFinish() != null){ finish = task.getFinish(); }
			
			jparesult.setErrorRate(Error_rate);
			jparesult.setKappaStatistic(Kappa_statistic);
			jparesult.setMeanAbsoluteError(Mean_absolute_error);
			jparesult.setRootMeanSquaredError(Root_mean_squared_error);
			jparesult.setRelativeAbsoluteError(Relative_absolute_error);
			jparesult.setRootRelativeSquaredError(Root_relative_squared_error);
			jparesult.setStart(new Date(Timestamp.valueOf(start).getTime()));
			jparesult.setFinish(new Date(Timestamp.valueOf(finish).getTime()));

			// v novem modelu tohle neni
			// query += "\'" + duration + "\',";
			// query += "\'" + durationLR + "\',";

			// je to ono?
			jparesult.setSerializedFileName(task.getResult().getObjectFilename());
			// query += "\'" + res.getResult().getObject_filename() + "\', ";

			// jparesult.setExperiment(TODO);
			// query += "\'" + res.getId().getIdentificator() + "\',"; // TODO -
			// pozor - neni jednoznacne, pouze pro jednoho managera
			// query += "\'" + res.getProblem_name() + "\',";
			jparesult.setNote(task.getNote());
			log("JPA Result    "+jparesult.getErrorRate());
			DAOs.resultDAO.storeEntity(jparesult);
			PikaterLogger.getLogger(Agent_DataManager.class.getCanonicalName()).info("Persisted JPAResult");
		} catch(Exception e) {
			PikaterLogger.getLogger(Agent_DataManager.class.getCanonicalName()).error("Error in SaveResults", e);;
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}
	
	
	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		Date date = new Date();

		return dateFormat.format(date);
    }
	

	private ACLMessage respondToSaveMetadataMessage(ACLMessage request, Action a) throws SQLException, ClassNotFoundException {
		SaveMetadata saveMetadata = (SaveMetadata) a.getAction();
		Metadata metadata = saveMetadata.getMetadata();
		int dataSetID =saveMetadata.getDataSetID();
		
		JPADataSetLO dslo;
		try {
			dslo = DAOs.dataSetDAO.getByID(dataSetID,EmptyResultAction.THROW);
			
			
			String currentHash=dslo.getHash();
			
			java.util.List<JPADataSetLO> equalDataSets=DAOs.dataSetDAO.getByHash(currentHash);
			log("Hash of new dataset: "+currentHash);
			//we search for a dataset entry with the same hash and with already generated metadata
			JPADataSetLO dsloWithMetaData=null;
			for(JPADataSetLO candidate : equalDataSets){
				if((candidate.getAttributeMetaData()!=null)&&(candidate.getGlobalMetaData()!=null)){
					dsloWithMetaData=candidate;
					break;
				}
			}
			
			if(dsloWithMetaData==null){
				JPAMetaDataReader readr=new JPAMetaDataReader(metadata);
				dslo.setGlobalMetaData(readr.getJPAGlobalMetaData());
				dslo.setAttributeMetaData(readr.getJPAAttributeMetaData());
			}else{
				dslo.setAttributeMetaData(dsloWithMetaData.getAttributeMetaData());
				dslo.setGlobalMetaData(dsloWithMetaData.getGlobalMetaData());
			}
			DAOs.dataSetDAO.updateEntity(dslo);
			
		} catch (NoResultException e1) {
			logError("Dataset for ID "+dataSetID+" doesn't exist.");
			e1.printStackTrace();
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		return reply;
	}
	
	
	
	private ACLMessage respondToSaveDatasetMessage(ACLMessage request, Action a){
		SaveDataset sd=(SaveDataset)a.getAction();

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		
		try {
			JPAUser user=new ResultFormatter<JPAUser>(DAOs.userDAO.getByLogin(sd.getUserLogin())).getSingleResult();
			File sourceFile=new File(sd.getSourceFile());
			
			JPADataSetLO newDSLO=new JPADataSetLO();
			newDSLO.setCreated(new Date());
			newDSLO.setDescription(sd.getDescription());
			newDSLO.setOwner(user);
			//hash a OID will be set using DAO
			DAOs.dataSetDAO.storeNewDataSet(sourceFile, newDSLO);
			
			JPAFilemapping fm=new JPAFilemapping();
			fm.setExternalfilename(sourceFile.getName());
			fm.setInternalfilename(newDSLO.getHash());
			fm.setUser(user);
			DAOs.filemappingDAO.storeEntity(fm);
			reply.setContentObject((new Integer(newDSLO.getId())));
			log("Saved Dataset with ID: "+newDSLO.getId());
		} catch (NoResultException e) {
			logError("No user found with login: "+sd.getUserLogin());
			reply.setPerformative(ACLMessage.FAILURE);
			e.printStackTrace();
		} catch (IOException e) {
			logError("File can't be read.");
			reply.setPerformative(ACLMessage.FAILURE);
			e.printStackTrace();
		}

		return reply;
	}

	private ACLMessage respondToGetAllMetadata(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetAllMetadata gm = (GetAllMetadata) a.getAction();
		
		log("Agent_DataManager.respondToGetAllMetadata");

		java.util.List<JPADataSetLO> datasets = null;
		
		if (gm.getResults_required()) {
			if (gm.getExceptions() != null) {
				java.util.List<String> exHash = new java.util.LinkedList<String>();
				Iterator itr = gm.getExceptions().iterator();
				while (itr.hasNext()) {
					Metadata m = (Metadata) itr.next();
					exHash.add(m.getInternalName());
				}
				datasets = DAOs.dataSetDAO.getAllWithResultsExcludingHashes(exHash);
			}else{
				datasets = DAOs.dataSetDAO.getAllWithResults();
			}
		} else {
			if (gm.getExceptions() != null) {
				
				java.util.List<String> excludedHashes = new java.util.ArrayList<String>();
				
				Iterator itr = gm.getExceptions().iterator();
				while (itr.hasNext()) {
					Metadata m = (Metadata) itr.next();
					excludedHashes.add(m.getInternalName());
				}
				
				datasets = DAOs.dataSetDAO.getAllExcludingHashes(excludedHashes);
			}else{
				datasets = DAOs.dataSetDAO.getAll();
			}
			
		}

		List allMetadata = new ArrayList();

		for(JPADataSetLO dslo:datasets){
			
			//Getting the Global MetaData for the respond
			JPAGlobalMetaData gmd=dslo.getGlobalMetaData();
			
			java.util.List<JPAAttributeMetaData> attrMDs = dslo.getAttributeMetaData();
			for(JPAAttributeMetaData amd:attrMDs){
				Metadata aM=new Metadata();
				
				
				aM.setInternalName(dslo.getHash());
				aM.setExternalName(dslo.getDescription());
				
				aM.setDefaultTask(gmd.getDefaultTaskType().getName());
				aM.setNumberOfInstances(gmd.getNumberofInstances());
				
				aM.setMissingValues(amd.getRatioOfMissingValues()>0);
				aM.setNumberOfAttributes(attrMDs.size());
				
				if(amd instanceof JPAAttributeNumericalMetaData){
					aM.setAttributeType("Numerical");
				}else if(amd instanceof JPAAttributeCategoricalMetaData){
					aM.setAttributeType("Categorical");
				}else{
					aM.setAttributeType("Mixed");
				}
				
				allMetadata.add(aM);
			}
			
		}
		
		
		/**
		while (rs.next()) {
			Metadata m = new Metadata();
			m.setAttribute_type(rs.getString("attributeType"));
			m.setDefault_task(rs.getString("defaultTask"));
			m.setExternal_name(rs.getString("externalFilename"));
			m.setInternal_name(rs.getString("internalFilename"));
			m.setMissing_values(rs.getBoolean("missingValues"));
			m.setNumber_of_attributes(rs.getInt("numberOfAttributes"));
			m.setNumber_of_instances(rs.getInt("numberOfInstances"));

			allMetadata.add(m);
		}
		**/

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), allMetadata);
		getContentManager().fillContent(reply, _result);

		return reply;
	}

	private ACLMessage respondToGetTheBestAgent(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetTheBestAgent g = (GetTheBestAgent) a.getAction();
		String name = g.getNearest_file_name();

		openDBConnection();
		Statement stmt = db.createStatement();

		String query = "SELECT * FROM results " + "WHERE dataFile =\'" + name + "\'" + " AND errorRate = (SELECT MIN(errorRate) FROM results " + "WHERE dataFile =\'" + name + "\')";
		log("Executing query: " + query);

		ResultSet rs = stmt.executeQuery(query);
		if (!rs.isBeforeFirst()) {
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent("There are no results for this file in the database.");

			db.close();
			return reply;
		}
		rs.next();

		NewOptionList options = NewOptionList.importXML(rs.getString("options"));
		
		Agent agent = new Agent();
		agent.setName(rs.getString("agentName"));
		agent.setType(rs.getString("agentType"));
		agent.setOptions(options.getOptions());

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), agent);
		getContentManager().fillContent(reply, _result);

		db.close();
		return reply;
	}

	private ACLMessage respondToGetFileInfo(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetFileInfo gfi = (GetFileInfo) a.getAction();

		String query = "SELECT * FROM filemetadata WHERE " + gfi.toSQLCondition();

		openDBConnection();
		Statement stmt = db.createStatement();

		log("Executing query: " + query);

		ResultSet rs = stmt.executeQuery(query);

		List fileInfos = new ArrayList();

		while (rs.next()) {
			Metadata m = new Metadata();
			m.setAttributeType(rs.getString("attributeType"));
			m.setDefaultTask(rs.getString("defaultTask"));
			m.setExternalName(rs.getString("externalFilename"));
			m.setInternalName(rs.getString("internalFilename"));
			m.setMissingValues(rs.getBoolean("missingValues"));
			m.setNumberOfAttributes(rs.getInt("numberOfAttributes"));
			m.setNumberOfInstances(rs.getInt("numberOfInstances"));
			fileInfos.add(m);
		}

		Result r = new Result(a.getAction(), fileInfos);
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		getContentManager().fillContent(reply, r);

		db.close();
		return reply;
	}

	private ACLMessage respondToGetFiles(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		
		log("DataManager . GetFiles");
		GetFiles gf = (GetFiles) a.getAction();

		java.util.List<JPAFilemapping> userFiles=DAOs.filemappingDAO.getByUserID(gf.getUserID());
		
		ArrayList files = new ArrayList();

		for(JPAFilemapping fm:userFiles){
			files.add(fm.getExternalfilename());
		}

		Result r = new Result(a.getAction(), files);
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		getContentManager().fillContent(reply, r);

		return reply;
	}

	private ACLMessage respondToDeleteTempFiles(ACLMessage request) {
		String path = Agent_DataManager.dataFilesPath +
				"temp" + System.getProperty("file.separator");

		File tempDir = new File(path);
		String[] files = tempDir.list();

		if (files != null) {
			for (String file : files) {
				File d = new File(path + file);
				d.delete();
			}
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		return reply;
	}

	private ACLMessage respondToLoadResults(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		LoadResults lr = (LoadResults) a.getAction();

		String query = "SELECT * FROM resultsExternal " + lr.asSQLCondition();
		log(query);

		openDBConnection();
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		ArrayList results = new ArrayList();

		while (rs.next()) {

			SavedResult sr = new SavedResult();

			sr.setAgentType(rs.getString("agentType"));
			sr.setAgentOptions(rs.getString("options"));
			sr.setTrainFile(rs.getString("trainFileExt"));
			sr.setTestFile(rs.getString("testFileExt"));
			sr.setErrorRate(rs.getDouble("errorRate"));
			sr.setKappaStatistic(rs.getDouble("kappaStatistic"));
			sr.setMeanAbsError(rs.getDouble("meanAbsoluteError"));
			sr.setRMSE(rs.getDouble("rootMeanSquaredError"));
			sr.setRootRelativeSquaredError(rs.getDouble("rootRelativeSquaredError"));
			sr.setRelativeAbsoluteError(rs.getDouble("relativeAbsoluteError"));
			sr.setDate("nodate");

			results.add(sr);
		}

		Result r = new Result(a.getAction(), results);
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		getContentManager().fillContent(reply, r);

		db.close();
		return reply;
	}

	private void openDBConnection() throws SQLException, ClassNotFoundException {
		db = connectionProvider.getConnection();
	}

	private void emptyMetadataToDB(String internalFilename, String externalFilename) throws SQLException, ClassNotFoundException {
		openDBConnection();
		Statement stmt = db.createStatement();

		String query = "SELECT COUNT(*) AS number FROM metadata WHERE internalFilename = \'" + internalFilename + "\'";
		String query1 = "SELECT COUNT(*) AS number FROM jpafilemapping WHERE internalFilename = \'" + internalFilename + "\'";

		log("Executing query " + query);
		log("Executing query " + query1);

		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int isInMetadata = rs.getInt("number");

		ResultSet rs1 = stmt.executeQuery(query1);
		rs1.next();
		int isInFileMapping = rs1.getInt("number");

		if (isInMetadata == 0 && isInFileMapping == 1) {
			log("Executing query: " + query);
			query = "INSERT into metadata (externalFilename, internalFilename, defaultTask, " + "attributeType, numberOfInstances, numberOfAttributes, missingValues)" + "VALUES (\'"
					+ externalFilename + "\',\'" + internalFilename + "\', null, " + "null, 0, 0, false)";
			stmt.executeUpdate(query);
		}
		// stmt.close();
		db.close();
	}

	private ACLMessage replyToGetMetadata(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetMetadata gm = (GetMetadata) a.getAction();

		openDBConnection();
		Statement stmt = db.createStatement();

		String query = "SELECT * FROM metadata WHERE internalfilename = '" + gm.getInternal_filename() + "'";

		Metadata m = new Metadata();

		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			m.setAttributeType(rs.getString("attributeType"));
			m.setDefaultTask(rs.getString("defaultTask"));
			m.setExternalName(rs.getString("externalFilename"));
			m.setInternalName(rs.getString("internalFilename"));
			m.setMissingValues(rs.getBoolean("missingValues"));
			m.setNumberOfAttributes(rs.getInt("numberOfAttributes"));
			m.setNumberOfInstances(rs.getInt("numberOfInstances"));
		}

		log("Executing query: " + query);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), m);
		getContentManager().fillContent(reply, _result);

		db.close();
		return reply;
	}

	// Move file (src) to File/directory dest.
	public static synchronized void move(File src, File dest) throws FileNotFoundException, IOException {
		copy(src, dest);
		src.delete();
	}

	// Copy file (src) to File/directory dest.
	public static synchronized void copy(File src, File dest) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	
	private String md5(String path) {

		StringBuffer sb = null;

		try {
			FileInputStream fs = new FileInputStream(path);
			sb = new StringBuffer();

			int ch;
			while ((ch = fs.read()) != -1) {
				sb.append((char) ch);
			}
			fs.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logError("File not found: " + path + " -- " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logError("Error reading file: " + path + " -- " + e.getMessage());
		}

		String md5 = DigestUtils.md5Hex(sb.toString());

		log("MD5 hash of file " + path + " is " + md5);

		return md5;
	}
	/*********************************************************************
	 * End of obsolete methods
	 */
}