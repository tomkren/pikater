package org.pikater.core.agents.experiment.dataprocessing;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.pikater.core.AgentNames;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.agents.system.Agent_DataManager;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.subtrees.data.GetData;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

import com.google.common.io.Files;

/** Base class for agents doing dataset manipulation (reading and/or writing) - i.e. Agent_ComputingAgent and Agent_DataProcessingAgent */
abstract public class Agent_DataProcessing extends Agent_AbstractExperiment {
	private static final long serialVersionUID = -1350928536579781414L;
	
	protected void setup() {
		super.setup();
		initDefault();
		setEnabledO2ACommunication(true, 0);
	}
	
	public String saveArff(Instances i) {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(i);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			saver.setDestination(out);
			saver.writeBatch();
			byte[] bout = out.toByteArray();
			String md5 = DigestUtils.md5Hex(bout);
			Files.write(bout, new File(Agent_DataManager.dataFilesPath + md5));
			return md5;
		} catch (IOException e) {
			logError("Failed to write results", e);
			return null;
		}
	}

	/* Extract data from INFORM message (ARFF reader) */
	public DataInstances processGetDataResponse(ACLMessage inform) {
		ContentElement content;
		try {
			content = getContentManager().extractContent(inform);
			if (content instanceof Result) {
				Result result = (Result) content;

				if (result.getValue() instanceof DataInstances) {
					return (DataInstances) result.getValue();
				} else if (result.getValue() instanceof Boolean) {

					Object dataInstance = getO2AObject();
					if (dataInstance == null)
						throw new IllegalStateException(
								"received GetData response without o2a object in queue");
					else
						return (DataInstances) dataInstance;
				} else {
					throw new IllegalStateException(
							"received unexpected Inform");
				}
			}
		} catch (UngroundedException e) {
			logError("", e);
		} catch (CodecException e) {
			logError("", e);
		} catch (OntologyException e) {
			logError("", e);
		}
		return null;
	}

	public ACLMessage makeGetDataRequest(String fileName) {
		AID[] ARFFReaders;
		AID reader = null;
		ACLMessage msgOut = null;
		// Make the list of reader agents
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentNames.ARFF_READER);
		template.addServices(sd);
		try {
			GetData getData = new GetData();

			DFAgentDescription[] result = DFService.search(this, template);

			ARFFReaders = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				if (isSameNode(result[i].getName())) {
					// prefer local reader for O2A transfer
					reader = result[i].getName();
					log("preferring reader " + reader.getName());
					getData.setO2aAgent(getLocalName());
					break;
				}
				ARFFReaders[i] = result[i].getName();
			}

			// randomly choose one of the readers if none preferred
			if (reader == null) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(result.length);
				reader = ARFFReaders[randomInt];
			}

			log("using reader " + reader + ", filename: " + fileName);

			Ontology ontology = DataOntology.getInstance();

			// request
			msgOut = new ACLMessage(ACLMessage.REQUEST);
			msgOut.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msgOut.setLanguage(getCodec().getName());
			msgOut.setOntology(ontology.getName());
			msgOut.addReceiver(reader);
			// content
			getData.setFileName(fileName);
			Action a = new Action();
			a.setAction(getData);
			a.setActor(getAID());
			getContentManager().fillContent(msgOut, a);

		} catch (FIPAException fe) {
			logError("", fe);
			return null;
		} catch (CodecException e) {
			logError("", e);
			return null;
		} catch (OntologyException e) {
			logError("", e);
			return null;
		}
		return msgOut;
	}
}
