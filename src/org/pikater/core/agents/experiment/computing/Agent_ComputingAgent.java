package org.pikater.core.agents.experiment.computing;

import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetResponder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing;
import org.pikater.core.agents.experiment.dataprocessing.communicator.ComputingAction;
import org.pikater.core.agents.experiment.dataprocessing.communicator.ComputingCommunicator;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.DurationOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batchdescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.option.GetOptions;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

import weka.core.Instances;

/**
 * 
 * Abstract class that gives interface and skeleton of implementation
 * for all another Computing agents
 *
 */
public abstract class Agent_ComputingAgent extends Agent_DataProcessing {

	private static final long serialVersionUID = -7927583436579620995L;

	public enum States {
		NEW, TRAINED
	}

	private static final String CLASS_NAME = "className";

	// common properties for all computing agents
	public String trainFileName;
	public String testFileName;
	public String labelFileName = "";

	public States state = States.NEW;
	public boolean hasGotRightData = false;

	public Agent agentOptions = null;

	// data read from fileName file
	protected Instances data;
	public Instances train;
	public DataInstances ontoTrain;
	public Instances test;
	public DataInstances ontoTest;

	public Instances label;
	public DataInstances ontoLabel;

	protected String[] options;
	public Task currentTask = null;

	protected String className;

	protected Object[] args;

	public boolean working = false; // TODO -> state?

	public LinkedList<ACLMessage> taskFIFO = new LinkedList<ACLMessage>();

	public Behaviour executionBehaviour = null;

	private boolean newAgent = true;
	public boolean resurrected = false;
	
	protected long lastDuration;
	protected Date lastStartDate;

	public long getLastDuration() {
		return lastDuration;
	}

	public Date getLastStartDate() {
		return lastStartDate;
	}

	public abstract Date train(Evaluation evaluation) throws Exception;

	public abstract void evaluateCA(EvaluationMethod evaluation_method,
			Evaluation evaluation) throws Exception;

	public abstract DataInstances getPredictions(Instances test,
			DataInstances onto_test);

	public abstract String getAgentType();

	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(DurationOntology.getInstance());
		
		return ontologies;
	}

	/**
	 * Setup of agent
	 */
	@Override
	protected void setup() {
		super.setup();
		
        if (containsArgument(CLASS_NAME)) {
            className = getArgumentValue(CLASS_NAME);
        }
        if (isArgumentValueTrue("load")) {
        }

		// register with the DF
		if (! this.getAID().getLocalName().contains("Service")){	
			List<String> descr = new ArrayList<String>();
			descr.add(CoreAgents.COMPUTING_AGENT.getName());
			descr.add(getLocalName());
	
			// add fileName to service description
			String typeDesc;
			if (state == States.TRAINED) {
				typeDesc = getAgentType() + " trained on " + trainFileName;
			} else {
				typeDesc = getAgentType();
			}
			descr.add(typeDesc);
			
			registerWithDF(descr);
		}						

		if (!newAgent) {
			resurrected = true;
			logInfo("was resurrected");
			taskFIFO = new LinkedList<ACLMessage>();
			executionBehaviour.reset();
			state = States.TRAINED;
			return;
		}
		newAgent = false;

		MessageTemplate reqMsgTemplate = 
				MessageTemplate.and(
						MessageTemplate.MatchProtocol(
								FIPANames.InteractionProtocol.FIPA_REQUEST),
						MessageTemplate.and(
								MessageTemplate.MatchPerformative(
										ACLMessage.REQUEST),
								MessageTemplate.MatchOntology(
										TaskOntology.getInstance().getName())
						)
				);
		
		MessageTemplate cfpMsgTemplate = 
				MessageTemplate.and(
						MessageTemplate.MatchProtocol(
							FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
						MessageTemplate.and(
							MessageTemplate.MatchPerformative(ACLMessage.CFP),
							MessageTemplate.MatchOntology(
									TaskOntology.getInstance().getName())
						)
				);
	
		executionBehaviour = new ComputingAction(this);

		addBehaviour(new RequestServer(this, reqMsgTemplate));
		addBehaviour(new CFPResponder(this, cfpMsgTemplate));
		addBehaviour(executionBehaviour);
		
	}

	/**
	 * Get parameters
	 */
	protected void getParameters() {
	}

	/**
	 * Set options
	 */
	public boolean setOptions(Task task) {
		
		NewOptions taskOptions = new NewOptions(task.getAgent().getOptions());
		taskOptions.removeOptionByName(CoreConstant.Mode.DEFAULT.name());
		taskOptions.removeOptionByName(CoreConstant.Output.DEFAULT.name());
		
		String wekaOptionsString = 
				taskOptions.exportToWeka();
		
		options = wekaOptionsString.split("[ ]+");
		currentTask = task;

		return true;
	}

	/**
	 * Get options
	 */
	public String getOptions() {

		String strOPTIONS = "";
		strOPTIONS += "OPTIONS:";
		for (int i = 0; i < options.length; i++) {
			strOPTIONS += " " + options[i];
		}
		return strOPTIONS;
	}

	/**
	 * Will we accept or refuse the request? (working,
	 * size of taksFIFO, latency time...)
	 */
	public boolean acceptTask() {
		return true;
	}
	
	protected class RequestServer extends AchieveREResponder {
		
		public RequestServer(Agent_ComputingAgent agent,
				MessageTemplate msgTemplate) {
			super(agent, msgTemplate);
			
		}

		private static final long serialVersionUID = 1074564968341084444L;

		@Override
		protected ACLMessage handleRequest(ACLMessage request
				) throws NotUnderstoodException, RefuseException {
			
			ACLMessage resultMsg = request.createReply();
			try {
				Action action = (Action)
						getContentManager().extractContent(request);
					
					if (action.getAction() instanceof GetOptions) {
						return respondToGetOptions(request, action);

					} else if (action.getAction() instanceof ExecuteTask) {
						return respondExecuteTask(request, action);
					}

					resultMsg.setPerformative(ACLMessage.NOT_UNDERSTOOD);

			} catch (CodecException ce) {
				Agent_ComputingAgent.this.logException(ce.getMessage(), ce);
			} catch (OntologyException oe) {
				Agent_ComputingAgent.this.logException(oe.getMessage(), oe);
			}
			
			return resultMsg;
		}

	}
	
	private ACLMessage respondToGetOptions(ACLMessage request, Concept concept) {
		
		return ComputingCommunicator.sendOptions(
				Agent_ComputingAgent.this, request);
	}
	
	private ACLMessage respondExecuteTask(ACLMessage request, Action a) {
		
		return ComputingCommunicator.handleTask(this, request);
	}

	protected class CFPResponder extends ContractNetResponder {
		private static final long serialVersionUID = -7855318009388214053L;

		public CFPResponder(jade.core.Agent agent,
				MessageTemplate messageTemplate) {
			
			super(agent, messageTemplate);
		}

		@Override
		protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException,
				FailureException, NotUnderstoodException {
			
			Agent_ComputingAgent agent =
					(Agent_ComputingAgent)this.getAgent();
			
			return ComputingCommunicator.executeDurationTask(agent, cfp);
		}
		
	}
	
	public byte[] getAgentObject() throws IOException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		oos.flush();
		oos.close();

		return bos.toByteArray();
	}

	public String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSSSSS");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public void logStartTask() {
	}
	
	public void logOptions() {
		logInfo(getOptions());
	}
	
	public void logFinishedTask() {
		logInfo("CA terminating");
		terminate();
	}
	
	
}

