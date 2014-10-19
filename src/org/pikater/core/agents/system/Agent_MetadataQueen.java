package org.pikater.core.agents.system;

import jade.content.AgentAction;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.metadata.MetadataReader;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.subtrees.agent.AgentClass;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.NewComputedData;
import org.pikater.core.ontology.subtrees.metadata.NewDataset;
import org.pikater.core.ontology.subtrees.metadata.SaveMetadata;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectReader;

import weka.core.Instances;

import com.google.common.io.Files;

/**
 * The main task of this agent is computing the metadata 
 * information for datasets, that have been newly added 
 * to the system.
 * 
 * It also utilizes the corresponding functions
 * of {@link Agent_DataManager}.
 *
 * @see Agent_DataManager
 *
 */
public class Agent_MetadataQueen extends PikaterAgent {
	
	private static final long serialVersionUID = -1886699589066832983L;

	/**
	 * Get {@link List<Ontology>} that utilizes this agent
	 * 
	 * @param agentClasses list of {@link AgentClass}
	 *        represents {@link PikaterAgent} to wake
	 */
	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(MetadataOntology.getInstance());
		return ontologies;
	}

	/**
	 * Sets up the agent during the startup. The most crucial
	 * part of this function is when the corresponding actions
	 * are set.
	 */
	@Override
	protected void setup() {
		initDefault();

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mt) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
				try {
					Action action = (Action) getContentManager().extractContent(request);
					/*
					 * Action when metadata for user-added dataset were computed/
					 */
					if (action.getAction() instanceof NewDataset) {
						return respondToNewDataset(request, action);
					}
					/*
					 * Action when metadata were computed for such a dataset,
					 * that was added by an agent, thus it can be e.g. a trained
					 * dataset. 
					 */
					if (action.getAction() instanceof NewComputedData) {
						return respondToNewComputedData(request, action);
					}
				} catch (OntologyException e) {
					logException("Problem extracting content", e);
				} catch (CodecException e) {
					logException("Codec problem", e);
				} catch (Exception e) {
					logException("General Exception", e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logSevere("Failure responding to request: " + request.getContent());
				return failure;
			}

		});

	}

	/**
	 * Creates message for an event, when new message was received,
	 * requesting metadata computation for a user-added dataset.
	 * @param request {@link ACLMessage} received with the request.
	 * @param action requested {@link Action}
	 * @return {@link ACLMessage} responding to the request
	 */
	private ACLMessage respondToNewDataset(ACLMessage request, Action action) {
		NewDataset newDataset = (NewDataset) action.getAction();

		return respondToNewData(request, CoreConfiguration.getMetadataPath(), newDataset.getDataSetID());
	}

	/**
	 * Creates message for an event, when new message was received,
	 * requesting metadata computation for a agent-added dataset.
	 * @param request {@link ACLMessage} received with the request
	 * @param action requested {@link Action}
	 * @return {@link ACLMessage} responding to the request
	 */
	private ACLMessage respondToNewComputedData(ACLMessage request, Action action) {
		NewComputedData newComputedData = (NewComputedData) action.getAction();

		return respondToNewData(request, CoreConfiguration.getDataFilesPath(), newComputedData.getComputedDataID());
	}

	/**
	 * Retrieves the newly added dataset from the Database. After metadata creation ended, these data
	 * are sent to the {@link Agent_DataManager}, that stores them to the database.
	 * @param request {@link ACLMessage} received with the request
	 * @param path of the temporary location, where the dataset is stores while the computation is performed
	 * @param dataID ideintification of the dataset we want metadata computated
	 * @return
	 */
	private ACLMessage respondToNewData(ACLMessage request, String path, int dataID) {
		try {
			JPADataSetLO dslo = DAOs.dataSetDAO.getByID(dataID, EmptyResultAction.THROW);
			PGLargeObjectReader plor = PGLargeObjectReader.getForLargeObject(dslo.getOID());

			final String baseFilename = path + dslo.getHash();
			File file = new File(baseFilename);
			if (!file.exists()) {
				File tmp = new File(baseFilename + ".tmp");
				FileWriter fw = new FileWriter(tmp);

				char[] buf = new char[100];
				int s = -1;
				while ((s = plor.read(buf)) != -1) {
					fw.write(buf, 0, s);
				}
				fw.close();
				Files.move(tmp, file);
			}

			Metadata resultMetaData = readFile(file);

			sendSaveMetaDataRequest(resultMetaData, dataID);

			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);

			return reply;
		} catch (NoResultException e) {
			logException("DataSet with ID " + dataID + " not found  in the database", e);
		} catch (IOException e) {
			logException("IOError while accessing dataset", e);
		}

		return null;
	}

	private void sendSaveMetaDataRequest(Metadata metadata, int dataSetID) {
		AID receiver = new AID(CoreAgents.DATA_MANAGER.getName(), false);

		SaveMetadata saveMetaDataAction = new SaveMetadata();
		saveMetaDataAction.setMetadata(metadata);
		saveMetaDataAction.setDataSetID(dataSetID);

		try {
			ACLMessage request = makeActionRequest(receiver, saveMetaDataAction);
			logInfo("Sending SaveMetaDataAction to DataManager");
			ACLMessage reply = FIPAService.doFipaRequestClient(this, request);
			if (reply == null) {
				logSevere("Reply not received");
			} else {
				logInfo("Reply received: " + ACLMessage.getPerformative(reply.getPerformative()) + " " + reply.getContent());
			}
		} catch (OntologyException e) {
			logException("Ontology error occurred: ", e);
		} catch (CodecException e) {
			logException("Codec error occurred: ", e);
		} catch (FIPAException e) {
			logException("FIPA error occurred", e);
		}
	}

	/**
	 * Fills in a request with the Action, that is needed to be performed.
	 * @param target agent for the request
	 * @param action {@link AgentAction} to be performed
	 * @return created {@link ACLMessage} request
	 * @throws CodecException
	 * @throws OntologyException
	 */
	private ACLMessage makeActionRequest(AID target, AgentAction action) throws CodecException, OntologyException {
		Ontology ontology = MetadataOntology.getInstance();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(target);
		msg.setLanguage(getCodec().getName());
		msg.setOntology(ontology.getName());
		getContentManager().fillContent(msg, new Action(target, action));
		return msg;
	}

	/**
	 * Reads file to the {@link DataInstances} object, that can be used to compute
	 * the metadata.
	 * @param file {@link File} object of the temporary file representing tha dataset
	 * @return {@link Metadata} object
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Metadata readFile(File file) throws FileNotFoundException, IOException {
		DataInstances data = new DataInstances();
		data.fillWekaInstances(new Instances(new BufferedReader(new FileReader(file))));
		MetadataReader reader = new MetadataReader();
		return reader.computeMetadata(data);
	}

}
