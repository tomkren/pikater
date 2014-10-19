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
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.CoreConstant;
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
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;
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
import org.pikater.core.utilities.CoreUtilities;
import org.pikater.shared.database.exceptions.NoResultException;
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
import org.pikater.shared.database.util.DatabaseUtilities;
import org.pikater.shared.database.util.ResultFormatter;
import org.pikater.shared.experiment.UniversalComputationDescription;
import org.pikater.shared.util.collections.CollectionUtils;

import com.google.common.io.Files;

/**
 * <p>This agent implementation is used to commit database
 * changes made from the core system and also provides
 * database entities to other agents.</p>
 * 
 * <p>Each method corresponds to a particular use case.</p>
 * 
 * @author siposp
 */
public class Agent_DataManager extends PikaterAgent {

	private static final long serialVersionUID = 1L;

	/**
	 * Get ontologies which is using this agent
	 */
	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
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
					Action action = (Action)
							getContentManager().extractContent(request);

					/*
					 * Acount action
					 */
					if (action.getAction() instanceof GetUserID) {
						return respondToGetUserID(request, action);
					}
					if (action.getAction() instanceof GetUser) {
						return respondToGetUser(request, action);
					}

					/*
					 * LogicalNameTraslate actions
					 */
					if (action.getAction() instanceof TranslateFilename) {
						return respondToTranslateFilename(request, action);
					}

					/*
					 * AgentInfo actions
					 */
					if (action.getAction() instanceof SaveAgentInfo) {
						return respondToSaveAgentInfo(request, action);
					}
					if (action.getAction() instanceof GetAgentInfo) {
						return respondToGetAgentInfo(request, action);
					}
					if (action.getAction() instanceof GetAgentInfos) {
						return respondToGetAgentInfos(request, action);
					}
					if (action.getAction() instanceof GetAllAgentInfos) {
						return respondToGetAllAgentInfos(request, action);
					}
					if (action.getAction() instanceof GetExternalAgentNames) {
						return respondToGetExternalAgentNames(request, action);
					}

					/*
					 * Batch actions
					 */
					if (action.getAction() instanceof SaveBatch) {
						return respondToSaveBatch(request, action);
					}
					if (action.getAction() instanceof LoadBatch) {
						return respondToLoadBatch(request, action);
					}
					if (action.getAction() instanceof UpdateBatchStatus) {
						return respondToUpdateBatchStatus(request, action);
					}
					if (action.getAction() instanceof GetBatchPriority) {
						return respondToGetBatchPriority(request, action);
					}

					/*
					 * Experiment actions
					 */
					if (action.getAction() instanceof SaveExperiment) {
						return respondToSaveExperiment(request, action);
					}
					if (action.getAction() instanceof UpdateExperimentStatus) {
						return respondToUpdateExperimentStatus(request, action);
					}

					/*
					 * Results actions
					 */
					if (action.getAction() instanceof SaveResults) {
						return respondToSaveResults(request, action);
					}
					if (action.getAction() instanceof LoadResults) {
						logSevere("Not Implemented");
					}

					/*
					 * Dataset actions
					 */
					if (action.getAction() instanceof SaveDataset) {
						return respondToSaveDatasetMessage(request, action);
					}

					/*
					 * Metadata actions
					 */
					if (action.getAction() instanceof SaveMetadata) {
						return respondToSaveMetadataMessage(request, action);
					}
					if (action.getAction() instanceof GetMetadata) {
						return replyToGetMetadata(request, action);
					}
					if (action.getAction() instanceof GetAllMetadata) {
						return respondToGetAllMetadata(request, action);
					}
					if (action.getAction() instanceof GetMultipleBestAgents) {
						return respondToGetTheBestAgent(request, action);
					}

					/*
					 * Model actions
					 */
					if (action.getAction() instanceof SaveModel) {
						return respondToSaveModel(request, action);
					}
					if (action.getAction() instanceof GetModel) {
						return respondToGetModel(request, action);
					}
					if (action.getAction() instanceof GetModels) {
						return respondToGetModels(request, action);
					}

					/*
					 * Files actions
					 */
					if (action.getAction() instanceof GetExternalAgentJar) {
						return respondToGetExternalAgentJar(request, action);
					}
					if (action.getAction() instanceof PrepareFileUpload) {
						return respondToPrepareFileUpload(request, action);
					}

					if (action.getAction() instanceof GetFile) {
						return respondToGetFile(request, action);
					}
					if (action.getAction() instanceof GetAllDatasetInfo) {
						return respondToGetAllDatasetInfo(request, action);
					}
					
