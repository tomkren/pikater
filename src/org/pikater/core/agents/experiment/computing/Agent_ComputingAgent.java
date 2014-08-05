package org.pikater.core.agents.experiment.computing;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing;
import org.pikater.core.agents.experiment.dataprocessing.communicator.ComputingAction;
import org.pikater.core.agents.experiment.dataprocessing.communicator.ComputingCommunicator;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.option.GetOptions;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

import weka.core.Instances;

public abstract class Agent_ComputingAgent extends Agent_DataProcessing {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7927583436579620995L;

	protected Codec codec = new SLCodec();

	public enum states {
		NEW, TRAINED
	}

	private final String CLASS_NAME = "className";

	/* common properties for all computing agents */
	public String trainFileName;
	public String testFileName;
	public String labelFileName = "";

	public states state = states.NEW;
	public boolean hasGotRightData = false;

	// protected Vector<MyWekaOption> Options;
	public Agent agentOptions = null;

	protected Instances data; // data read from fileName file
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

	public abstract Date train(Evaluation evaluation) throws Exception;

	public abstract void evaluateCA(EvaluationMethod evaluation_method,
			Evaluation evaluation) throws Exception;

	public abstract DataInstances getPredictions(Instances test,
			DataInstances onto_test);

	public abstract String getAgentType();

	@Override
	public java.util.List<Ontology> getOntologies() {

		java.util.List<Ontology> ontologies = new java.util.ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		
		return ontologies;
	}

	@Override
	protected void setup() {
		super.setup();
		
        if (containsArgument(CLASS_NAME)) {
            className = getArgumentValue(CLASS_NAME);
        }
        if (isArgumentValueTrue("load")) {
            // TODO loadAgent(getLocalName());
            // args = new String[0]; // arguments are empty
        }

		// register with the DF
		if (! this.getAID().getLocalName().contains("Service")){	
			List<String> descr = new ArrayList<String>();
			descr.add(AgentNames.COMPUTING_AGENT);
			descr.add(getLocalName());
	
			String typeDesc;
			if (state == states.TRAINED) { // add fileName to service description
				typeDesc = getAgentType() + " trained on " + trainFileName;
			} else {
				typeDesc = getAgentType();
			}
			descr.add(typeDesc);
			
			registerWithDF(descr);
		}						

		if (!newAgent) {
			resurrected = true;
			System.out.println(getLocalName() + " resurrected.");
			taskFIFO = new LinkedList<ACLMessage>();
			executionBehaviour.reset();
			state = states.TRAINED;
			return;
		}
		newAgent = false;

		MessageTemplate reqMsgTemplate = MessageTemplate
				.and(MessageTemplate
						.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
						MessageTemplate.and(MessageTemplate
								.MatchPerformative(ACLMessage.REQUEST),
								MessageTemplate.MatchOntology(
										TaskOntology.getInstance().getName())));

		addBehaviour(new RequestServer(this, reqMsgTemplate));
		addBehaviour(executionBehaviour = new ComputingAction(this));
		
	} // end setup

	protected void getParameters() {

		// AgentInfo agentInfo = getAgentInfo();
		// TODO: transformation from agentInfo to parameters + send to old
		// guiAgent
	}

	public boolean setOptions(Task task) {
		/*
		 * INPUT: task with weka options Fills the OPTIONS array and
		 * current_task.
		 */
		
		String wekaOptionsString = 
				NewOptions.exportToWeka(task.getAgent().getOptions());
		
		options = wekaOptionsString.split("[ ]+");
		currentTask = task;

		return true;
	}

	public String getOptions() {
		// write out OPTIONS

		String strOPTIONS = "";
		strOPTIONS += "OPTIONS:";
		for (int i = 0; i < options.length; i++) {
			strOPTIONS += " " + options[i];
		}
		return strOPTIONS;
	}

	// TODO: will we accept or refuse the request? (working, size of
	// taksFIFO, latency time...)
	public boolean acceptTask() {
		return true/* taskFIFO.size()<=MAX_TASKS */;
	}
	
	public static byte[] toBytes(Object object) throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);

		return baos.toByteArray();
	}

	protected class RequestServer extends AchieveREResponder {
		
		public RequestServer(Agent_ComputingAgent agent, MessageTemplate mt) {
			super(agent, mt);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1074564968341084444L;

		@Override
		protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
			
			ACLMessage resultMsg = request.createReply();
			try {
				Action a = (Action) getContentManager().extractContent(request);

					
					if (a.getAction() instanceof GetOptions) {
						return respondToGetOptions(request, a);

					} else if (a.getAction() instanceof ExecuteTask) {
						return respondExecuteTask(request, a);
					}

					resultMsg.setPerformative(ACLMessage.NOT_UNDERSTOOD);

			} catch (CodecException ce) {
				ce.printStackTrace();
			} catch (OntologyException oe) {
				oe.printStackTrace();
			}
			
			return resultMsg;
		}

	}
	
	private ACLMessage respondToGetOptions(ACLMessage request, Concept concept) {
		
		ComputingCommunicator communicator = new ComputingCommunicator();
		return communicator.sendOptions(Agent_ComputingAgent.this, request);
	}
	
	private ACLMessage respondExecuteTask(ACLMessage request, Action a) {
		
		ComputingCommunicator communicator = new ComputingCommunicator();
		return communicator.executeTask(this, request);
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
}

