package org.pikater.core.agents.system;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.CopyOption;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.pikater.core.AgentNames;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.CoreConstants;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataTransferService;
import org.pikater.core.agents.system.metadata.reader.JPAMetaDataReader;
import org.pikater.core.ontology.AccountOntology;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.MailingOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.ResultOntology;
import org.pikater.core.ontology.subtrees.account.GetUser;
import org.pikater.core.ontology.subtrees.account.GetUserID;
import org.pikater.core.ontology.subtrees.account.User;
import org.pikater.core.ontology.subtrees.agent.AgentClass;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.ExternalAgentNames;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetAllAgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetExternalAgentNames;
import org.pikater.core.ontology.subtrees.agentInfo.SaveAgentInfo;
import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batch.GetBatchPriority;
import org.pikater.core.ontology.subtrees.batch.LoadBatch;
import org.pikater.core.ontology.subtrees.batch.SaveBatch;
import org.pikater.core.ontology.subtrees.batch.SavedBatch;
import org.pikater.core.ontology.subtrees.batch.UpdateBatchStatus;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.dataset.DatasetInfo;
import org.pikater.core.ontology.subtrees.dataset.DatasetsInfo;
import org.pikater.core.ontology.subtrees.dataset.GetAllDatasetInfo;
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
import org.pikater.core.ontology.subtrees.mailing.SendEmail;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.Agents;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.Metadatas;
import org.pikater.core.ontology.subtrees.metadata.SaveMetadata;
import org.pikater.core.ontology.subtrees.metadata.attributes.AttributeMetadata;
import org.pikater.core.ontology.subtrees.metadata.attributes.CategoricalAttributeMetadata;
import org.pikater.core.ontology.subtrees.metadata.attributes.NumericalAttributeMetadata;
import org.pikater.core.ontology.subtrees.model.GetModel;
import org.pikater.core.ontology.subtrees.model.GetModels;
import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.core.ontology.subtrees.model.Models;
import org.pikater.core.ontology.subtrees.model.SaveModel;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.recommend.GetMultipleBestAgents;
import org.pikater.core.ontology.subtrees.result.LoadResults;
import org.pikater.core.ontology.subtrees.result.SaveResults;
import org.pikater.core.ontology.subtrees.task.Eval;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPAAgentInfo;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.jpa.status.JPADatasetSource;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectReader;
import org.pikater.shared.database.util.ResultFormatter;
import org.pikater.shared.experiment.UniversalComputationDescription;

import com.google.common.io.Files;

public class Agent_DataManager extends PikaterAgent {

	private static final long serialVersionUID = 1L;

