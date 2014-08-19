package org.pikater.core.agents.system.data;

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
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import java.io.File;
import java.util.Date;

import org.pikater.core.AgentNames;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.AccountOntology;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.ResultOntology;
import org.pikater.core.ontology.subtrees.account.GetUser;
import org.pikater.core.ontology.subtrees.account.GetUserID;
import org.pikater.core.ontology.subtrees.account.User;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.ExternalAgentNames;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetExternalAgentNames;
import org.pikater.core.ontology.subtrees.agentInfo.SaveAgentInfo;
import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batch.GetBatchPriority;
import org.pikater.core.ontology.subtrees.batch.LoadBatch;
import org.pikater.core.ontology.subtrees.batch.SaveBatch;
import org.pikater.core.ontology.subtrees.batch.SavedBatch;
import org.pikater.core.ontology.subtrees.batch.UpdateBatchStatus;
import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.core.ontology.subtrees.experiment.SaveExperiment;
import org.pikater.core.ontology.subtrees.experiment.SavedExperiment;
import org.pikater.core.ontology.subtrees.externalAgent.GetExternalAgentJar;
import org.pikater.core.ontology.subtrees.file.DeleteTempFiles;
import org.pikater.core.ontology.subtrees.file.GetFile;
import org.pikater.core.ontology.subtrees.file.GetFileInfo;
import org.pikater.core.ontology.subtrees.file.GetFiles;
import org.pikater.core.ontology.subtrees.file.ImportFile;
import org.pikater.core.ontology.subtrees.file.TranslateFilename;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.Agents;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.Metadatas;
import org.pikater.core.ontology.subtrees.metadata.SaveMetadata;
import org.pikater.core.ontology.subtrees.metadata.UpdateMetadata;
import org.pikater.core.ontology.subtrees.model.GetModel;
import org.pikater.core.ontology.subtrees.model.GetModels;
import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.core.ontology.subtrees.model.Models;
import org.pikater.core.ontology.subtrees.recommend.GetMultipleBestAgents;
import org.pikater.core.ontology.subtrees.result.SaveResults;
import org.pikater.core.ontology.subtrees.task.Task;

public class DataManagerService extends FIPAService {

	static final Codec codec = new SLCodec();