					/*
					 * Deprecated Files actions, that should not be used by any agent.
					 * Unfortunately may exist some agents, that might still use these
					 * actions.
					 * 
					 * To inform about such behaviour {@link UnsupportedOperationException} is
					 * thrown.
					 */
					if (action.getAction() instanceof GetFileInfo) {
						throw new UnsupportedOperationException();
					}
					if (action.getAction() instanceof ImportFile) {
						throw new UnsupportedOperationException();
					}
					if (action.getAction() instanceof GetFiles) {
						throw new UnsupportedOperationException();
					}
					if (action.getAction() instanceof DeleteTempFiles) {
						throw new UnsupportedOperationException();
					}

				} catch (OntologyException e) {
					logException("Problem extracting content: " + e.getMessage(), e);
				} catch (CodecException e) {
					logException("Codec problem: " + e.getMessage(), e);
				} catch (Exception e) {
					logException(e.getMessage(), e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logSevere("Failure responding to request: " + request.getConversationId());
				return failure;
			}

		});

		cleanupAbortedBatches();
	}

	/**
	 * <p>Cleans up all records related to a batch that was interrupted while
	 * in execution from database.</p>
	 * 
	 * <p>This method is currently only called when the system starts.</p>
	 */
	private void cleanupAbortedBatches() {
		DAOs.batchDAO.cleanUp();
		DAOs.experimentDAO.cleanUp();
	}

	/**
	 * Creates a respond to a {@link GetUserID} request message.
	 * 
	 * @param request {@link ACLMessage} of the request.
	 * @param a {@link Action} containing {@link GetUserID} action
	 * @return {@link ACLMessage} containing the requested ID or a FAILURE message.
	 */
	private ACLMessage respondToGetUserID(ACLMessage request, Action a) {

		logInfo("respondToGetUserID");

		GetUserID getUserID = (GetUserID) a.getAction();

		JPAUser userJPA = DAOs.userDAO.getByLogin(getUserID.getLogin()).get(0);
		if (userJPA == null) {
			// received request links to a non-existing user account - respond with a FAILURE message
			ACLMessage failure = request.createReply();
			failure.setPerformative(ACLMessage.FAILURE);
			logSevere("UserLogin " + getUserID.getLogin() + " doesn't exist");
			return failure;
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(a, userJPA.getId());
		result.setValue(String.valueOf(userJPA.getId()));
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;
	}

	/**
	 * Creates a respond to the {@link GetUser} action.
	 * 
	 * @param request {@link ACLMessage} of the request.
	 * @param a {@link GetUser} action
	 * @return {@link ACLMessage} containing the requested {@link User} object
	 */
	private ACLMessage respondToGetUser(ACLMessage request, Action a) {

		logInfo("respondToGetUser");

		GetUser getUser = (GetUser) a.getAction();

		JPAUser userJPA = DAOs.userDAO.getByID(getUser.getUserID());
		if (userJPA == null) {
			// received request links to a non-existing user account - respond with a FAILURE message
			ACLMessage failure = request.createReply();
			failure.setPerformative(ACLMessage.FAILURE);
			logSevere("UserID " + getUser.getUserID() + " doesn't exist");
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
			logSevere(e.getMessage());
		} catch (OntologyException e) {
			logSevere(e.getMessage());
		}

		return reply;
	}

	/**
	 * <p>Creates a respond to the {@link TranslateFilename} action requesting
	 * filename conversions in database. Conversions always succeed. 
	 * 
	 * TODO: If the action is always successful, why is there a need for errors?
	 * "Error state is representing by returning 'error' as the translated filename."
	 * 
	 * TODO: wouldn't it be better to return null if something went wrong? Lets hard-code
	 * a little less :).
	 * 
	 * @param request {@link ACLMessage} received with the request. 
	 * @param action {@link Action} containing {@link TranslateFilename} action 
	 * @return {@link ACLMessage} containing String of the translated filename or value 'error'
	 * @throws CodecException
	 * @throws OntologyException
	 */
	private ACLMessage respondToTranslateFilename(ACLMessage request,
			Action action) throws CodecException, OntologyException {

		TranslateFilename translateFile =
				(TranslateFilename) action.getAction();

		String translatedName = "error";

		// external to internal filename translation was requested
		if ((translateFile.getExternalFilename() != null) &&
				(translateFile.getInternalFilename() == null)) {

			logInfo("respondToTranslateFilename External File Name " +
					translateFile.getExternalFilename());

			List<JPAFilemapping> files = 
					DAOs.filemappingDAO.getByExternalFilename(
							translateFile.getExternalFilename());
			if (!files.isEmpty()) {
				translatedName = files.get(0).getInternalfilename();
			} else {
				String pathPrefix = CoreConfiguration.getDataFilesPath() +
						"temp" + System.getProperty("file.separator");

				String tempFileName = pathPrefix +
						translateFile.getExternalFilename();
				
				if (new File(tempFileName).exists()) {
					translatedName = "temp" +
							System.getProperty("file.separator") +
							translateFile.getExternalFilename();
				}
			}

		// internal to external filename translation was requested
		} else if ((translateFile.getInternalFilename() != null) &&
				(translateFile.getExternalFilename() == null)) {

			logInfo("respondToTranslateFilename Internal File Name " +
					translateFile.getInternalFilename());

			List<JPAFilemapping> files =
					DAOs.filemappingDAO.getByExternalFilename(
							translateFile.getInternalFilename());
			
			if (!files.isEmpty()) {
				translatedName = files.get(0).getExternalfilename();
			} else {
				String pathPrefix = CoreConfiguration.getDataFilesPath() +
						"temp" + System.getProperty("file.separator");

				String tempFileName = pathPrefix +
						translateFile.getExternalFilename();
				
				if (new File(tempFileName).exists()) {
					translatedName = "temp" +
							System.getProperty("file.separator") +
							translateFile.getExternalFilename();
				}
			}
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(translateFile, translatedName);
		getContentManager().fillContent(reply, result);

		return reply;
	}

	/**
	 * Creates a respond to {@link GetAgentInfo} action.
	 *  
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing {@link GetAgentInfo} action.
	 * @return {@link ACLMessage} containing the requested {@link AgentInfo} object
	 */
	protected ACLMessage respondToGetAgentInfo(ACLMessage request, Action action) {

		GetAgentInfo getAgentInfo = (GetAgentInfo) action.getAction();
		String agentClass = getAgentInfo.getAgentClassName();

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		List<JPAAgentInfo> agentInfoList =
				DAOs.agentInfoDAO.getByAgentClass(agentClass);
		JPAAgentInfo agentInfoJPA = agentInfoList.get(0);

		AgentInfo agentInfo =
				AgentInfo.importXML(agentInfoJPA.getInformationXML());

		Result result = new Result(action, agentInfo);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;

	}

	/**
	 * Creates a respond to {@link GetAgentInfos} action.
	 *   
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing {@link GetAgentInfos} action.
	 * @return {@link ACLMessage} containing the requested {@link AgentInfo} objects
	 * wrapped in {@link AgentInfos} or a FAILURE message.
	 */
	protected ACLMessage respondToGetAgentInfos(ACLMessage request,
			Action action) {

		GetAgentInfos getAgentInfos = (GetAgentInfos) action.getAction();
		int userID = getAgentInfos.getUserID();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.FAILURE);

		JPAUser user = DAOs.userDAO.getByID(userID);
		
		List<JPAAgentInfo> agentInfoList =
				DAOs.agentInfoDAO.getByExternalAgentOwner(user);

		AgentInfos agentInfos = new AgentInfos();
		for (JPAAgentInfo jpaAgentInfoI : agentInfoList) {
			
			// global agent... always approved
			// external agent for a user... check if approved
			if((jpaAgentInfoI.getExternalAgent() == null) ||
					jpaAgentInfoI.getExternalAgent().isApproved()) {
				AgentInfo agentInfoI =
						AgentInfo.importXML(jpaAgentInfoI.getInformationXML());
				agentInfos.addAgentInfo(agentInfoI);
			}
		}

		Result result = new Result(action, agentInfos);
		try {
			getContentManager().fillContent(reply, result);
			reply.setPerformative(ACLMessage.INFORM);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;

	}
	
	/**
	 * Creates a respond to {@link GetAllAgentInfos} action.
	 * 
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing {@link GetAllAgentInfos} action.
	 * @return {@link ACLMessage} containing the requested {@link AgentInfo}
	 * objects wrapped in {@link AgentInfos}.
	 */
	protected ACLMessage respondToGetAllAgentInfos(ACLMessage request,
			Action action) {
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		List<JPAAgentInfo> agentInfoList = DAOs.agentInfoDAO.getAll();

		AgentInfos agentInfos = new AgentInfos();
		for (JPAAgentInfo jpaAgentInfoI : agentInfoList) {

			AgentInfo agentInfoI =
					AgentInfo.importXML(jpaAgentInfoI.getInformationXML());
			agentInfos.addAgentInfo(agentInfoI);
		}

		Result result = new Result(action, agentInfos);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;
	}
	
	/**
	 * Creates a respond to {@link GetExternalAgentNames} action.
	 *   
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing {@link GetExternalAgentNames} action.
	 * @return {@link ACLMessage} containing the requested {@link AgentInfo} objects
	 * wrapped in {@link ExternalAgentNames} or a FAILURE message.
	 */
	protected ACLMessage respondToGetExternalAgentNames(ACLMessage request,
			Action action) {

		logInfo("getting external agent names");

		List<JPAExternalAgent> externalAgents = DAOs.externalAgentDAO.getAll();

		List<AgentClass> agentNames = new ArrayList<AgentClass>();
		for (JPAExternalAgent JPAAgentI : externalAgents) {
			AgentClass agentClass = new AgentClass(
					JPAAgentI.getAgentClass());
			
			agentNames.add(agentClass);
		}
		ExternalAgentNames externalAgentNames =
				new ExternalAgentNames(agentNames);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.FAILURE);
		try{
			Result result = new Result(action, externalAgentNames);
			getContentManager().fillContent(reply, result);
			reply.setPerformative(ACLMessage.INFORM);
		}catch(CodecException e){
			logException("", e);
		}catch(OntologyException e){
			logException("", e);
		}

		return reply;
	}

	/**
	 * Attempts to save an instance of {@link AgentInfo} wrapped in
	 * {@link SaveAgentInfo} to database and responds with the result:
	 * <ul>
	 * <li> If there is no record about the agent (class name) in database yet, 
	 * the object is stored successfully.
	 * <li> If such record already exists, FAILURE message is returned. 
	 * </ul>
	 * 
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} providing the {@link AgentInfo} object wrapped
	 * in {@link SaveAgentInfo}.
	 * @return see above
	 */
	protected ACLMessage respondToSaveAgentInfo(ACLMessage request,
			Action action) {

		logInfo("RespondToSaveAgentInfo");

		SaveAgentInfo saveAgentInfo = (SaveAgentInfo) action.getAction();
		AgentInfo newAgentInfo = saveAgentInfo.getAgentInfo();

		String agentClassName = newAgentInfo.getAgentClassName();
		JPAExternalAgent externalAgent =
				DAOs.externalAgentDAO.getByAgentClass(agentClassName);
		
		ACLMessage reply = request.createReply();

		java.util.List<JPAAgentInfo> agentInfoList =
				DAOs.agentInfoDAO.getAll();
		for (JPAAgentInfo jpaAgentInfoI : agentInfoList) {

			AgentInfo agentInfoI =
					AgentInfo.importXML(jpaAgentInfoI.getInformationXML());
			if (agentInfoI.equals(newAgentInfo)) {
				reply.setPerformative(ACLMessage.FAILURE);
				reply.setContent(
						"AgentInfo has been  already stored in the database");
				return reply;
			}
		}
		
		DAOs.agentInfoDAO.storeAgentInfoOntology(newAgentInfo, externalAgent);

		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");

		return reply;
	}

	
	/**
	 * Saves a new batch wrapped in {@link SaveBatch} to database,
	 * after it is first converted to a {@link UniversalComputationDescription
	 * database-compatible batch format} and then to XML. Some metadata are 
	 * also stored - e.g. name of the batch.
	 * 
	 * @param request {@link ACLMessage} received with the request
	 * @param action {@link Action} containing a {@link SaveBatch} action
	 * @return {@link ACLMessage} of the reply
	 */
	private ACLMessage respondToSaveBatch(ACLMessage request, Action action) {

		logInfo("RespondToSaveBatch");

		SaveBatch saveBatch = (SaveBatch) action.getAction();
		Batch batch = saveBatch.getBatch();
		ComputationDescription description = batch.getDescription();

		UniversalComputationDescription uDescription =
				description.exportUniversalComputationDescription();

		JPAUser user = DAOs.userDAO.getByID(batch.getOwnerID());

		String batchXml = uDescription.toXML();

		JPABatch batchJpa = new JPABatch();
		batchJpa.setName(batch.getName());
		batchJpa.setNote(batch.getNote());
		batchJpa.setStatus(batch.getStatus());
		batchJpa.setUserAssignedPriority(batch.getPriority());
		batchJpa.setXML(batchXml);
		batchJpa.setOwner(user);
		batchJpa.setCreated(new Date());

		DAOs.batchDAO.storeEntity(batchJpa);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		SavedBatch savedBatch = new SavedBatch();
		savedBatch.setSavedBatchId(batchJpa.getId());
		savedBatch.setMessage("OK");

		Result result = new Result(action, savedBatch);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;
	}

	/**
	 * Creates a respond to {@link LoadBatch} request. {@link LoadBatch} contains the ID
	 * of the batch to be loaded.
	 * 
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing {@link LoadBatch} action.
	 * @return {@link ACLMessage} containing {@link Batch} object defining the batch.
	 */
	private ACLMessage respondToLoadBatch(ACLMessage request, Action action) {

		logInfo("respondToLoadBatch");

		LoadBatch loadBatch = (LoadBatch) action.getAction();

		JPABatch batchJPA = DAOs.batchDAO.getByID(loadBatch.getBatchID());

		UniversalComputationDescription uDescription =
				UniversalComputationDescription.fromXML(batchJPA.getXML());

		ComputationDescription compDescription =
				ComputationDescription
				.importUniversalComputationDescription(uDescription);

		Batch batch = new Batch();
		batch.setId(batchJPA.getId());
		batch.setName(batchJPA.getName());
		batch.setStatus(batchJPA.getStatus().name());
		batch.setOwnerID(batchJPA.getOwner().getId());
		batch.setPriority(batchJPA.getUserAssignedPriority());
		batch.setDescription(compDescription);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(action, batch);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;
	}

	/**
	 * Requests a notification e-mail be sent to the owner of
	 * the given {@link JPABatch}.
	 * 
	 * Some experiments can be executed for quite a long time. After
	 * the execution is finished, user can be about the results of the
	 * long-lasting experiment.
	 *  
	 */
	private void requestMailNotification(final JPABatch batchJPA) {
		addBehaviour(new OneShotBehaviour() {
			
			private static final long serialVersionUID = -6987340128342367505L;

			@Override
			public void action() {
				
				String mailAddr = batchJPA.getOwner().getEmail();
				SendEmail action = new SendEmail(
						Agent_Mailing.EmailType.RESULT, mailAddr);
				action.setBatchId(batchJPA.getId());
				List<JPAExperiment> exps = batchJPA.getExperiments();
				
				// when there was more than 1 sub-experiment, send the best result 
				if (exps.size() == 1 && !exps.get(0).getResults().isEmpty()) {
					double bestErrorRate = 200;
					for (JPAResult r : exps.get(0).getResults()) {
						if (r.getErrorRate() < bestErrorRate) {
							bestErrorRate = r.getErrorRate();
						}
					}
					action.setResult(bestErrorRate);
				}
				AID receiver = new AID(CoreAgents.MAILING.getName(), false);
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				Ontology ontology = MailingOntology.getInstance();

				request.addReceiver(receiver);
				request.setLanguage(getCodec().getName());
				request.setOntology(ontology.getName());
				try {
					Action requestAction = new Action(receiver, action);
					getContentManager().fillContent(request, requestAction);
					logInfo("Sending notification request to mailAgent");
					ACLMessage reply = FIPAService.doFipaRequestClient(
							this.myAgent, request, 10000);
					if (reply == null) {
						logSevere("Reply not received.");
					}
				} catch (CodecException | OntologyException | FIPAException e) {
					logException("Failed to request e-mail", e);
				}
			}
		});
	}

	/**
	 * <p>Updates the given batch's status with the given status. Both of them
	 * are specified by {@link UpdateBatchStatus} object wrapped within
	 * the action.</p>
	 * 
	 * As a side effect:
	 * <ul>
	 * <li> If the given status is {@link JPABatchStatus#COMPUTING}, sets the start
	 * time for the given batch.
	 * <li> If the given status is {@link JPABatchStatus#FAILED} or
	 * {@link JPABatchStatus#FINISHED}, sets the finish time for the given batch. 
	 * </ul>
	 * 
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing {@link UpdateBatchStatus} action.
	 * @return {@link ACLMessage} confirming the batch status update.
	 */
	protected ACLMessage respondToUpdateBatchStatus(ACLMessage request,
			Action action) {

		logInfo("respondToUpdateBatchStatus");

		UpdateBatchStatus updateBatchStatus = (UpdateBatchStatus)
				action.getAction();
		
		logInfo("****** " + updateBatchStatus.getBatchID());


		JPABatch batchJPA =
				DAOs.batchDAO.getByID(updateBatchStatus.getBatchID());
		JPABatchStatus batchStatus =
				JPABatchStatus.valueOf(updateBatchStatus.getStatus());
		
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

		if ((batchStatus == JPABatchStatus.FINISHED) &&
				(batchJPA.isSendEmailAfterFinish())) {
			requestMailNotification(batchJPA);
		} else {
			logInfo("not sending mail notification - option not set");
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");

		return reply;
	}

	/**
	 * Creates a respond containing priority of the batch specified
	 * in incoming {@link GetBatchPriority} object.
	 *  
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing the {@link GetBatchPriority} object.
	 * @return {@link ACLMessage} of the reply or FAILURE message if no such batch
	 * exists in the database. 
	 */
	private ACLMessage respondToGetBatchPriority(ACLMessage request,
			Action action) {
		
		logInfo("respondToGetBatchPriority");

		GetBatchPriority getBatchPriority = (GetBatchPriority)
				action.getAction();
		int batchID = getBatchPriority.getBatchID();

		JPABatch batchJPA = DAOs.batchDAO.getByID(batchID);

		ACLMessage reply = request.createReply();

		if (batchJPA == null) {
			reply.setPerformative(ACLMessage.FAILURE);
		} else {
			reply.setPerformative(ACLMessage.INFORM);

			Result result = new Result(action, batchJPA.getTotalPriority());
			try {
				getContentManager().fillContent(reply, result);
			} catch (CodecException e) {
				logException(e.getMessage(), e);
			} catch (OntologyException e) {
				logException(e.getMessage(), e);
			}
		}

		return reply;
	}

	/**
	 * Stores a new {@link Experiment experiment}, wrapped in {@link SavedExperiment}, to the database.
	 * 
	 * @param request {@link ACLMessage} received with the request
	 * @param action {@link Action} containing the {@link SavedExperiment} action.
	 * @return {@link ACLMessage} of the reply
	 */
	private ACLMessage respondToSaveExperiment(ACLMessage request,
			Action action) {

		logInfo("respondToSaveExperiment");

		SaveExperiment saveExperiment = (SaveExperiment)
				action.getAction();
		Experiment experiment = saveExperiment.getExperiment();

		/* Computation begins when experiment is created by Parser
		 *  and SaveExperiment message is sent. 
		 */
		int savedID = DAOs.batchDAO.addExperimentToBatch(experiment);

		ACLMessage reply = request.createReply();

		if (savedID == -1) {
			reply.setPerformative(ACLMessage.FAILURE);
		} else {
			reply.setPerformative(ACLMessage.INFORM);

			SavedExperiment savedExperiment = new SavedExperiment();
			savedExperiment.setSavedExperimentId(savedID);
			savedExperiment.setMessage("OK");

			Result result = new Result(action, savedExperiment);
			try {
				getContentManager().fillContent(reply, result);
			} catch (CodecException e) {
				logException(e.getMessage(), e);
			} catch (OntologyException e) {
				logException(e.getMessage(), e);
			}
		}

		return reply;
	}

	/**
	 * Changes status of the given experiment to the given value, wrapped in
	 * {@link UpdateExperimentStatus}. As a side effect:
	 * <ul>
	 * <li> If the new status is COMPUTING, start time of the experiment is reset to now.
	 * <li> If the new status is FAILED or FINISHED, finish time of the experiment is reset to now. 
	 * </ul> 
	 *
	 * @param request {@link ACLMessage} of the request.
	 * @param action {@link Action} containing the {@link UpdateExperimentStatus}
	 * @return {@link ACLMessage} of the reply
	 */
	protected ACLMessage respondToUpdateExperimentStatus(ACLMessage request,
			Action action) {

		logInfo("respondToUpdateExperimentStatus");

		UpdateExperimentStatus updateExperimentStatus = (UpdateExperimentStatus)
				action.getAction();

		JPAExperiment experimentJPA =
				DAOs.experimentDAO.getByID(
						updateExperimentStatus.getExperimentID());
		JPAExperimentStatus experimentStatus =
				JPAExperimentStatus.valueOf(
						updateExperimentStatus.getStatus());
		
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

	/**
	 * Stores the given {@link SaveResults results/statistics} to database
	 * and as a side effect, links other database entities like datasets
	 * with them.
	 * 
	 * @param request {@link ACLMessage} received with the request
	 * @param action {@link Action} containing action {@link SaveResults}
	 * @return {@link ACLMessage} of the reply
	 */
	private ACLMessage respondToSaveResults(ACLMessage request,
			Action action) {
		
		SaveResults saveResult = (SaveResults)
				action.getAction();
		
		Task task = saveResult.getTask();
		NewOptions options = new NewOptions(task.getAgent().getOptions());

		int experimentID = saveResult.getExperimentID();

		JPAResult jparesult = new JPAResult();
		jparesult.setAgentName(task.getAgent().getType());
		logInfo("Adding result for Agent Type: " + task.getAgent().getType());
		jparesult.setOptions(options.exportXML());
		logInfo("Saving result for hash: " +
				task.getDatas().exportInternalTrainFileName());
		
		String inernalTrainFileName =
				task.getDatas().exportInternalTrainFileName();
		jparesult.setSerializedFileName(inernalTrainFileName);

		//Associating input datasets with the result
		for (Data dataI : CollectionUtils.nullSafeList(task.getDatas().getDatas())) {
			if (dataI == null) {
				continue;
			}
			
			List<JPADataSetLO> daoSets =
					DAOs.dataSetDAO.getByHash(dataI.getInternalFileName());
			ResultFormatter<JPADataSetLO> resultFormartter =
					new ResultFormatter<JPADataSetLO>(daoSets);
			
			JPADataSetLO dslo = resultFormartter.getSingleResultWithNull();
			if (dslo != null) {
				if (!DatabaseUtilities.containsID(jparesult.getInputs(), dslo.getId())) {
					jparesult.getInputs().add(dslo);
					logInfo("Adding input " + dataI.getInternalFileName() +
							" to result." );
				} else {
					logInfo("Adding input " + dataI.getInternalFileName() +
							" skipped. Duplicate value.");
				}
			} else {
				logSevere("Failed to add output " +
						dataI.getInternalFileName() +
						" to result for train dataset " +
						task.getDatas().exportInternalTrainFileName());
			}
		}
		
		//Associating output datasets with the result
		for (TaskOutput outputI : task.getOutput()) {
			
			List<JPADataSetLO> dataSets =
					DAOs.dataSetDAO.getByHash(outputI.getName());
			ResultFormatter<JPADataSetLO> resultFormatter =
					new ResultFormatter<JPADataSetLO>(dataSets);
			
			JPADataSetLO dslo = resultFormatter.getSingleResultWithNull();
			if (dslo != null) {
				if (!DatabaseUtilities.containsID(jparesult.getOutputs(), dslo.getId())) {
					jparesult.getOutputs().add(dslo);
					
					logInfo("Adding output " + outputI.getName() +
							" to result for train dataset " +
							task.getDatas().exportInternalTrainFileName());
				} else {
					logInfo("Adding output " + outputI.getName() +
							" skipped. Duplicate value.");
				}
			} else {
				logSevere("Failed to add output " + outputI.getName() +
						" to result for train dataset " +
						task.getDatas().exportInternalTrainFileName());
			}
		}

		float errorRate = Float.MAX_VALUE;
		float kappaStatistic = Float.MAX_VALUE;
		float meanAbsoluteError = Float.MAX_VALUE;
		float rootMeanSquaredError = Float.MAX_VALUE;
		// percent
		float relativeAbsoluteError = Float.MAX_VALUE;
		// percent
		float rootRelativeSquaredError = Float.MAX_VALUE;
		// miliseconds
		int duration = Integer.MAX_VALUE;
		float durationLR = Float.MAX_VALUE;

		Evaluation evaluation = task.getResult();

		Eval errorRateEval = evaluation.exportEvalByName(
				CoreConstant.Error.ERROR_RATE.name());
		if (errorRateEval != null) {
			errorRate = errorRateEval.getValue();
		}

		Eval kappaStatisticEval = evaluation.exportEvalByName(
				CoreConstant.Error.KAPPA_STATISTIC.name());
		if (kappaStatisticEval != null) {
			kappaStatistic = kappaStatisticEval.getValue();
		}

		Eval meanAbsoluteEval = evaluation.exportEvalByName(
				CoreConstant.Error.MEAN_ABSOLUTE.name());
		if (meanAbsoluteEval != null) {
			meanAbsoluteError = meanAbsoluteEval.getValue();
		}

		Eval rootMeanSquaredEval = evaluation.exportEvalByName(
				CoreConstant.Error.ROOT_MEAN_SQUARED.name());
		if (rootMeanSquaredEval != null) {
			rootMeanSquaredError = rootMeanSquaredEval.getValue();
		}

		Eval relativeAbsoluteEval = evaluation.exportEvalByName(
				CoreConstant.Error.RELATIVE_ABSOLUTE.name());
		if (relativeAbsoluteEval != null) {
			relativeAbsoluteError = relativeAbsoluteEval.getValue();
		}

		Eval rootRelativeSquaredEval = evaluation.exportEvalByName(
				CoreConstant.Error.ROOT_RELATIVE_SQUARED.name());
		if (rootRelativeSquaredEval != null) {
			rootRelativeSquaredError = rootRelativeSquaredEval.getValue();
		}

		Eval durationEval = evaluation.exportEvalByName(
				CoreConstant.DURATION);
		if (durationEval != null) {
			duration = (int) durationEval.getValue();
		}

		Eval durationLREval = evaluation.exportEvalByName(
				CoreConstant.DURATIONLR);
		if (durationLREval != null) {
			durationLR = (float) durationLREval.getValue();
		}

		jparesult.setErrorRate(errorRate);
		jparesult.setKappaStatistic(kappaStatistic);
		jparesult.setMeanAbsoluteError(meanAbsoluteError);
		jparesult.setRootMeanSquaredError(rootMeanSquaredError);
		jparesult.setRelativeAbsoluteError(relativeAbsoluteError);
		jparesult.setRootRelativeSquaredError(rootRelativeSquaredError);
		jparesult.setDuration(duration);
		jparesult.setDurationLR(durationLR);

		Date start = new Date();
		if (task.getStart() != null) {
			start = CoreUtilities.getDateFromPikaterDateString(task.getStart());
		} else {
			logSevere("Result start date isn't set. Using current DateTime");
		}
		jparesult.setStart(start);
		logInfo("Start Date set: " + start.toString());

		Date finish = new Date();
		if (task.getFinish() != null) {
			finish = CoreUtilities.getDateFromPikaterDateString(task.getFinish());
		} else {
			logSevere("Result finish date isn't set. Using current DateTime");
		}
		jparesult.setFinish(finish);
		logInfo("Finish Date set: " + finish.toString());

		jparesult.setNote(task.getNote());
		logInfo("JPA Result    " + jparesult.getErrorRate());
		int resultID = DAOs.experimentDAO.addResultToExperiment(
				experimentID, jparesult);
		if (resultID != -1) {
			logInfo("Persisted JPAResult for experiment ID " +
					experimentID + " with ID: " + resultID);
			
			if (task.getResult().getObject() != null) {
				saveResultModel(task, resultID);
			}
		} else {
			logSevere("Couldn't persist JPAResult for experiment ID \n" + experimentID);
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}

	/**
	 * Stores a dataset containing an experiment's result data. The location of the file
	 * is encapsulated in the {@link SaveDataset} action.
	 * 
	 * @param request {@link ACLMessage} received with the request
	 * @param action {@link Action} containing {@link SaveDataset} action
	 * @return {@link ACLMessage} of the reply containing the ID of stored dataset or with
	 * FAILURE flag set after unsuccessful operation.
	 */
	private ACLMessage respondToSaveDatasetMessage(ACLMessage request,
			Action action) {
		
		SaveDataset savedataset = (SaveDataset) action.getAction();

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		try {
			File file = new File(savedataset.getSourceFile());
			int jpadsloID = DAOs.dataSetDAO.storeNewDataSet(file,
					file.getName(), savedataset.getDescription(),
					savedataset.getUserID(),JPADatasetSource.EXPERIMENT);

			reply.setContentObject(new Integer(jpadsloID));
			logInfo("Saved Dataset with ID: " + jpadsloID +
					" for sourcefile "+savedataset.getSourceFile());
			
		} catch (NoResultException e) {
			logException("No user found with login: " +
					savedataset.getUserID(), e);
			
			reply.setPerformative(ACLMessage.FAILURE);
		} catch (IOException e) {
			logException("File can't be read.", e);
			reply.setPerformative(ACLMessage.FAILURE);
		}

		return reply;
	}

	/**
	 * Associates the given agent model with the given experiment result.
	 *  
	 * @param task {@link Task} containing data about the model.
	 * @param resultId ID of the result
	 */
	private void saveResultModel(Task task, int resultId) {
		Model m = new Model();
		m.setAgentClassName(task.getAgent().getType());
		m.setResultID(resultId);
		m.setSerializedAgent(task.getResult().getObject());
		int savedModelID = DAOs.resultDAO.setModelForResult(m);
		if (savedModelID == -1) {
			logSevere("Failed to persist model for result with ID " +
					resultId);
		} else {
			logInfo("Persisted JPAModel " + savedModelID);
		}
	}

	/**
	 * Stores metadata wrapped in {@link SaveMetadata} to the given dataset. Dataset
	 * is looked up by a hash and so:
	 * <ul>
	 * <li> If it already contains all metadata, no actions is taken.
	 * <li> If it doesn't contain all metadata (or contains none), they are merged
	 * with the given metadata.
	 * </ul>
	 * 
	 * @param request {@link ACLMessage} received with the request
	 * @param a {@link Action} containing {@link SaveMetadata} action
	 * @return {@link ACLMessage} with the reply
	 */
	private ACLMessage respondToSaveMetadataMessage(ACLMessage request,
			Action a) {
		
		SaveMetadata saveMetadata = (SaveMetadata) a.getAction();
		Metadata metadata = saveMetadata.getMetadata();
		int dataSetID = saveMetadata.getDataSetID();

		JPADataSetLO dslo;
		try {
			dslo = DAOs.dataSetDAO.getByID(dataSetID, EmptyResultAction.THROW);

			String currentHash = dslo.getHash();

			List<JPADataSetLO> equalDataSets =
					DAOs.dataSetDAO.getByHash(currentHash);
			logInfo("Hash of new dataset: " + currentHash);
			//we search for a dataset entry with the same hash and with already generated metadata
			JPADataSetLO dsloWithMetaData = null;
			for (JPADataSetLO candidateI : equalDataSets) {
				if ((candidateI.getAttributeMetaData() != null) &&
						(candidateI.getGlobalMetaData() != null)) {
					dsloWithMetaData = candidateI;
					break;
				}
			}

			if (dsloWithMetaData == null) {
				JPAMetaDataReader readr = new JPAMetaDataReader(metadata);
				dslo.setGlobalMetaData(readr.getJPAGlobalMetaData());
				dslo.setAttributeMetaData(readr.getJPAAttributeMetaData());
			} else {
				dslo.setAttributeMetaData(
						dsloWithMetaData.getAttributeMetaData());
				dslo.setGlobalMetaData(dsloWithMetaData.getGlobalMetaData());
			}
			DAOs.dataSetDAO.updateEntity(dslo);

		} catch (NoResultException e1) {
			logException("Dataset for ID " + dataSetID + " doesn't exist.", e1);
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		return reply;
	}

	/**
	 * Retrieves metadata for the given dataset. Dataset is looked by a hash.
	 * 
	 * @param request {@link ACLMessage} received with the request/
	 * @param action {@link Action} containing {@link GetMetadata} action
	 * @return {@link ACLMessage} reply containing the retrieved {@link Metadata} object
	 * @throws CodecException
	 * @throws OntologyException
	 */
	private ACLMessage replyToGetMetadata(ACLMessage request,
			Action action) throws CodecException, OntologyException {
		
		GetMetadata getMetadata = (GetMetadata) action.getAction();

		Metadata metadata = new Metadata();
		ACLMessage reply = request.createReply();
		logInfo("Retrieving metadata for hash " +
				getMetadata.getInternalFilename());
		
		List<JPADataSetLO> dataSets =
				DAOs.dataSetDAO.getByHash(getMetadata.getInternalFilename());
		ResultFormatter<JPADataSetLO> resultFormatter =
				new ResultFormatter<JPADataSetLO>(dataSets);
		JPADataSetLO dslo = resultFormatter.getSingleResultWithNull();

		if ((dslo != null) &&
			(dslo.getGlobalMetaData() != null) &&
			(dslo.getAttributeMetaData() != null)) {
			
			metadata = this.convertJPADatasetToOntologyMetadata(dslo);
			reply.setPerformative(ACLMessage.INFORM);
		} else {
			reply.setPerformative(ACLMessage.FAILURE);
		}

		Result result = new Result(action.getAction(), metadata);
		getContentManager().fillContent(reply, result);

		return reply;
	}

	/**
	 * Creates an {@link AttributeMetadata} object from a {@link JPAAttributeMetaData} object.
	 * 
	 * @param amd {@link JPAAttributeMetaData} object
	 * @return {@link AttributeMetadata} object representing the input {@link JPAAttributeMetaData}
	 */
	private AttributeMetadata convertJPAAttributeMetadataToOntologyMetadata(
			JPAAttributeMetaData amd) {
		
		AttributeMetadata attributeMetadata;

		if (amd instanceof JPAAttributeNumericalMetaData) {

			JPAAttributeNumericalMetaData jpaAttrnum =
					(JPAAttributeNumericalMetaData) amd;
			
			NumericalAttributeMetadata numAttributeMetadata =
					new NumericalAttributeMetadata();

			numAttributeMetadata.setAvg(jpaAttrnum.getAvarage());
			numAttributeMetadata.setMax(jpaAttrnum.getMax());
			numAttributeMetadata.setMin(jpaAttrnum.getMin());
			numAttributeMetadata.setMedian(jpaAttrnum.getMedian());
			numAttributeMetadata.setStandardDeviation(jpaAttrnum.getVariance());

			attributeMetadata = numAttributeMetadata;
			
		} else if (amd instanceof JPAAttributeCategoricalMetaData) {
			
			JPAAttributeCategoricalMetaData jpaAttrCat =
					(JPAAttributeCategoricalMetaData) amd;

			CategoricalAttributeMetadata catAttributeMetadata =
					new CategoricalAttributeMetadata();

			catAttributeMetadata.setNumberOfCategories(
					jpaAttrCat.getNumberOfCategories());
			
			attributeMetadata = catAttributeMetadata;

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

	/**
	 * Creates a {@link Metadata} ontology representing metadata of the given
	 * {@link JPADataSetLO}.
	 *  
	 * @param dslo {@link JPADataSetLO} object containing the metadata.
	 * @return {@link Metadata} object
	 */
	private Metadata convertJPADatasetToOntologyMetadata(JPADataSetLO dslo) {
		JPAGlobalMetaData gmd = dslo.getGlobalMetaData();

		Metadata globalMetaData = new Metadata();

		List<JPAFilemapping> fileMappings =
				DAOs.filemappingDAO.getByInternalFilename(dslo.getHash());
		ResultFormatter<JPAFilemapping> resultFormatter =
				new ResultFormatter<JPAFilemapping>(fileMappings);
		JPAFilemapping filemap = resultFormatter.getSingleResultWithNull();

		if (filemap != null) {
			globalMetaData.setExternalName(filemap.getExternalfilename());
			globalMetaData.setInternalName(filemap.getInternalfilename());
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

		for (JPAAttributeMetaData amdI : attrMDs) {
			AttributeMetadata attributeMetadata =
					this.convertJPAAttributeMetadataToOntologyMetadata(amdI);

			missingValues = missingValues || (amdI.getRatioOfMissingValues() > 0);

			globalMetaData.getAttributeMetadataList().add(attributeMetadata);
		}

		globalMetaData.setMissingValues(missingValues);

		return globalMetaData;

	}

	/**
	 * Retrieves all metadata for the datasets specified in {@link GetAllMetadata}.
	 *  
	 * @param request {@link ACLMessage} received with the request
	 * @param action {@link Action} containing {@link GetAllMetadata} action
	 * @return {@link ACLMessage} reply containing metadata for the datasets.
	 * @throws CodecException
	 * @throws OntologyException
	 */
	private ACLMessage respondToGetAllMetadata(ACLMessage request,
			Action action) throws CodecException, OntologyException {
		
		GetAllMetadata getMetadata = (GetAllMetadata) action.getAction();

		logInfo("Agent_DataManager.respondToGetAllMetadata");

		List<String> exHash = new java.util.LinkedList<String>();
		for (Metadata metadataI : getMetadata.getExceptions()) {
			exHash.add(metadataI.getInternalName());
		}
		
		List<JPADataSetLO> datasets = null;
		
		if (getMetadata.getResultsRequired()) {
			logInfo("DataManager.Results Required");
			datasets = DAOs.dataSetDAO
					.getAllWithResultsExcludingHashesWithMetadata(exHash);
		} else {
			logInfo("DataManager.Results NOT Required");
			datasets =
					DAOs.dataSetDAO.getAllExcludingHashesWithMetadata(exHash);
		}

		if (datasets == null) {
			datasets = new ArrayList<JPADataSetLO>();
		}
		
		Metadatas metadatas = new Metadatas();

		for (JPADataSetLO dsloI : datasets) {
			Metadata globalMetaData =
					this.convertJPADatasetToOntologyMetadata(dsloI);
			metadatas.addMetadata(globalMetaData);
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(action.getAction(), metadatas);
		getContentManager().fillContent(reply, result);

		return reply;
	}

	/**
	 * Retrieves the given amount of the best agents (ordered by result 
	 * error rate) of a certain experiment for the given dataset.
	 *   
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing {@link GetMultipleBestAgents} action
	 * @return {@link ACLMessage} containing the best agents for the dataset.
	 * @throws ClassNotFoundException
	 * @throws CodecException
	 * @throws OntologyException
	 */
	private ACLMessage respondToGetTheBestAgent(ACLMessage request,
			Action action) throws ClassNotFoundException, CodecException,
			OntologyException {
		
		GetMultipleBestAgents g = (GetMultipleBestAgents) action.getAction();
		String datasethash = g.getNearestInternalFileName();
		int count = g.getNumberOfAgents();

		logInfo("DataManager.GetTheBestAgent for datafile " + datasethash);

		List<JPAResult> results =
				DAOs.resultDAO.getResultsByDataSetHashAscendingUponErrorRate(
						datasethash, count);

		ACLMessage reply = request.createReply();

		Agents foundAgents = new Agents();
		if (!results.isEmpty()) {
			for (JPAResult resultI : results) {
				NewOptions options = new NewOptions();
				try{
					options = NewOptions.importXML(resultI.getOptions());
				}catch(Exception e){
					logSevere("Incompatible options in result, " +
							"using default options");
				}
				Agent agent = new Agent();
				agent.setName(resultI.getAgentName());
				agent.setType(resultI.getAgentName());
				agent.setOptions(options.getOptions());
				foundAgents.add(agent);
			}
		}

		reply.setPerformative(ACLMessage.INFORM);

		Result result = new Result(action.getAction(), foundAgents);
		getContentManager().fillContent(reply, result);

		return reply;
	}

	/**
	 * Stores the given model to the given result. Both are defined by {@link Model}
	 * wrapped in {@link SaveModel}.
	 * 
	 * @param request {@link ACLMessage} of the request. 
	 * @param a {@link Action} containing {@link SaveModel} action
	 * @return {@link ACLMessage} of the reply indicating success or failure using flags INFORM or FAILURE
	 */
	private ACLMessage respondToSaveModel(ACLMessage request, Action a) {

		SaveModel sm = (SaveModel) a.getAction();
		ACLMessage reply = request.createReply();

		int savedModelID = DAOs.resultDAO.setModelForResult(sm.getModel());

		if (savedModelID != -1) {
			logInfo("Saved Model ID: " + savedModelID);
			reply.setPerformative(ACLMessage.INFORM);
		} else {
			logSevere("Couldn't be saved model for experiment ID " +
					sm.getModel().getResultID());
			reply.setPerformative(ACLMessage.FAILURE);
		}

		return reply;
	}

	/**
	 * Retrieves the given model, defined by {@link GetModel}.
	 *  
	 * @param request {@link ACLMessage} of the request.
	 * @param a {@link Action} containing {@link GetModel} action
	 * @return {@link ACLMessage} of the reply
	 */
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
				logException(e.getMessage(), e);
			} catch (OntologyException e) {
				logException(e.getMessage(), e);
			}
		}
		return reply;
	}
	
	/**
	 * Retrieves all models stored in the database.
	 * 
	 * @param request {@link ACLMessage} of the request
	 * @param action {@link Action} containing {@link GetModels} so that action
	 * handler calls this method. 
	 * @return {@link ACLMessage} containing all models stored in the database.
	 */
	private ACLMessage respondToGetModels(ACLMessage request,
			Action action) {
		
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

		Result result = new Result(action, models);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;
	}

	/**
	 * <p>Retrieves an external agent (.JAR), defined by class name in
	 * {@link GetExternalAgentJar}, from database.</p>
	 * 
	 * <p>Content of the JAR is copied to a file on local filesystem
	 * with path taken from {@link CoreConfiguration#getExtAgentsPath()}.</p>
	 * 
	 * @param request {@link ACLMessage} received with the request.
	 * @param action {@link Action} containing {@link GetExternalAgentJar} action
	 * @return {@link ACLMessage} indicating success if everything went well
	 * @throws FailureException
	 * @throws CodecException
	 * @throws OntologyException
	 */
	private ACLMessage respondToGetExternalAgentJar(ACLMessage request,
			Action action) throws FailureException, CodecException,
			OntologyException {
		
		String type = ((GetExternalAgentJar) action.getAction()).getType();
		logInfo("getting JAR for agent type " + type);

		JPAExternalAgent extAgent =
				DAOs.externalAgentDAO.getByAgentClass(type);

		if (extAgent == null) {
			throw new FailureException("Agent jar for type " +
					type + " not found in DB");
		} else {
			String jarname = type.replace(".", "_") + ".jar";
			String jarpath = CoreConfiguration.getExtAgentsPath() + jarname;

			File dest = new File(jarpath);
			File tmp = new File(jarpath + ".tmp");
			try {
				FileUtils.writeByteArrayToFile(tmp, extAgent.getJar());
				Files.move(tmp, dest);
			} catch (IOException e) {
				throw new FailureException("Failed to write/move agent JAR");
			}
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result r = new Result(action, "OK");
		getContentManager().fillContent(reply, r);

		return reply;
	}

	/**
	 * Prepares the necessary actions, needed to perform file uploading
	 * from other agents to this {@link Agent_DataManager}.
	 * 
	 * It opens a {@link ServerSocket}, thus permission in the firewall
	 * rules can be necessary.
	 * 
	 * @param request {@link ACLMessage} received with request
	 * @param action {@link Action} that was used to determine which function should be called
	 * @return {@link ACLMessage} of the reply
	 * @throws CodecException
	 * @throws OntologyException
	 * @throws IOException
	 */
	private ACLMessage respondToPrepareFileUpload(ACLMessage request,
			Action action) throws CodecException, OntologyException,
			IOException {
		
		final String hash = ((PrepareFileUpload) action.getAction()).getHash();
		logInfo("respondToPrepareFileUpload");

		final ServerSocket serverSocket = new ServerSocket();
		serverSocket.setSoTimeout(15000);
		serverSocket.bind(null);
		logInfo("Listening on port: " + serverSocket.getLocalPort());

		addBehaviour(
				new ThreadedBehaviourFactory().wrap(new OneShotBehaviour() {
			private static final long serialVersionUID = -860154937392441937L;

			@Override
			public void action() {
				try {
					DataTransferService
						.handleUploadConnection(serverSocket, hash);
				} catch (IOException e) {
					logException("Data upload failed", e);
				}
			}
		}));

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent(Integer.toString(serverSocket.getLocalPort()));
		return reply;
	}

	/**
	 * Retrieves a dataset, defined by a hash wrapped in {@link GetFile},
	 * from database. Content of the dataset is copied to a file on local
	 * filesystem with a path taken from 
	 * {@link CoreConfiguration#getDataFilesPath()}.
	 * 
	 * @param request {@link ACLMessage} received with the request
	 * @param action {@link Action} containing {@link GetFile} action
	 * @return {@link ACLMessage} of the reply
	 * @throws CodecException
	 * @throws OntologyException
	 * @throws ClassNotFoundException
	 */
	private ACLMessage respondToGetFile(ACLMessage request, Action action)
			throws CodecException, OntologyException, ClassNotFoundException {
		
		String hash = ((GetFile) action.getAction()).getHash();
		logInfo(new Date().toString() + " DataManager.GetFile");

		List<JPADataSetLO> dslos = DAOs.dataSetDAO.getByHash(hash);
		
		if (!dslos.isEmpty()) {
			try {
				JPADataSetLO dslo = dslos.get(0);
				logInfo(new Date().toString() + " Found DSLO: " +
						dslo.getDescription() + " - " + dslo.getOID() +
						" - " + dslo.getHash());

				PGLargeObjectReader reader =
						PGLargeObjectReader.getForLargeObject(dslo.getOID());
				logInfo(reader.toString());
				File temp = new File(CoreConfiguration.getDataFilesPath() +
						"temp" + System.getProperty("file.separator") + hash);
				File target =
						new File(CoreConfiguration.getDataFilesPath() + hash);

				FileOutputStream out = new FileOutputStream(temp);
				try {
					byte[] buf = new byte[100 * 1024];
					int read;
					while ((read = reader.read(buf, 0, buf.length)) > 0) {
						out.write(buf, 0, read);
					}

					logInfo(new Date() + " Moving file to: " +
							target.getAbsolutePath());
					try {
						CopyOption copyOption = (CopyOption)
								StandardCopyOption.REPLACE_EXISTING;
						
						java.nio.file.Files.move(temp.toPath(),
								target.toPath(), copyOption);
						
						logInfo(new Date() + "File was successfully moved");
						
					} catch (IOException ioe) {
						logSevere(new Date() +
								"Error while moving file with hash " +
								dslo.getHash() + " to new location " +
								target.getAbsolutePath());
					}
				} catch (IOException ioe) {
					logException("Error while downloading file with hash " +
							hash + " from database", ioe);
				} finally {
					reader.close();
					out.close();

				}
			} catch (IOException e) {
				logException("Unexpected error occured:", e);
			}
		} else {
			logSevere("DataSet file with hash " + hash + " not found.");
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result r = new Result(action, "OK");
		getContentManager().fillContent(reply, r);

		return reply;
	}
	
	/**
	 * Retrieves the ID, hash and filename triples for all datasets
	 * uploaded by users.
	 * 
	 * @param request {@link ACLMessage} received with the request
	 * @param action {@link Action} containing {@link GetAllDatasetInfo} so that action
	 * handler calls this method. 
	 * @return {@link ACLMessage} containing {@link DatasetsInfo} with the triples
	 */
	public ACLMessage respondToGetAllDatasetInfo(ACLMessage request,
			Action action) {
		
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

		Result result = new Result(action, datasetsOnto);
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
