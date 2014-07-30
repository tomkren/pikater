package org.pikater.core.agents.system.data;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import java.io.File;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.Agent_DataManager;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.ResultOntology;
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
import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.core.ontology.subtrees.recommend.GetMultipleBestAgents;
import org.pikater.core.ontology.subtrees.result.SaveResults;
import org.pikater.core.ontology.subtrees.task.Task;

public class DataManagerService extends FIPAService {

	static final Codec codec = new SLCodec();

	public static String translateFilename(PikaterAgent agent, int user,
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
		if (new File(Agent_DataManager.dataFilesPath + filename).exists())
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
		} catch (CodecException | OntologyException | FIPAException e) {
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
			agent.getContentManager().fillContent(request, new Action(agent.getAID(), act));
			ACLMessage response = FIPAService.doFipaRequestClient(agent, request, 10000);
			if (response == null) {
				agent.logError("did not receive GetModel response for model "+model+" in time");
				return null;
			}

			ContentElement content = agent.getContentManager().extractContent(response);
			if (content instanceof Result) {
				Result result = (Result) content;

				if (result.getValue() instanceof Model) {
					return (Model)result.getValue();
				} else {
					agent.logError("did not receive expected GetModel response for model "+model);
				}
			}
		} catch (CodecException | OntologyException | FIPAException e) {
			agent.logError("GetModel failure", e);
		}
		return null;
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
	
	public static Agents getNBestAgents(PikaterAgent agent, String fileName,int count){

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
		Agents agents=DataManagerService.getNBestAgents(agent, fileName, 1);
		if((agents!=null)&&(agents.getAgents().size()>0)){
			return agents.getAgents().get(0);
		}else{
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
		} catch (CodecException | OntologyException | FIPAException e) {
			agent.logError(e.getMessage(), e);
		}
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