	/*
	 * Sends the request to userID to the Agent_DataManger Returns User-ID
	 */
	public static int getUserID(PikaterAgent agent, String login) {

		GetUserID getUserID = new GetUserID();
		getUserID.setLogin(login);

		Ontology ontology = AccountOntology.getInstance();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		msg.setLanguage(new SLCodec().getName());
		msg.setOntology(ontology.getName());

		Action a = new Action();
		a.setAction(getUserID);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logError(ce.getMessage());
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage());
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logError(e.getMessage());
		}

		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logError(e1.getMessage());
		} catch (CodecException e1) {
			agent.logError(e1.getMessage());
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage());
		}

		if (content instanceof Result) {
			Result result = (Result) content;

			int userID = Integer.parseInt((String) result.getValue());
			agent.log("Recieved ID " + userID);

			return userID;
		} else {
			agent.logError("No Result ontology");
		}

		return -1;
	}

	public static String translateInternalFilename(PikaterAgent agent, int user,
			String internalFilename) {
		return DataManagerService.translateFilename(agent, user,
				null, internalFilename);
	}
	public static String translateExternalFilename(PikaterAgent agent, int user,
			String externalFilename) {
		return DataManagerService.translateFilename(agent, user,
				externalFilename, null);	
	}
	private static String translateFilename(PikaterAgent agent, int user,
			String externalFilename, String internalFilename) {

		TranslateFilename tf = new TranslateFilename();
		tf.setUserID(user);
		tf.setExternalFilename(externalFilename);
		tf.setInternalFilename(internalFilename);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(FilenameTranslationOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(tf);

		try {
			agent.getContentManager().fillContent(request, a);

			ACLMessage inform = FIPAService.doFipaRequestClient(agent, request);

			if (inform == null) {
				return null;
			}

			Result r = (Result) agent.getContentManager()
					.extractContent(inform);

			return (String) r.getValue();
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

		return null;
	}

	public static void saveAgentInfo(PikaterAgent agent, AgentInfo agentInfo) {

		SaveAgentInfo saveAgentInfo = new SaveAgentInfo();
		saveAgentInfo.setAgentInfo(agentInfo);

		AID receiver = new AID(AgentNames.DATA_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(receiver);
		request.setLanguage(agent.getCodec().getName());
		request.setOntology(ontology.getName());

		try {
			agent.getContentManager().fillContent(request,
					new Action(receiver, saveAgentInfo));
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		try {
			FIPAService.doFipaRequestClient(agent, request, 10000);
		} catch (FIPAException e) {
		}

	}

	public static void saveResult(PikaterAgent agent, Task task,
			int experimentID) {

		SaveResults saveResults = new SaveResults();
		saveResults.setTask(task);
		saveResults.setExperimentID(experimentID);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(ResultOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(saveResults);

		try {
			agent.getContentManager().fillContent(request, a);

			FIPAService.doFipaRequestClient(agent, request);

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

	}

	/**
	 * Makes sure the file is present in the cache or gets it from the (remote)
	 * database.
	 */
	public static void ensureCached(PikaterAgent agent, String filename) {
		agent.log("making sure file " + filename + " is present");
		if (new File(CoreConfiguration.DATA_FILES_PATH + filename).exists())
			return;
		agent.log("getting file " + filename + " from dataManager");
		GetFile gf = new GetFile();
		gf.setHash(filename);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(DataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		try {
			agent.getContentManager().fillContent(request,
					new Action(agent.getAID(), gf));
			FIPAService.doFipaRequestClient(agent, request);
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
	}

	public static Model getModel(PikaterAgent agent, int model) {
		agent.log("getting model " + model + " from DataManager");
		GetModel act = new GetModel();
		act.setModelID(model);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(ModelOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		try {
			agent.getContentManager().fillContent(request,
					new Action(agent.getAID(), act));
			ACLMessage response = FIPAService.doFipaRequestClient(agent,
					request, 10000);
			if (response == null) {
				agent.logError("did not receive GetModel response for model "
						+ model + " in time");
				return null;
			}

			ContentElement content = agent.getContentManager().extractContent(
					response);
			if (content instanceof Result) {
				Result result = (Result) content;

				if (result.getValue() instanceof Model) {
					return (Model) result.getValue();
				} else {
					agent.logError("did not receive expected GetModel response for model "
							+ model);
				}
			}
		} catch (CodecException e) {
			agent.logError("GetModel failure", e);
		} catch (OntologyException e) {
			agent.logError("GetModel failure", e);
		} catch (FIPAException e) {
			agent.logError("GetModel failure", e);
		}
		return null;
	}

	public static Models getAllModels(PikaterAgent agent) {

		AID receiver = new AID(AgentNames.DATA_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setSender(agent.getAID());
		request.addReceiver(receiver);
		request.setLanguage(agent.getCodec().getName());
		request.setOntology(ontology.getName());

		try {
			agent.getContentManager().fillContent(request,
					new Action(receiver, new GetModels()));
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, request, 10000);

		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

		Models models = null;
		try {
			Result r = (Result) agent.getContentManager().extractContent(reply);

			models = (Models) r.getValue();

		} catch (UngroundedException e) {
			agent.logError(e.getMessage(), e);
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		return models;
	}

	public static void saveMetadata(PikaterAgent agent, Metadata m) {
		SaveMetadata saveMetadata = new SaveMetadata();
		saveMetadata.setMetadata(m);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(MetadataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(saveMetadata);

		try {
			agent.getContentManager().fillContent(request, a);

			FIPAService.doFipaRequestClient(agent, request);

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
	}

	public static Metadatas getAllMetadata(PikaterAgent agent, GetAllMetadata gm) {

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(MetadataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(gm);

		try {
			agent.getContentManager().fillContent(request, a);
			ACLMessage inform = FIPAService.doFipaRequestClient(agent, request);

			Result r = (Result) agent.getContentManager()
					.extractContent(inform);
			Metadatas allMetadata = (Metadatas) r.getValue();
			return allMetadata;

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
		return null;
	}

	public static Metadata getMetadata(PikaterAgent agent, GetMetadata gm) {

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(MetadataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(gm);

		try {
			agent.getContentManager().fillContent(request, a);
			ACLMessage inform = FIPAService.doFipaRequestClient(agent, request);

			Result r = (Result) agent.getContentManager()
					.extractContent(inform);
			Metadata metadata = (Metadata) r.getValue();
			return metadata;

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
		return null;
	}

	public static Agents getNBestAgents(PikaterAgent agent, String fileName,
			int count) {

		GetMultipleBestAgents g = new GetMultipleBestAgents();
		g.setNearestInternalFileName(fileName);
		g.setNumberOfAgents(count);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(RecommendOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(g);

		try {
			agent.getContentManager().fillContent(request, a);
			ACLMessage inform = FIPAService.doFipaRequestClient(agent, request);

			if (inform.getPerformative() == ACLMessage.FAILURE) {
				return null;
			}

			Result r = (Result) agent.getContentManager()
					.extractContent(inform);
			Agents bestAgentCandidates = (Agents) r.getValue();
			return bestAgentCandidates;

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
		return null;
	}

	public static Agent getTheBestAgent(PikaterAgent agent, String fileName) {
		Agents agents = DataManagerService.getNBestAgents(agent, fileName, 1);
		if ((agents != null) && (agents.getAgents().size() > 0)) {
			return agents.getAgents().get(0);
		} else {
			return null;
		}
	}

	public static void getExternalAgent(PikaterAgent agent, String type) {
		agent.log("getting jar for type " + type + " from dataManager");
		GetExternalAgentJar act = new GetExternalAgentJar();
		act.setType(type);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(DataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		try {
			agent.getContentManager().fillContent(request,
					new Action(agent.getAID(), act));
			FIPAService.doFipaRequestClient(agent, request);
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
	}

	public static AgentInfo getAgentInfo(PikaterAgent agent,
			String agentClassName) {

		if (agent == null) {
			throw new IllegalArgumentException("Argument agent can't be null");
		}

		AID receiver = new AID(AgentNames.DATA_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();

		ACLMessage getAgentInfomsg = new ACLMessage(ACLMessage.REQUEST);
		getAgentInfomsg.addReceiver(receiver);
		getAgentInfomsg.setSender(agent.getAID());
		getAgentInfomsg.setLanguage(agent.getCodec().getName());
		getAgentInfomsg.setOntology(ontology.getName());

		GetAgentInfo getAgentInfo = new GetAgentInfo();
		getAgentInfo.setAgentClassName(agentClassName);

		Action action = new Action(agent.getAID(), getAgentInfo);

		try {
			agent.getContentManager().fillContent(getAgentInfomsg, action);
			ACLMessage agentInfoMsg = FIPAService.doFipaRequestClient(agent,
					getAgentInfomsg);

			Result replyResult = (Result) agent.getContentManager()
					.extractContent(agentInfoMsg);

			AgentInfo agentInfo = (AgentInfo) replyResult.getValue();

			return agentInfo;

		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		} catch (Codec.CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		return null;
	}

	public static AgentInfos getAgentInfos(PikaterAgent agent) {

		if (agent == null) {
			throw new IllegalArgumentException("Argument agent can't be null");
		}

		AID receiver = new AID(AgentNames.DATA_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();

		ACLMessage getAgentInfomsg = new ACLMessage(ACLMessage.REQUEST);
		getAgentInfomsg.addReceiver(receiver);
		getAgentInfomsg.setSender(agent.getAID());
		getAgentInfomsg.setLanguage(agent.getCodec().getName());
		getAgentInfomsg.setOntology(ontology.getName());

		GetAgentInfos getAgentInfos = new GetAgentInfos();

		Action action = new Action(agent.getAID(), getAgentInfos);

		try {
			agent.getContentManager().fillContent(getAgentInfomsg, action);
			ACLMessage agentInfoMsg = FIPAService.doFipaRequestClient(agent,
					getAgentInfomsg);

			Result replyResult = (Result) agent.getContentManager()
					.extractContent(agentInfoMsg);

			AgentInfos agentInfos = (AgentInfos) replyResult.getValue();

			return agentInfos;

		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		} catch (Codec.CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		return null;
	}

	public static ExternalAgentNames getExternalAgentNames(PikaterAgent agent) {

		AID receiver = new AID(AgentNames.DATA_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();

		ACLMessage getAgentInfomsg = new ACLMessage(ACLMessage.REQUEST);
		getAgentInfomsg.addReceiver(receiver);
		getAgentInfomsg.setSender(agent.getAID());
		getAgentInfomsg.setLanguage(agent.getCodec().getName());
		getAgentInfomsg.setOntology(ontology.getName());

		GetExternalAgentNames getExternalAgentNames = new GetExternalAgentNames();

		Action action = new Action(agent.getAID(), getExternalAgentNames);

		try {
			agent.getContentManager().fillContent(getAgentInfomsg, action);
			ACLMessage agentInfoMsg = FIPAService.doFipaRequestClient(agent,
					getAgentInfomsg);

			Result replyResult = (Result) agent.getContentManager()
					.extractContent(agentInfoMsg);

			ExternalAgentNames externalAgentNames = (ExternalAgentNames) replyResult
					.getValue();

			return externalAgentNames;

		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		} catch (Codec.CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		return null;
	}

	/*
	 * Sends to save the Batch to the Agent_DataManger Returns Batch-ID
	 */
	public static int saveBatch(PikaterAgent agent, Batch batch) {

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
			agent.logError(ce.getMessage());
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage());
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logError(e.getMessage());
		}

		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (CodecException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;

			SavedBatch savedBatch = (SavedBatch) result.getValue();
			agent.log(savedBatch.getMessage());

			return savedBatch.getSavedBatchId();
		} else {
			agent.logError("No Result ontology");
		}

		return -1;
	}

	public static Batch loadBatch(PikaterAgent agent, int batchID) {

		LoadBatch loadBatch = new LoadBatch();
		loadBatch.setBatchID(batchID);

		Ontology ontology = BatchOntology.getInstance();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(ontology.getName());
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(loadBatch);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logError(ce.getMessage(), ce);
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage(), oe);
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (CodecException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;

			Batch savedBatch = (Batch) result.getValue();
			return savedBatch;
		} else {
			agent.logError("No Result ontology");
		}
		return null;
	}

	public static void updateBatchStatus(PikaterAgent agent, int batchID,
			String batchStatus) {

		UpdateBatchStatus updateBatchStatus = new UpdateBatchStatus();
		updateBatchStatus.setBatchID(batchID);
		updateBatchStatus.setStatus(batchStatus);

		Ontology ontology = BatchOntology.getInstance();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(ontology.getName());
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(updateBatchStatus);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logError(ce.getMessage(), ce);
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage(), oe);
		}

		@SuppressWarnings("unused")
		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

	}

	public static int getBatchPriority(PikaterAgent agent, int batchID) {
		
		GetBatchPriority getBatchPriority = new GetBatchPriority();
		getBatchPriority.setBatchID(batchID);
		
		Ontology ontology = BatchOntology.getInstance();
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(ontology.getName());
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(getBatchPriority);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logError(ce.getMessage(), ce);
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage(), oe);
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (CodecException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;

			long totalPriorityLong = (long) result.getValue();
			int totalPriority = (int) totalPriorityLong;
			return totalPriority;
		} else {
			agent.logError("No Result ontology");
		}

		return -1;
	}
	
	/*
	 * Sends to save the Experiment to the Agent_DataManger Returns
	 * Experiment-ID
	 */
	public static int saveExperiment(PikaterAgent agent, Experiment experiment) {

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
			agent.logError(ce.getMessage());
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage());
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.log(e.getMessage());
		}

		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (CodecException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;

			SavedExperiment savedExperiment = (SavedExperiment) result
					.getValue();
			agent.log(savedExperiment.getMessage());

			return savedExperiment.getSavedExperimentId();
		} else {
			agent.logError("Error - No Result ontology");
		}

		return -1;
	}

	public static void updateExperimentStatus(PikaterAgent agent,
			int experimentID, String experimentStatus) {
	}

	public static User loadUser(PikaterAgent agent, int userID) {

		GetUser getUser = new GetUser();
		getUser.setUserID(userID);

		Ontology ontology = AccountOntology.getInstance();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(agent.getAID());
		msg.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(ontology.getName());
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));

		Action a = new Action();
		a.setAction(getUser);
		a.setActor(agent.getAID());

		try {
			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msg, a);

		} catch (CodecException ce) {
			agent.logError(ce.getMessage(), ce);
		} catch (OntologyException oe) {
			agent.logError(oe.getMessage(), oe);
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);
		} catch (FIPAException e) {
			agent.logError(e.getMessage());
		}

		ContentElement content = null;
		try {
			content = agent.getContentManager().extractContent(reply);
		} catch (UngroundedException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (CodecException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage(), e1);
		}

		if (content instanceof Result) {
			Result result = (Result) content;

			User user = (User) result.getValue();
			return user;
		} else {
			agent.logError("No Result ontology");
		}
		return null;
	}

	@Deprecated
	public static jade.util.leap.ArrayList getFilesInfo(PikaterAgent agent,
			GetFileInfo gfi) {

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(DataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(gfi);

		try {
			agent.getContentManager().fillContent(request, a);

			ACLMessage inform = FIPAService.doFipaRequestClient(agent, request);

			if (inform == null) {
				return new jade.util.leap.ArrayList();
			}

			Result r = (Result) agent.getContentManager()
					.extractContent(inform);

			return (jade.util.leap.ArrayList) r.getValue();
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

		return null;

	}

	@Deprecated
	public static void updateMetadata(PikaterAgent agent, Metadata m) {
		UpdateMetadata updateMetadata = new UpdateMetadata();
		updateMetadata.setMetadata(m);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(MetadataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(updateMetadata);

		try {
			agent.getContentManager().fillContent(request, a);

			FIPAService.doFipaRequestClient(agent, request);

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
	}

	@Deprecated
	public static void deleteTempFiles(PikaterAgent agent) {

		DeleteTempFiles dtf = new DeleteTempFiles();

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(DataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(dtf);

		try {
			agent.getContentManager().fillContent(request, a);

			FIPAService.doFipaRequestClient(agent, request);

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

	}

	@Deprecated
	public static jade.util.leap.ArrayList getFiles(PikaterAgent agent,
			int userID) {
		GetFiles gfi = new GetFiles();
		gfi.setUserID(userID);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(DataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(gfi);

		try {
			agent.getContentManager().fillContent(request, a);

			ACLMessage inform = FIPAService.doFipaRequestClient(agent, request);

			if (inform == null) {
				return null;
			}

			Result r = (Result) agent.getContentManager()
					.extractContent(inform);

			return (jade.util.leap.ArrayList) r.getValue();
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

		return null;

	}

	@Deprecated
	public static String importFile(PikaterAgent agent, int userID,
			String path, String content, boolean temp) {

		ImportFile im = new ImportFile();
		im.setUserID(userID);
		im.setExternalFilename(path);
		im.setFileContent(content);
		im.setTempFile(temp);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(DataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(im);

		try {
			agent.getContentManager().fillContent(request, a);
		} catch (CodecException e1) {
			agent.logError(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage(), e1);
		}

		try {
			return FIPAService.doFipaRequestClient(agent, request).getContent();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		}

		return null;
	}

	@Deprecated
	public static String importFile(PikaterAgent agent, int userID,
			String path, String content) {
		return importFile(agent, userID, path, content, false);
	}

}