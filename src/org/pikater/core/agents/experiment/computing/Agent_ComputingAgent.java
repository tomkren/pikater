package org.pikater.core.agents.experiment.computing;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.agents.experiment.computing.computing.ComputingAction;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.SaveAgent;
import org.pikater.core.ontology.subtrees.data.GetData;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;
import org.pikater.core.ontology.subtrees.option.GetOptions;
import org.pikater.core.ontology.subtrees.result.PartialResults;
import org.pikater.core.ontology.subtrees.task.Evaluation;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;
import org.pikater.core.ontology.subtrees.task.ExecuteTaksOnCPUCore;
import org.pikater.core.ontology.subtrees.task.Task;

import weka.core.Instances;

public abstract class Agent_ComputingAgent extends Agent_AbstractExperiment {
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
	protected Agent agent_options = null;

	protected Instances data; // data read from fileName file
	public Instances train;
	public DataInstances onto_train;
	public Instances test;
	public DataInstances onto_test;

	public Instances label;
	public DataInstances onto_label;
	int convId = 0;

	protected String[] OPTIONS;
	public  org.pikater.core.ontology.subtrees.task.Task current_task = null;

	protected String className;

	protected Object[] args;

	public boolean working = false; // TODO -> state?

	public LinkedList<ACLMessage> taskFIFO = new LinkedList<ACLMessage>();

	public Behaviour execution_behaviour = null;

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

