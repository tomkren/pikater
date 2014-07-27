package org.pikater.core.agents.system.data;

import java.io.File;

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
import jade.util.leap.ArrayList;
import jade.util.leap.List;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.Agent_DataManager;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.FilenameTranslationOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.RecommendOntology;
import org.pikater.core.ontology.ResultOntology;
import org.pikater.core.ontology.subtrees.externalAgent.GetExternalAgentJar;
import org.pikater.core.ontology.subtrees.file.DeleteTempFiles;
import org.pikater.core.ontology.subtrees.file.GetFile;
import org.pikater.core.ontology.subtrees.file.GetFileInfo;
import org.pikater.core.ontology.subtrees.file.GetFiles;
import org.pikater.core.ontology.subtrees.file.ImportFile;
import org.pikater.core.ontology.subtrees.file.TranslateFilename;
import org.pikater.core.ontology.subtrees.management.GetTheBestAgent;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.SaveMetadata;
import org.pikater.core.ontology.subtrees.metadata.UpdateMetadata;
import org.pikater.core.ontology.subtrees.result.SaveResults;
import org.pikater.core.ontology.subtrees.task.Task;

public class DataManagerService extends FIPAService {

	static final Codec codec = new SLCodec();

       
        public static String importFile(PikaterAgent agent, int userID, String path, String content, boolean temp) {            

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
			e1.printStackTrace();
		} catch (OntologyException e1) {
			agent.logError(e1.getMessage(), e1);
			e1.printStackTrace();
		}

		try {
			return FIPAService.doFipaRequestClient(agent, request).getContent();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}

		return null;
		}


	public static String importFile(PikaterAgent agent, int userID, String path, String content) {
             return importFile(agent, userID, path, content, false);
	}

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
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}

		return null;
	}

	public static void saveResult(PikaterAgent agent, Task t) {

		SaveResults sr = new SaveResults();
		sr.setTask(t);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(ResultOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(sr);

		try {
			agent.getContentManager().fillContent(request, a);

			FIPAService.doFipaRequestClient(agent, request);

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}

	}
	
	/** Makes sure the file is present in the cache or gets it from the (remote) database. */
	public static void ensureCached(PikaterAgent agent, String filename) {
		agent.log("making sure file "+filename+" is present");
		if (new File(Agent_DataManager.dataFilesPath + filename).exists())
			return;
		agent.log("getting file "+filename+" from dataManager");
		GetFile gf = new GetFile();
		gf.setHash(filename);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(DataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		try {
			agent.getContentManager().fillContent(request, new Action(agent.getAID(), gf));
			FIPAService.doFipaRequestClient(agent, request);
		} catch (CodecException | OntologyException | FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}
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
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public static List getAllMetadata(PikaterAgent agent, GetAllMetadata gm) {

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
			List allMetadata = (List) r.getValue();
			return allMetadata;

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
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

			Result r = (Result) agent.getContentManager().extractContent(inform);
			Metadata metadata = (Metadata)r.getValue();
			return metadata;

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}
	
        public static org.pikater.core.ontology.subtrees.management.Agent getTheBestAgent(PikaterAgent agent, String fileName) {
            return (org.pikater.core.ontology.subtrees.management.Agent) getTheBestAgents(agent, fileName, 1).get(0);
        }
        
	public static List getTheBestAgents(PikaterAgent agent, String fileName, int number) {
		GetTheBestAgent g = new GetTheBestAgent();
		g.setNearest_file_name(fileName);
                g.setNumberOfAgents(number);

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

			Result r = (Result) agent.getContentManager().extractContent(inform);
			List bestAgents = (List) r.getValue();
			return bestAgents;

		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList getFilesInfo(PikaterAgent agent, GetFileInfo gfi) {

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
				return new ArrayList();
			}

			Result r = (Result) agent.getContentManager()
					.extractContent(inform);

			return (ArrayList) r.getValue();
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}

		return null;

	}

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
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}
	}

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
                e.printStackTrace();
            } catch (OntologyException e) {
    			agent.logError(e.getMessage(), e);
                e.printStackTrace();
            } catch (FIPAException e) {
    			agent.logError(e.getMessage(), e);
                e.printStackTrace();
            }

        }

	public static ArrayList getFiles(PikaterAgent agent, int userID) {
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

			return (ArrayList) r.getValue();
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}

		return null;

	}
	

	public static void getExternalAgent(PikaterAgent agent, String type) {
		agent.log("getting jar for type "+type+" from dataManager");
		GetExternalAgentJar act = new GetExternalAgentJar();
		act.setType(type);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.DATA_MANAGER, false));
		request.setOntology(DataOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		try {
			agent.getContentManager().fillContent(request, new Action(agent.getAID(), act));
			FIPAService.doFipaRequestClient(agent, request);
		} catch (CodecException | OntologyException | FIPAException e) {
			agent.logError(e.getMessage(), e);
			e.printStackTrace();
		}
	}
}