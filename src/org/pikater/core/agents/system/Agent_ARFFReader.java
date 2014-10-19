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
import jade.wrapper.AgentController;

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.subtrees.data.GetData;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;

import weka.core.Attribute;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Agent reads Data-Sets from ARFF file
 *
 */
public class Agent_ARFFReader extends PikaterAgent {
	
	private static final long serialVersionUID = 7116837600070711675L;
	
	// data read from file
	protected Instances data;

	/**
	 * Get ontologies which is using this agent
	 */
	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(DataOntology.getInstance());
		
		return ontologies;
	}
	
	/**
	 * Reads the Data-Set from file
	 * 
	 */
	boolean ReadFromFile(String relativeFileName) {
		
		if (relativeFileName == null || relativeFileName.length() == 0) {
			return false;
		}
		
		String path = CoreConfiguration.getDataFilesPath() + relativeFileName;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			data = new Instances(reader);
			reader.close();

		} catch (IOException e) {
			logException("Reading of data from file " + path + " failed.", e);
			return false;
		}
		logInfo("Reading of data from file " + path + " succesful.");
		return true;
	}

	/**
	 * Get type of agent
	 */
	@Override
	protected String getAgentType() {
		return CoreAgents.ARFF_READER.getName();
	}

	/**
	 * Agent setup
	 */
	@Override
	protected void setup() {
		initDefault();
		registerWithDF();
		getContentManager().registerOntology(DataOntology.getInstance());

		
		MessageTemplate template =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		addBehaviour(new GetDataResponder(this, template));
	}

	/**
	 * Sends data
	 * 
	 * @param request - ontology {@link GetData} 
	 * @return - message with data
	 */
	protected ACLMessage sendData(ACLMessage request) {
		
		// responding message
		ACLMessage msgOut = request.createReply();
		msgOut.setPerformative(ACLMessage.INFORM);
		
		try {
			ContentElement content =
					getContentManager().extractContent(request);
			GetData getData = (GetData) ((Action) content).getAction();
			
			String fileName = getData.getFileName();
			
			int slashIndex = Math.max(
					fileName.lastIndexOf("/"),
					fileName.lastIndexOf("\\"));
			int lastSlashIndex = Math.max(slashIndex, 0);
			
			// trim to relative
			fileName = fileName.substring(lastSlashIndex);
			DataManagerService.ensureCached(this, fileName);
			DataInstances instances = new DataInstances();
			
			// Read the file
			boolean file_read = ReadFromFile(fileName);
			if (!file_read) {
				throw new FailureException(
						"File hasn't been read. Wrong file-name?");
			}

			instances.fillWekaInstances(data);
			ArrayList<Integer> types = new ArrayList<Integer>();

			for (int i = 0; i < data.numAttributes(); i++) {
				Attribute attributeI = data.attribute(i);

				int index;
				if (data.classIndex() >= 0) {
					index = data.classIndex();
				} else {
					index = data.numAttributes() - 1;
				}
				
				if ( (i != index) && (!types.contains(attributeI.type())) ) {
					types.add(attributeI.type());
				}
			}

			Result result;
			Action action = (Action) content;
			if (getData.getO2aAgent() != null) {
				
				AgentController agentController =
						getContainerController().getAgent(getData.getO2aAgent());
				agentController.putO2AObject(instances, false);
				
				result = new Result(action, true);
			} else {
				result = new Result(action, instances);
			}

			try {
				getContentManager().fillContent(msgOut, result);
			} catch (CodecException ce) {
				logException(ce.getMessage(), ce);
			} catch (OntologyException oe) {
				logException(oe.getMessage(), oe);
			}
		} catch (Exception e) {
			logException("Unexpected error message:", e);
		}
		return msgOut;
	}

	/**
	 * 
	 * Behavior to manage a request {@link GetData}
	 *
	 */
	private class GetDataResponder extends AchieveREResponder {
		private static final long serialVersionUID = 4340928332826216394L;

		private PikaterAgent agent;
				
		/**
		 * Constructor
		 * 
		 */
		public GetDataResponder(PikaterAgent agent,
				MessageTemplate messageTemplate) {
			super(agent, messageTemplate);
			this.agent = agent;
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) {
			ACLMessage agree = request.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
		}

		@Override
		protected ACLMessage prepareResultNotification(
				ACLMessage request, ACLMessage response) {
			try {
				ContentElement content =
						getContentManager().extractContent(request);
				// GetData
				if (((Action) content).getAction() instanceof GetData) {
					return sendData(request);
				}
			} catch (CodecException ce) {
				agent.logException(ce.getMessage(), ce);
			} catch (OntologyException oe) {
				agent.logException(oe.getMessage(), oe);
			}

			ACLMessage notUnderstood = request.createReply();
			notUnderstood.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			return notUnderstood;
		}
	}

}