	protected ACLMessage sendOptions(ACLMessage request) {
		ACLMessage msgOut = request.createReply();
		msgOut.setPerformative(ACLMessage.INFORM);
		try {
			// Prepare the content
			ContentElement content = getContentManager()
					.extractContent(request); // TODO exception block?
			Result result = new Result((Action) content, agent_options);
			// result.setValue(options);

			try {
				// Let JADE convert from Java objects to string
				getContentManager().fillContent(msgOut, result);
				// send(msgOut);
			} catch (CodecException ce) {
				logError("", ce);
			} catch (OntologyException oe) {
				logError("", oe);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msgOut;

	} // end SendOptions

	@Override
	protected void setup() {

		initDefault();

		
        if (containsArgument(CLASS_NAME)) {
            className = getArgumentValue(CLASS_NAME);
        }
        if (isArgumentValueTrue("load")) {
            // TODO loadAgent(getLocalName());
            // args = new String[0]; // arguments are empty
        }
        // some important initializations before registering
        getParameters();

		// register with the DF
		if (! this.getAID().getLocalName().contains("Service")){	
			java.util.List<String> descr = new java.util.ArrayList<String>();
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
			execution_behaviour.reset();
			state = states.TRAINED;
			return;
		}
		newAgent = false;

		setEnabledO2ACommunication(true, 0);

		addBehaviour(new RequestServer(this));
		addBehaviour(execution_behaviour = new ComputingAction(this));

		sendAgentInfo(getAgentInfo());
		
	} // end setup

	protected void getParameters() {

		// AgentInfo agentInfo = getAgentInfo();
		// TODO: transformation from agentInfo to parameters + send to old
		// guiAgent
	}

	public boolean setOptions(org.pikater.core.ontology.subtrees.task.Task task) {
		/*
		 * INPUT: task with weka options Fills the OPTIONS array and
		 * current_task.
		 */
		current_task = task;
		OPTIONS = task.getAgent().optionsToString().split("[ ]+");

		return true;
	} // end loadConfiguration

	public String getOptions() {
		// write out OPTIONS

		String strOPTIONS = "";
		strOPTIONS += "OPTIONS:";
		for (int i = 0; i < OPTIONS.length; i++) {
			strOPTIONS += " " + OPTIONS[i];
		}
		return strOPTIONS;
	}

	public ACLMessage sendGetDataReq(String fileName) {
		AID[] ARFFReaders;
		AID reader = null;
		ACLMessage msgOut = null;
		// Make the list of reader agents
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentNames.ARRFF_READER);
		template.addServices(sd);
		try {
			GetData get_data = new GetData();

			DFAgentDescription[] result = DFService.search(this, template);
			// System.out.println(getLocalName() +
			// ": Found the following ARFFReader agents:");
			ARFFReaders = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				if (isSameNode(result[i].getName())) {
					// prefer local reader for O2A transfer
					reader = result[i].getName();
					log("preferring reader " + reader.getName());
					get_data.setO2aAgent(getLocalName());
					break;
				}
				ARFFReaders[i] = result[i].getName();
				// System.out.println("    " + ARFFReaders[i].getName());
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
			msgOut.setLanguage(codec.getName());
			msgOut.setOntology(ontology.getName());
			msgOut.addReceiver(reader);
			msgOut.setConversationId("get-data_" + convId++);
			// content
			get_data.setFileName(fileName);
			Action a = new Action();
			a.setAction(get_data);
			a.setActor(this.getAID());
			getContentManager().fillContent(msgOut, a);
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return null;
		} catch (CodecException e) {
			e.printStackTrace();
			return null;
		} catch (OntologyException e) {
			e.printStackTrace();
			return null;
		}
		return msgOut;
	} // end sendGetDataReq

	public static byte[] toBytes(Object object) throws Exception {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
		oos.writeObject(object);

		return baos.toByteArray();
	}

	/*
	 * Send partial results to the GUI Agent(s) call it after training or during
	 * training?
	 */
	protected void sendResultsToGUI(Boolean first_time, Task _task,
			List _evaluations) {
		ACLMessage msgOut = new ACLMessage(ACLMessage.INFORM);
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentNames.GUI_AGENT);
		template.addServices(sd);
		try {
			DFAgentDescription[] gui_agents = DFService.search(this, template);
			for (int i = 0; i < gui_agents.length; ++i) {
				msgOut.addReceiver(gui_agents[i].getName());
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		msgOut.setConversationId("partial-results");

		PartialResults content = new PartialResults();
		content.setResults(_evaluations);
		// content.setTask_id(_task.getId());
		if (first_time) {
			content.setTask(_task);
		}
		try {
			getContentManager().fillContent(msgOut, content);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}

		send(msgOut);
	}

	protected class RequestServer extends CyclicBehaviour {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1074564968341084444L;

		private MessageTemplate reqMsgTemplate = MessageTemplate
				.and(MessageTemplate
						.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
						MessageTemplate.and(MessageTemplate
								.MatchPerformative(ACLMessage.REQUEST),
								MessageTemplate.and(MessageTemplate
										.MatchLanguage(codec.getName()),
										MessageTemplate
												.MatchOntology(TaskOntology
														.getInstance()
														.getName()))));

		public RequestServer(PikaterAgent agent) {
			super(agent);
		}

		// TODO: will we accept or refuse the request? (working, size of
		// taksFIFO, latency time...)
		boolean acceptTask() {
			return true/* taskFIFO.size()<=MAX_TASKS */;
		}

		ACLMessage processExecute(ACLMessage req) {
			ACLMessage result_msg = req.createReply();
			if (acceptTask()) {
				result_msg.setPerformative(ACLMessage.AGREE);
				taskFIFO.addLast(req);

				if (taskFIFO.size() == 1) {
					if (!execution_behaviour.isRunnable()) {
						execution_behaviour.restart();
					}
				}
				/*
				 * if (!execution_behaviour.isRunnable()) {
				 * execution_behaviour.restart(); }
				 */
			} else {
				result_msg.setPerformative(ACLMessage.REFUSE);
				result_msg.setContent("(Computing agent overloaded)");
			}
			return result_msg;
		}

		@Override
		public void action() {

			ContentElement content;
			try {
				ACLMessage req = receive(reqMsgTemplate);
				if (req != null) {
					content = getContentManager().extractContent(req);
					if (((Action) content).getAction() instanceof GetOptions) {
						ACLMessage result_msg = sendOptions(req);
						send(result_msg);
						return;

					} else if (((Action) content).getAction() instanceof ExecuteTaksOnCPUCore) {
						send(processExecute(req));
						return;
					}

					ACLMessage result_msg = req.createReply();
					result_msg.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					send(result_msg);
					return;
				} else {
					block(); // don't cycle waiting for messages
				}
			} catch (CodecException ce) {
				ce.printStackTrace();
			} catch (OntologyException oe) {
				oe.printStackTrace();
			}
		}
	}

	public byte[] getAgentObject() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		oos.flush();
		oos.close();

		byte[] data = bos.toByteArray();
		return data;
	}

	private Agent getAgentWithFilledObject()
			throws IOException {

		Agent savedAgent = current_task
				.getAgent();
		savedAgent.setObject(getAgentObject());

		return savedAgent;
	}

	public String saveAgentToFile() throws IOException, CodecException,
			OntologyException, FIPAException {

		SaveAgent saveAgent = new SaveAgent();

		saveAgent.setAgent(getAgentWithFilledObject());

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.MANAGER_AGENT, false));
		request.setOntology(TaskOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(this.getAID());
		a.setAction(saveAgent);

		getContentManager().fillContent(request, a);
		ACLMessage reply = FIPAService.doFipaRequestClient(this, request);

		String objectFilename = reply.getContent();

		return objectFilename;
	}

	public String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSSSSS");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public void terminate() {
		doDelete();
	}
}