	@Override
	public java.util.List<Ontology> getOntologies() {

		java.util.List<Ontology> ontologies = new java.util.ArrayList<Ontology>();
		ontologies.add(AccountOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(ResultOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(FilenameTranslationOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(ModelOntology.getInstance());
		ontologies.add(RecommendOntology.getInstance());
		ontologies.add(MailingOntology.getInstance());

		return ontologies;
	}

	@Override
	protected void setup() {
		initDefault();
		registerWithDF();

		File data = new File(CoreConfiguration.DATA_FILES_PATH + "temp");
		if (!data.exists()) {
			log("Creating directory: " + CoreConfiguration.DATA_FILES_PATH);
			if (data.mkdirs()) {
				log("Succesfully created directory: " + CoreConfiguration.DATA_FILES_PATH);
			} else {
				logError("Error creating directory: " + CoreConfiguration.DATA_FILES_PATH);
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
					if (a.getAction() instanceof GetAgentInfo) {
						return respondToGetAgentInfo(request, a);
					}
					if (a.getAction() instanceof GetAgentInfos) {
						return respondToGetAgentInfos(request, a);
					}
					if (a.getAction() instanceof GetAllAgentInfos) {
						return respondToGetAllAgentInfos(request, a);
					}
					if (a.getAction() instanceof GetExternalAgentNames) {
						return respondToGetExternalAgentNames(request, a);
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
					if (a.getAction() instanceof GetBatchPriority) {
						return respondToGetBatchPriority(request, a);
					}

					/**
					 * Experiment actions
					 */
					if (a.getAction() instanceof SaveExperiment) {
						return respondToSaveExperiment(request, a);
					}
					if (a.getAction() instanceof UpdateExperimentStatus) {
						return respondToUpdateExperimentStatus(request, a);
					}

					/**
					 * Results actions
					 */
					if (a.getAction() instanceof SaveResults) {
						return respondToSaveResults(request, a);
					}
					if (a.getAction() instanceof LoadResults) {
						logError("Not Implemented");
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
					if (a.getAction() instanceof GetMultipleBestAgents) {
						return respondToGetTheBestAgent(request, a);
					}

					///SaVEMODEL NATRENOVANY AGENT
					///ONTOLOGIE K TOMU
					///ONTOLOGIE NA SMAZANI NA ZAKLADE NEJAKYCH PODMINKE NAPR . PO 2 MESICICH
					/// SMAZANI 
					/**
					 * Model actions
					 */
					if (a.getAction() instanceof SaveModel) {
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
					if (a.getAction() instanceof GetExternalAgentJar) {
						return respondToGetExternalAgentJar(request, a);
					}
					if (a.getAction() instanceof PrepareFileUpload) {
						return respondToPrepareFileUpload(request, a);
					}

					if (a.getAction() instanceof GetFile) {
						return respondToGetFile(request, a);
					}
					if (a.getAction() instanceof GetAllDatasetInfo) {
						return respondToGetAllDatasetInfo(request, a);
					}
					
					/**
					 * Deprecated Files actions
					 */
					if (a.getAction() instanceof GetFileInfo) {
						logError("Not Implemented");
					}
					if (a.getAction() instanceof ImportFile) {
						logError("Not Implemented");
					}
					if (a.getAction() instanceof GetFiles) {
						logError("Not Implemented");
					}
					if (a.getAction() instanceof DeleteTempFiles) {
						logError("Not Implemented");
					}

				} catch (OntologyException e) {
					logError("Problem extracting content: " + e.getMessage(), e);
				} catch (CodecException e) {
					logError("Codec problem: " + e.getMessage(), e);
				} catch (Exception e) {
					logError(e.getMessage(), e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logError("Failure responding to request: " + request.getConversationId());
				return failure;
			}

		});

		cleanupAbortedBatches();
	}

	private void cleanupAbortedBatches() {
		DAOs.batchDAO.cleanUp();
	}

	private ACLMessage respondToGetUserID(ACLMessage request, Action a) {

		log("respondToGetUserID");

		GetUserID getUserID = (GetUserID) a.getAction();

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
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;
	}

	private ACLMessage respondToGetUser(ACLMessage request, Action a) {

		log("respondToGetUser");

		GetUser getUser = (GetUser) a.getAction();

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
		reply.setPerformative(ACLMessage.INFORM);

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

		if (transtateFile.getExternalFilename() != null && transtateFile.getInternalFilename() == null) {

			log("respondToTranslateFilename External File Name " + transtateFile.getExternalFilename());

			java.util.List<JPAFilemapping> files = DAOs.filemappingDAO.getByExternalFilename(transtateFile.getExternalFilename());
			if (files.size() > 0) {
				translatedName = files.get(0).getInternalfilename();
			} else {
				String pathPrefix = CoreConfiguration.DATA_FILES_PATH + "temp" + System.getProperty("file.separator");

				String tempFileName = pathPrefix + transtateFile.getExternalFilename();
				if (new File(tempFileName).exists())
					translatedName = "temp" + System.getProperty("file.separator") + transtateFile.getExternalFilename();
			}

		} else if (transtateFile.getInternalFilename() != null && transtateFile.getExternalFilename() == null) {

			log("respondToTranslateFilename Internal File Name " + transtateFile.getInternalFilename());

			java.util.List<JPAFilemapping> files = DAOs.filemappingDAO.getByExternalFilename(transtateFile.getInternalFilename());
			if (files.size() > 0) {
				translatedName = files.get(0).getExternalfilename();
			} else {
				String pathPrefix = CoreConfiguration.DATA_FILES_PATH + "temp" + System.getProperty("file.separator");

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

	protected ACLMessage respondToGetAgentInfo(ACLMessage request, Action a) {

		GetAgentInfo getAgentInfo = (GetAgentInfo) a.getAction();
		String agentClass = getAgentInfo.getAgentClassName();

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		List<JPAAgentInfo> agentInfoList = DAOs.agentInfoDAO.getByAgentClass(agentClass);
		JPAAgentInfo agentInfoJPA = agentInfoList.get(0);

		AgentInfo agentInfo = AgentInfo.importXML(agentInfoJPA.getInformationXML());

		Result result = new Result(a, agentInfo);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;

	}

	protected ACLMessage respondToGetAgentInfos(ACLMessage request, Action a) {

		GetAgentInfos getAgentInfos = (GetAgentInfos) a.getAction();
		int userID = getAgentInfos.getUserID();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		JPAUser user = DAOs.userDAO.getByID(userID);
		
		List<JPAAgentInfo> agentInfoList = DAOs.agentInfoDAO.getByExternalAgentOwner(user);

		AgentInfos agentInfos = new AgentInfos();
		for (JPAAgentInfo jpaAgentInfoI : agentInfoList) {
			
			if((jpaAgentInfoI.getExternalAgent() == null) // global agent... always approved
					|| jpaAgentInfoI.getExternalAgent().isApproved()) // external agent for a user... check if approved
			{
				AgentInfo agentInfoI = AgentInfo.importXML(jpaAgentInfoI.getInformationXML());
				agentInfos.addAgentInfo(agentInfoI);
			}
		}

		Result result = new Result(a, agentInfos);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;

	}

	protected ACLMessage respondToGetAllAgentInfos(ACLMessage request, Action a) {
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		List<JPAAgentInfo> agentInfoList = DAOs.agentInfoDAO.getAll();

		AgentInfos agentInfos = new AgentInfos();
		for (JPAAgentInfo jpaAgentInfoI : agentInfoList) {

			AgentInfo agentInfoI = AgentInfo.importXML(jpaAgentInfoI.getInformationXML());
			agentInfos.addAgentInfo(agentInfoI);
		}

		Result result = new Result(a, agentInfos);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;
	}
	
	protected ACLMessage respondToGetExternalAgentNames(ACLMessage request, Action a) throws CodecException, OntologyException {

		log("getting external agent names");

		List<JPAExternalAgent> externalAgents = DAOs.externalAgentDAO.getAll();

		List<AgentClass> agentNames = new ArrayList<AgentClass>();
		for (JPAExternalAgent JPAAgentI : externalAgents) {
			AgentClass agentClass = new AgentClass(
					JPAAgentI.getAgentClass());
			
			agentNames.add(agentClass);
		}
		ExternalAgentNames externalAgentNames = new ExternalAgentNames(agentNames);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result result = new Result(a, externalAgentNames);
		getContentManager().fillContent(reply, result);

		return reply;
	}

	protected ACLMessage respondToSaveAgentInfo(ACLMessage request, Action a) {

		log("RespondToSaveAgentInfo");

		SaveAgentInfo saveAgentInfo = (SaveAgentInfo) a.getAction();
		AgentInfo newAgentInfo = saveAgentInfo.getAgentInfo();

		String agentClassName = newAgentInfo.getAgentClassName();
		JPAExternalAgent externalAgent = DAOs.externalAgentDAO.getByAgentClass(agentClassName);
		
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
		
		DAOs.agentInfoDAO.storeAgentInfoOntology(newAgentInfo, externalAgent);

		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");

		return reply;
	}

	private ACLMessage respondToSaveBatch(ACLMessage request, Action a) {

		log("RespondToSaveBatch");

		SaveBatch saveBatch = (SaveBatch) a.getAction();
		Batch batch = saveBatch.getBatch();
		ComputationDescription description = batch.getDescription();

		UniversalComputationDescription uDescription = description.exportUniversalComputationDescription();

		JPAUser user = DAOs.userDAO.getByID(batch.getOwnerID());

		String batchXml = uDescription.toXML();

		int totalPriority = 10 * user.getPriorityMax() + batch.getPriority();

		JPABatch batchJpa = new JPABatch();
		batchJpa.setName(batch.getName());
		batchJpa.setNote(batch.getNote());
		batchJpa.setStatus(batch.getStatus());
		batchJpa.setUserAssignedPriority(batch.getPriority());
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
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;
	}

	private ACLMessage respondToLoadBatch(ACLMessage request, Action a) {

		log("respondToLoadBatch");

		LoadBatch loadBatch = (LoadBatch) a.getAction();

		JPABatch batchJPA = DAOs.batchDAO.getByID(loadBatch.getBatchID());

		UniversalComputationDescription uDescription = UniversalComputationDescription.fromXML(batchJPA.getXML());

		ComputationDescription compDescription = ComputationDescription.importUniversalComputationDescription(uDescription);

		Batch batch = new Batch();
		batch.setId(batchJPA.getId());
		batch.setName(batchJPA.getName());
		batch.setStatus(batchJPA.getStatus().name());
		batch.setOwnerID(batchJPA.getOwner().getId());
		batch.setPriority(batchJPA.getUserAssignedPriority());
		batch.setDescription(compDescription);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(a, batch);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;
	}

	private void requestMailNotification(final JPABatch batchJPA) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = -6987340128342367505L;

			@Override
			public void action() {
				String mailAddr = batchJPA.getOwner().getEmail();
				SendEmail action = new SendEmail(Agent_Mailing.EmailType.RESULT, mailAddr);
				action.setBatch_id(batchJPA.getId());
				List<JPAExperiment> exps = batchJPA.getExperiments();
				// when there was more than 1 sub-experiment, don't send the best result 
				if (exps.size() == 1 && exps.get(0).getResults().size() > 0) {
					double bestErrorRate = 200;
					for (JPAResult r : exps.get(0).getResults()) {
						if (r.getErrorRate() < bestErrorRate) {
							bestErrorRate = r.getErrorRate();
						}
					}
					action.setResult(bestErrorRate);
				}
				AID receiver = new AID(AgentNames.MAILING, false);
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				Ontology ontology = MailingOntology.getInstance();

				request.addReceiver(receiver);
				request.setLanguage(getCodec().getName());
				request.setOntology(ontology.getName());
				try {
					getContentManager().fillContent(request, new Action(receiver, action));
					log("Sending notification request to mailAgent");
					ACLMessage reply = FIPAService.doFipaRequestClient(this.myAgent, request, 10000);
					if (reply == null)
						logError("Reply not received.");
				} catch (CodecException | OntologyException | FIPAException e) {
					logError("Failed to request e-mail", e);
				}
			}
		});
	}

	protected ACLMessage respondToUpdateBatchStatus(ACLMessage request, Action a) {

		log("respondToUpdateBatchStatus");

		UpdateBatchStatus updateBatchStatus = (UpdateBatchStatus) a.getAction();
		
		log("****** " + updateBatchStatus.getBatchID());


		JPABatch batchJPA = DAOs.batchDAO.getByID(updateBatchStatus.getBatchID());
		JPABatchStatus batchStatus = JPABatchStatus.valueOf(updateBatchStatus.getStatus());
		switch (batchStatus) {
		case COMPUTING:
			batchJPA.setStarted(new Date());
			break;
		case FAILED:
		case FINISHED:
			batchJPA.setFinished(new Date());
			break;
		default:
			break;
		}
		batchJPA.setStatus(batchStatus);
		DAOs.batchDAO.updateEntity(batchJPA);

		if (batchStatus == JPABatchStatus.FINISHED && batchJPA.isSendEmailAfterFinish()) {
			requestMailNotification(batchJPA);
		} else {
			log("not sending mail notification - option not set");
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");

		return reply;
	}

	private ACLMessage respondToGetBatchPriority(ACLMessage request, Action a) {
		log("respondToGetBatchPriority");

		GetBatchPriority getBatchPriority = (GetBatchPriority) a.getAction();
		int batchID = getBatchPriority.getBatchID();

		JPABatch batchJPA = DAOs.batchDAO.getByID(batchID);

		ACLMessage reply = request.createReply();

		if (batchJPA == null) {
			reply.setPerformative(ACLMessage.FAILURE);
		} else {
			reply.setPerformative(ACLMessage.INFORM);

			Result result = new Result(a, batchJPA.getTotalPriority());
			try {
				getContentManager().fillContent(reply, result);
			} catch (CodecException e) {
				logError(e.getMessage(), e);
			} catch (OntologyException e) {
				logError(e.getMessage(), e);
			}
		}

		return reply;

	}

	private ACLMessage respondToSaveExperiment(ACLMessage request, Action a) {

		log("respondToSaveExperiment");

		SaveExperiment saveExperiment = (SaveExperiment) a.getAction();
		Experiment experiment = saveExperiment.getExperiment();

		/**TODO: Parser sends SaveExperiment message, when experiment is
		* created and computation has begun
		* DAO now sets current date for created and started, but maybe the created can be ommited...
		* */
		int savedID = DAOs.batchDAO.addExperimentToBatch(experiment);

		ACLMessage reply = request.createReply();

		if (savedID == -1) {
			reply.setPerformative(ACLMessage.FAILURE);
		} else {
			reply.setPerformative(ACLMessage.INFORM);

			SavedExperiment savedExperiment = new SavedExperiment();
			savedExperiment.setSavedExperimentId(savedID);
			savedExperiment.setMessage("OK");

			Result result = new Result(a, savedExperiment);
			try {
				getContentManager().fillContent(reply, result);
			} catch (CodecException e) {
				logError(e.getMessage(), e);
			} catch (OntologyException e) {
				logError(e.getMessage(), e);
			}
		}

		return reply;
	}

	protected ACLMessage respondToUpdateExperimentStatus(ACLMessage request, Action a) {

		log("respondToUpdateExperimentStatus");

		UpdateExperimentStatus updateExperimentStatus = (UpdateExperimentStatus) a.getAction();

		JPAExperiment experimentJPA = DAOs.experimentDAO.getByID(updateExperimentStatus.getExperimentID());
		JPAExperimentStatus experimentStatus = JPAExperimentStatus.valueOf(updateExperimentStatus.getStatus());
		experimentJPA.setStatus(experimentStatus);
		switch (experimentStatus) {
		case COMPUTING:
			experimentJPA.setStarted(new Date());
			break;
		case FAILED:
		case FINISHED:
			experimentJPA.setFinished(new Date());
			break;
		default:
			break;
		}

		DAOs.experimentDAO.updateEntity(experimentJPA);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");

		return reply;
	}

	private <T extends JPAAbstractEntity> boolean containsID(List<T> list, T item ){
		for(T i : list){
			if(i.getId()==item.getId()){
				return true;
			}
		}
		return false;
	}
	
	private <E> List<E> safe(List<E> list){
		return (list!=null)?list:Collections.<E>emptyList();
	}
	
	private ACLMessage respondToSaveResults(ACLMessage request, Action a) {
		SaveResults saveResult = (SaveResults) a.getAction();
		Task task = saveResult.getTask();
		NewOptions options = new NewOptions(task.getAgent().getOptions());

		int experimentID = saveResult.getExperimentID();

		JPAResult jparesult = new JPAResult();
		jparesult.setAgentName(task.getAgent().getType());
		log("Adding result for Agent Type: " + task.getAgent().getType());
		jparesult.setOptions(options.exportXML());
		log("Saving result for hash: " + task.getDatas().exportInternalTrainFileName());
		jparesult.setSerializedFileName(task.getDatas().exportInternalTrainFileName());

		for(Data data : safe(task.getDatas().getDatas())){
			if(data==null)
				continue;
			
			JPADataSetLO dslo=new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByHash(data.getInternalFileName())).getSingleResultWithNull();
			if(dslo!=null){
				if(!this.containsID(jparesult.getInputs(), dslo)){
					jparesult.getInputs().add(dslo);
					log("Adding input " + data.getInternalFileName() + " to result." );
				}else{
					log("Adding input " + data.getInternalFileName() + " skipped. Duplicate value.");
				}
			}else{
				logError("Failed to add output " + data.getInternalFileName() + " to result for train dataset " + task.getDatas().exportInternalTrainFileName());
			}
		}
		
		for (TaskOutput output : task.getOutput()) {
			JPADataSetLO dslo=new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByHash(output.getName())).getSingleResultWithNull();
			if(dslo!=null){
				if(!this.containsID(jparesult.getOutputs(), dslo)){
					jparesult.getOutputs().add(dslo);
					log("Adding output " + output.getName() + " to result for train dataset " + task.getDatas().exportInternalTrainFileName());
				}else{
					log("Adding output " + output.getName() + " skipped. Duplicate value.");
				}
			}else{
				logError("Failed to add output " + output.getName() + " to result for train dataset " + task.getDatas().exportInternalTrainFileName());
			}
		}

		float errorRate = Float.MAX_VALUE;
		float kappa_statistic = Float.MAX_VALUE;
		float mean_absolute_error = Float.MAX_VALUE;
		float root_mean_squared_error = Float.MAX_VALUE;
		float relative_absolute_error = Float.MAX_VALUE; // percent
		float root_relative_squared_error = Float.MAX_VALUE; // percent
		int duration = Integer.MAX_VALUE; // miliseconds
		float durationLR = Float.MAX_VALUE;

		Evaluation evaluation = task.getResult();

		Eval errorRateEval = evaluation.exportEvalByName(CoreConstants.ERROR_RATE);
		if (errorRateEval != null) {
			errorRate = errorRateEval.getValue();
		}

		Eval kappaStatisticEval = evaluation.exportEvalByName(CoreConstants.KAPPA_STATISTIC);
		if (kappaStatisticEval != null) {
			kappa_statistic = kappaStatisticEval.getValue();
		}

		Eval meanAbsoluteEval = evaluation.exportEvalByName(CoreConstants.MEAN_ABSOLUTE_ERROR);
		if (meanAbsoluteEval != null) {
			mean_absolute_error = meanAbsoluteEval.getValue();
		}

		Eval rootMeanSquaredEval = evaluation.exportEvalByName(CoreConstants.ROOT_MEAN_SQUARED_ERROR);
		if (rootMeanSquaredEval != null) {
			root_mean_squared_error = rootMeanSquaredEval.getValue();
		}

		Eval relativeAbsoluteEval = evaluation.exportEvalByName(CoreConstants.RELATIVE_ABSOLUTE_ERROR);
		if (relativeAbsoluteEval != null) {
			relative_absolute_error = relativeAbsoluteEval.getValue();
		}

		Eval rootRelativeSquaredEval = evaluation.exportEvalByName(CoreConstants.ROOT_RELATIVE_SQUARED_ERROR);
		if (rootRelativeSquaredEval != null) {
			root_relative_squared_error = rootRelativeSquaredEval.getValue();
		}

		Eval durationEval = evaluation.exportEvalByName(CoreConstants.DURATION);
		if (durationEval != null) {
			duration = (int) durationEval.getValue();
		}

		Eval durationLREval = evaluation.exportEvalByName(CoreConstants.DURATIONLR);
		if (durationLREval != null) {
			durationLR = (float) durationLREval.getValue();
		}

		jparesult.setErrorRate(errorRate);
		jparesult.setKappaStatistic(kappa_statistic);
		jparesult.setMeanAbsoluteError(mean_absolute_error);
		jparesult.setRootMeanSquaredError(root_mean_squared_error);
		jparesult.setRelativeAbsoluteError(relative_absolute_error);
		jparesult.setRootRelativeSquaredError(root_relative_squared_error);
		jparesult.setDuration(duration);
		jparesult.setDurationLR(durationLR);

		Date start = new Date();
		if (task.getStart() != null) {
			start = Agent_DataManager.getDateFromPikaterDateString(task.getStart());
		} else {
			logError("Result start date isn't set. Using current DateTime");
		}
		jparesult.setStart(start);
		log("Start Date set: " + start.toString());

		Date finish = new Date();
		if (task.getFinish() != null) {
			finish = Agent_DataManager.getDateFromPikaterDateString(task.getFinish());
		} else {
			logError("Result finish date isn't set. Using current DateTime");
		}
		jparesult.setFinish(finish);
		log("Finish Date set: " + finish.toString());

		jparesult.setNote(task.getNote());
		log("JPA Result    " + jparesult.getErrorRate());
		int resultID = DAOs.experimentDAO.addResultToExperiment(experimentID, jparesult);
		if (resultID != -1) {
			log("Persisted JPAResult for experiment ID " + experimentID + " with ID: " + resultID);
			if (task.getResult().getObject() != null) {
				saveResultModel(task, resultID);
			}
		} else {
			logError("Couldn't persist JPAResult for experiment ID \n" + experimentID);
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}

	private ACLMessage respondToSaveDatasetMessage(ACLMessage request, Action a) {
		SaveDataset sd = (SaveDataset) a.getAction();

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		try {
			File f = new File(sd.getSourceFile());
			int jpadsloID = DAOs.dataSetDAO.storeNewDataSet(f, sd.getDescription(), sd.getUserID(),JPADatasetSource.EXPERIMENT);

			reply.setContentObject((new Integer(jpadsloID)));
			log("Saved Dataset with ID: " + jpadsloID + " for sourcefile "+sd.getSourceFile());
		} catch (NoResultException e) {
			logError("No user found with login: " + sd.getUserID(), e);
			reply.setPerformative(ACLMessage.FAILURE);
		} catch (IOException e) {
			logError("File can't be read.", e);
			reply.setPerformative(ACLMessage.FAILURE);
		}

		return reply;
	}

	private void saveResultModel(Task task, int resultId) {
		Model m = new Model();
		m.setAgentClassName(task.getAgent().getType());
		m.setResultID(resultId);
		m.setSerializedAgent(task.getResult().getObject());
		int savedModelID = DAOs.resultDAO.setModelForResult(m);
		if (savedModelID == -1) {
			logError("Failed to persist model for result with ID " + resultId);
		} else {
			log("Persisted JPAModel " + savedModelID);
		}
	}

	private ACLMessage respondToSaveMetadataMessage(ACLMessage request, Action a) {
		SaveMetadata saveMetadata = (SaveMetadata) a.getAction();
		Metadata metadata = saveMetadata.getMetadata();
		int dataSetID = saveMetadata.getDataSetID();

		JPADataSetLO dslo;
		try {
			dslo = DAOs.dataSetDAO.getByID(dataSetID, EmptyResultAction.THROW);

			String currentHash = dslo.getHash();

			List<JPADataSetLO> equalDataSets = DAOs.dataSetDAO.getByHash(currentHash);
			log("Hash of new dataset: " + currentHash);
			//we search for a dataset entry with the same hash and with already generated metadata
			JPADataSetLO dsloWithMetaData = null;
			for (JPADataSetLO candidate : equalDataSets) {
				if ((candidate.getAttributeMetaData() != null) && (candidate.getGlobalMetaData() != null)) {
					dsloWithMetaData = candidate;
					break;
				}
			}

			if (dsloWithMetaData == null) {
				JPAMetaDataReader readr = new JPAMetaDataReader(metadata);
				dslo.setGlobalMetaData(readr.getJPAGlobalMetaData());
				dslo.setAttributeMetaData(readr.getJPAAttributeMetaData());
			} else {
				dslo.setAttributeMetaData(dsloWithMetaData.getAttributeMetaData());
				dslo.setGlobalMetaData(dsloWithMetaData.getGlobalMetaData());
			}
			DAOs.dataSetDAO.updateEntity(dslo);

		} catch (NoResultException e1) {
			logError("Dataset for ID " + dataSetID + " doesn't exist.", e1);
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		return reply;
	}

	private ACLMessage replyToGetMetadata(ACLMessage request, Action a) throws CodecException, OntologyException {
		GetMetadata gm = (GetMetadata) a.getAction();

		Metadata m = new Metadata();
		ACLMessage reply = request.createReply();
		log("Retrieving metadata for hash " + gm.getInternal_filename());
		JPADataSetLO dslo = new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByHash(gm.getInternal_filename())).getSingleResultWithNull();

		if ((dslo != null)
			&&(dslo.getGlobalMetaData()!=null)
			&&(dslo.getAttributeMetaData()!=null)
			){
			m = this.convertJPADatasetToOntologyMetadata(dslo);
			reply.setPerformative(ACLMessage.INFORM);
		} else {
			reply.setPerformative(ACLMessage.FAILURE);
		}

		Result result = new Result(a.getAction(), m);
		getContentManager().fillContent(reply, result);

		return reply;
	}

	private AttributeMetadata convertJPAAttributeMetadataToOntologyMetadata(JPAAttributeMetaData amd) {
		AttributeMetadata attributeMetadata;

		if (amd instanceof JPAAttributeNumericalMetaData) {

			JPAAttributeNumericalMetaData jpaAttrnum = (JPAAttributeNumericalMetaData) amd;

			attributeMetadata = new NumericalAttributeMetadata();

			((NumericalAttributeMetadata) attributeMetadata).setAvg(jpaAttrnum.getAvarage());
			((NumericalAttributeMetadata) attributeMetadata).setMax(jpaAttrnum.getMax());
			((NumericalAttributeMetadata) attributeMetadata).setMin(jpaAttrnum.getMin());
			((NumericalAttributeMetadata) attributeMetadata).setMedian(jpaAttrnum.getMedian());
			((NumericalAttributeMetadata) attributeMetadata).setStandardDeviation(jpaAttrnum.getVariance());

		} else if (amd instanceof JPAAttributeCategoricalMetaData) {
			JPAAttributeCategoricalMetaData jpaAttrCat = (JPAAttributeCategoricalMetaData) amd;

			attributeMetadata = new CategoricalAttributeMetadata();

			((CategoricalAttributeMetadata) attributeMetadata).setNumberOfCategories(jpaAttrCat.getNumberOfCategories());

		} else {
			attributeMetadata = new AttributeMetadata();
		}

		attributeMetadata.setRatioOfMissingValues(amd.getRatioOfMissingValues());
		attributeMetadata.setIsTarget(amd.isTarget());
		attributeMetadata.setName(amd.getName());
		attributeMetadata.setAttributeClassEntropy(amd.getClassEntropy());
		attributeMetadata.setEntropy(amd.getEntropy());
		attributeMetadata.setOrder(amd.getOrder());

		return attributeMetadata;
	}

	private Metadata convertJPADatasetToOntologyMetadata(JPADataSetLO dslo) {
		JPAGlobalMetaData gmd = dslo.getGlobalMetaData();

		Metadata globalMetaData = new Metadata();

		JPAFilemapping fm = new ResultFormatter<JPAFilemapping>(DAOs.filemappingDAO.getByInternalFilename(dslo.getHash())).getSingleResultWithNull();

		if (fm != null) {
			globalMetaData.setExternalName(fm.getExternalfilename());
			globalMetaData.setInternalName(fm.getInternalfilename());
		} else {
			globalMetaData.setInternalName(dslo.getHash());
			globalMetaData.setExternalName(dslo.getDescription());
		}

		globalMetaData.setDefaultTask(gmd.getDefaultTaskType().getName());
		globalMetaData.setNumberOfInstances(gmd.getNumberofInstances());
		globalMetaData.setNumberOfAttributes(dslo.getNumberOfAttributes());

		globalMetaData.setAttributeType(gmd.getAttributeType());
		globalMetaData.setLinearRegressionDuration(gmd.getLinearRegressionDuration());

		List<JPAAttributeMetaData> attrMDs = dslo.getAttributeMetaData();

		boolean missingValues = false;

		for (JPAAttributeMetaData amd : attrMDs) {
			AttributeMetadata attributeMetadata = this.convertJPAAttributeMetadataToOntologyMetadata(amd);

			missingValues = missingValues || (amd.getRatioOfMissingValues() > 0);

			globalMetaData.getAttributeMetadataList().add(attributeMetadata);
		}

		globalMetaData.setMissingValues(missingValues);

		return globalMetaData;

	}

	private ACLMessage respondToGetAllMetadata(ACLMessage request, Action a) throws CodecException, OntologyException {
		GetAllMetadata gm = (GetAllMetadata) a.getAction();

		log("Agent_DataManager.respondToGetAllMetadata");

		List<String> exHash = new java.util.LinkedList<String>();
		for (Metadata metadataI : gm.getExceptions()) {
			exHash.add(metadataI.getInternalName());
		}
		
		List<JPADataSetLO> datasets = null;
		
		if (gm.getResultsRequired()) {
			log("DataManager.Results Required");
			datasets = DAOs.dataSetDAO.getAllWithResultsExcludingHashesWithMetadata(exHash);
		} else {
			log("DataManager.Results NOT Required");
			datasets = DAOs.dataSetDAO.getAllExcludingHashesWithMetadata(exHash);
		}

		if (datasets == null) {
			datasets = new ArrayList<JPADataSetLO>();
		}
		
		Metadatas metadatas = new Metadatas();

		for (JPADataSetLO dslo : datasets) {
			Metadata globalMetaData = this.convertJPADatasetToOntologyMetadata(dslo);
			metadatas.addMetadata(globalMetaData);
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(a.getAction(), metadatas);
		getContentManager().fillContent(reply, result);

		return reply;
	}

	private ACLMessage respondToGetTheBestAgent(ACLMessage request, Action a) throws ClassNotFoundException, CodecException, OntologyException {
		GetMultipleBestAgents g = (GetMultipleBestAgents) a.getAction();
		String datasethash = g.getNearestInternalFileName();
		int count = g.getNumberOfAgents();

		log("DataManager.GetTheBestAgent for datafile " + datasethash);

		List<JPAResult> results = DAOs.resultDAO.getResultsByDataSetHashAscendingUponErrorRate(datasethash, count);

		ACLMessage reply = request.createReply();

		Agents foundAgents = new Agents();
		if (results.size() > 0) {

			for (JPAResult result : results) {
				NewOptions options = new NewOptions();
				try{
					options = NewOptions.importXML(result.getOptions());
				}catch(Exception e){
					logError("Incompatible options in result, using default options");
				}
				Agent agent = new Agent();
				agent.setName(result.getAgentName());
				agent.setType(result.getAgentName());
				agent.setOptions(options.getOptions());
				foundAgents.add(agent);
			}
		}

		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), foundAgents);
		getContentManager().fillContent(reply, _result);

		return reply;
	}

	private ACLMessage respondToSaveModel(ACLMessage request, Action a) {

		SaveModel sm = (SaveModel) a.getAction();
		ACLMessage reply = request.createReply();

		int savedModelID = DAOs.resultDAO.setModelForResult(sm.getModel());

		if (savedModelID != -1) {
			System.out.println("Saved Model ID: " + savedModelID);
			reply.setPerformative(ACLMessage.INFORM);
		} else {
			logError("Couldn't be saved model for experiment ID " + sm.getModel().getResultID());
			reply.setPerformative(ACLMessage.FAILURE);
		}

		return reply;
	}

	private ACLMessage respondToGetModel(ACLMessage request, Action a) {
		GetModel gm = (GetModel) a.getAction();

		JPAModel savedModel = DAOs.modelDAO.getByID(gm.getModelID());
		ACLMessage reply = request.createReply();
		if (savedModel == null) {
			reply.setPerformative(ACLMessage.FAILURE);
		} else {
			Model retrModel = new Model();
			retrModel.setAgentClassName(savedModel.getAgentClassName());
			retrModel.setResultID(savedModel.getCreatorResult().getId());
			retrModel.setSerializedAgent(savedModel.getSerializedAgent());
			reply.setPerformative(ACLMessage.INFORM);

			Result result = new Result(a, retrModel);
			try {
				getContentManager().fillContent(reply, result);
			} catch (CodecException e) {
				logError(e.getMessage(), e);
			} catch (OntologyException e) {
				logError(e.getMessage(), e);
			}
		}
		return reply;
	}

	private ACLMessage respondToGetModels(ACLMessage request, Action a) {
		//GetModels gm=(GetModels)a.getAction();

		List<JPAModel> savedModels = DAOs.modelDAO.getAll();

		Models models = new Models();
		for (JPAModel modelJPA : savedModels) {

			Model retrModel = new Model();
			retrModel.setAgentClassName(modelJPA.getAgentClassName());
			retrModel.setResultID(modelJPA.getCreatorResult().getId());
			retrModel.setSerializedAgent(modelJPA.getSerializedAgent());

			models.addModel(retrModel);
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

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

	private ACLMessage respondToGetExternalAgentJar(ACLMessage request, Action a) throws FailureException, CodecException, OntologyException {
		String type = ((GetExternalAgentJar) a.getAction()).getType();
		log("getting JAR for agent type " + type);

		JPAExternalAgent ea = DAOs.externalAgentDAO.getByAgentClass(type);

		if (ea == null) {
			throw new FailureException("Agent jar for type " + type + " not found in DB");
		} else {
			String jarname = type.replace(".", "_") + ".jar";
			String jarpath = CoreConfiguration.EXTERNAL_AGENT_JARS_PATH + jarname;

			File dest = new File(jarpath);
			File tmp = new File(jarpath + ".tmp");
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

	@SuppressWarnings("serial")
	private ACLMessage respondToPrepareFileUpload(ACLMessage request, Action a) throws CodecException, OntologyException, IOException {
		final String hash = ((PrepareFileUpload) a.getAction()).getHash();
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
					logError("Data upload failed", e);
				}
			}
		}));

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent(Integer.toString(serverSocket.getLocalPort()));
		return reply;
	}

	private ACLMessage respondToGetFile(ACLMessage request, Action a) throws CodecException, OntologyException, ClassNotFoundException {
		String hash = ((GetFile) a.getAction()).getHash();
		log(new Date().toString() + " DataManager.GetFile");

		List<JPADataSetLO> dslos = DAOs.dataSetDAO.getByHash(hash);
		if (dslos.size() > 0) {

			try {
				JPADataSetLO dslo = dslos.get(0);
				log(new Date().toString() + " Found DSLO: " + dslo.getDescription() + " - " + dslo.getOID() + " - " + dslo.getHash());

				PGLargeObjectReader reader = PGLargeObjectReader.getForLargeObject(dslo.getOID());
				log(reader.toString());
				File temp = new File(CoreConfiguration.DATA_FILES_PATH + "temp" + System.getProperty("file.separator") + hash);
				File target = new File(CoreConfiguration.DATA_FILES_PATH + hash);

				FileOutputStream out = new FileOutputStream(temp);
				try {
					byte[] buf = new byte[100 * 1024];
					int read;
					while ((read = reader.read(buf, 0, buf.length)) > 0) {
						out.write(buf, 0, read);
					}

					log(new Date() + " Moving file to: " + target.getAbsolutePath());
					try {
						java.nio.file.Files.move(temp.toPath(), target.toPath(), (CopyOption) StandardCopyOption.REPLACE_EXISTING);
						log(new Date() + "File was successfully moved");
					} catch (IOException ioe) {
						logError(new Date() + "Error while moving file with hash " + dslo.getHash() + " to new location " + target.getAbsolutePath());
					}
				} catch (IOException ioe) {
					logError("Error while downloading file with hash " + hash + " from database", ioe);
				} finally {
					reader.close();
					out.close();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logError("DataSet file with hash " + hash + " not found.");
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result r = new Result(a, "OK");
		getContentManager().fillContent(reply, r);

		return reply;
	}

	public ACLMessage respondToGetAllDatasetInfo(ACLMessage request, Action a) {
		
		List<JPADataSetLO> dslos = DAOs.dataSetDAO.getAllUserUploaded();
		DatasetsInfo datasetsOnto = new DatasetsInfo();
		
		for (JPADataSetLO datasetI : dslos) {
		
			DatasetInfo datasetOntoI = new DatasetInfo();
			datasetOntoI.setDatasetID(datasetI.getId());
			datasetOntoI.setHash(datasetI.getHash());
			datasetOntoI.setFileName(datasetI.getFileName());
			
			datasetsOnto.addDatasets(datasetOntoI);
		}
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(a, datasetsOnto);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;
	}
	
	public static String getPikaterDateString(Date date) {
		return "" + date.getTime();
	}

	public static String getCurrentPikaterDateString() {
		return Agent_DataManager.getPikaterDateString(new Date());
	}

	public static Date getDateFromPikaterDateString(String dateString) {
		try {
			long millis = Long.parseLong(dateString);
			return new Date(millis);
		} catch (NumberFormatException e) {
			return new Date();
		}
	}

}
