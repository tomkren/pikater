package org.pikater.core.agents.system;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.AgentNames;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.subtrees.data.GetData;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;

import weka.core.Attribute;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Agent_ARFFReader extends PikaterAgent {
	private static final long serialVersionUID = 7116837600070711675L;
	// data read from file
	protected Instances data;

	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(DataOntology.getInstance());
		
		return ontologies;
	}
	
	boolean ReadFromFile(String relativeFileName) {
		if (relativeFileName == null || relativeFileName.length() == 0) {
			return false;
		}
		
		String path = CoreConfiguration.DATA_FILES_PATH + relativeFileName;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			data = new Instances(reader);
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
			logError("Reading of data from file " + path + " failed.");
			return false;
		}
		log("Reading of data from file " + path + " succesful.");
		return true;
	}

	@Override
	protected String getAgentType() {
		return AgentNames.ARFF_READER;
	}

	@Override
	protected void setup() {
		initDefault();
		registerWithDF();
		getContentManager().registerOntology(DataOntology.getInstance());

		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		addBehaviour(new GetDataResponder(this, template));
	} // end Setup

	protected ACLMessage sendData(ACLMessage request) {
		// responding message
		ACLMessage msgOut = request.createReply();
		msgOut.setPerformative(ACLMessage.INFORM);
		try {
			ContentElement content = getContentManager().extractContent(request);
			GetData gd = (GetData) ((Action) content).getAction();
			String file_name = gd.getFileName();
			int lastSlash = Math.max(Math.max(file_name.lastIndexOf("/"), file_name.lastIndexOf("\\")), 0);
			file_name = file_name.substring(lastSlash); // trim to relative
			DataManagerService.ensureCached(this, file_name);
			DataInstances instances = new DataInstances();
			// Read the file
			boolean file_read = ReadFromFile(file_name);
			if (!file_read) {
				throw new FailureException("File hasn't been read. Wrong file-name?");
			}

			instances.fillWekaInstances(data);
			ArrayList<Integer> types = new ArrayList<Integer>();

			for (int i = 0; i < data.numAttributes(); i++) {
				Attribute a = data.attribute(i);
				//AttributeStats as = data.attributeStats(i);

				if (i != (data.classIndex() >= 0 ? data.classIndex() : data.numAttributes() - 1)) {
					if (!types.contains(a.type())) {
						types.add(a.type());
					}
				}
			}

			Result result;
			if (gd.getO2aAgent() != null) {
				// log("putting o2a data to "+gd.getO2a_agent());
				getContainerController().getAgent(gd.getO2aAgent()).putO2AObject(instances, false);
				result = new Result((Action) content, true);
			} else {
				result = new Result((Action) content, instances);
			}

			try {
				getContentManager().fillContent(msgOut, result);
			} catch (CodecException ce) {
				logError(ce.getMessage(), ce);
			} catch (OntologyException oe) {
				logError(oe.getMessage(), oe);
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
		}
		return msgOut;
	} // end SendData

	private class GetDataResponder extends AchieveREResponder {
		private static final long serialVersionUID = 4340928332826216394L;

		private PikaterAgent agent;
		
		public GetDataResponder(PikaterAgent agent, MessageTemplate mt) {
			super(agent, mt);
			this.agent = agent;
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) {
			ACLMessage agree = request.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
		} // end prepareResponse

		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
			try {
				ContentElement content = getContentManager().extractContent(request);
				// GetData
				if (((Action) content).getAction() instanceof GetData) {
					return sendData(request);
				}
			} catch (CodecException ce) {
				agent.logError(ce.getMessage(), ce);
			} catch (OntologyException oe) {
				agent.logError(oe.getMessage(), oe);
			}

			ACLMessage notUnderstood = request.createReply();
			notUnderstood.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			return notUnderstood;
		} // end prepareResultNotification
	}

}