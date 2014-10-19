package org.pikater.core.agents;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.pikater.core.agents.system.manager.ManagerAgentService;
import org.pikater.core.configuration.Argument;
import org.pikater.core.configuration.Arguments;
import org.pikater.core.ontology.TerminationOntology;
import org.pikater.core.ontology.subtrees.agent.TerminateSelf;
import org.pikater.shared.logging.core.AgentLogger;

/**
 * User: Kuba Date: 25.8.13 Time: 9:38
 */
public abstract class PikaterAgent extends Agent {
	private static final long serialVersionUID = 3427874121438630714L;

	protected Codec codec = new SLCodec();
	private transient AgentLogger logger;
	protected Arguments arguments = new Arguments(
			new HashMap<String, Argument>());
	/**
	 * for the master node this is empty, slave node agents are named with this
	 * as the suffix
	 */
	protected String nodeName;
	private boolean registeredToDF = false;

	/*
	 * INIT INTERFACE
	 */

	protected boolean registerWithDF() {
		return registerWithDF(new ArrayList<String>());
	}

	protected boolean registerWithDF(String service) {
		return registerWithDF(Collections.singletonList(service));
	}

	protected boolean registerWithDF(List<String> serviceTypes) {
		// register with the DF

		DFAgentDescription description = new DFAgentDescription();
		// the description is the root description for each agent
		// and how we prefer to communicate.

		description.setName(getAID());
		// the service description describes a particular service we
		// provide.
		ServiceDescription serviceDescription = new ServiceDescription();

		serviceDescription.setName(getLocalName());
		String typeDesc = getAgentType();
		serviceDescription.setType(typeDesc);
		description.addServices(serviceDescription);

		// add more agent service(s) (typicaly general service type, e.g.
		// Search)

		for (String st : serviceTypes) {
			ServiceDescription serviceDescG = new ServiceDescription();
			serviceDescG.setName(getLocalName());
			serviceDescG.setType(st);
			description.addServices(serviceDescG);
		}

		// register synchronously registers us with the DF, we may
		// prefer to do this asynchronously using a behaviour.
		try {
			DFService.register(this, description);
			registeredToDF = true;

			StringBuilder sb = new StringBuilder(
					"Successfully registered with DF; service types: ");
			for (String st : serviceTypes) {
				sb.append(st).append(" ");
			}
			logInfo(sb.toString());
			return true;

		} catch (FIPAException e) {
			logException("Error registering with DF:", e);
			return false;

		}
	}

	public void initDefault() {
		Object[] args = getArguments();
		initDefault(args);
	}

	public void initDefault(Object[] args) {
		if (getName().contains("-")) {
			nodeName = getLocalName().substring(
					getLocalName().lastIndexOf('-') + 1);
		}
		parseArguments(args);

		initLogging();
		logInfo("is alive...");
		getContentManager().registerLanguage(getCodec());

		for (Ontology ontologyI : getOntologies()) {
			getContentManager().registerOntology(ontologyI);
		}
		getContentManager().registerOntology(TerminationOntology.getInstance());

		addTerminationBehavior();
	}

	protected void initLogging() {
		logger = new AgentLogger(this);
	}

	private void addTerminationBehavior() {
		MessageTemplate template = MessageTemplate.and(MessageTemplate
				.MatchOntology(TerminationOntology.getInstance().getName()),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

		addBehaviour(new AchieveREResponder(this, template) {
			private static final long serialVersionUID = 8926769466268882841L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request)
					throws NotUnderstoodException, RefuseException {
				try {
					Concept action = ((Action) (getContentManager()
							.extractContent(request))).getAction();
					if (action instanceof TerminateSelf) {
						addBehaviour(new OneShotBehaviour() {
							private static final long serialVersionUID = -8265931114367756110L;

							@Override
							public void action() {
								terminate();

							}
						});
						ACLMessage res = request.createReply();
						res.setPerformative(ACLMessage.INFORM);
						return res;
					} else {
						throw new NotUnderstoodException(
								"Unknown action requested");
					}
				} catch (CodecException e) {
					logException("Unknown codec", e);
					throw new NotUnderstoodException("Unknown codec: "
							+ e.getMessage());
				} catch (OntologyException e) {
					logException("Unknown ontology", e);
					throw new NotUnderstoodException("Unknown ontology: "
							+ e.getMessage());
				}
			}
		});
	}

	/*
	 * TERMINATION INTERFACE
	 */

	public boolean killAgent(String agentName) {
		ManagerAgentService communicator = new ManagerAgentService();
		return communicator.killAgent(this, agentName);
	}

	public void terminate() {
		if (registeredToDF) {
			try {
				logInfo("Deregistering from DF");
				DFService.deregister(this);
			} catch (FIPAException e) {
				logException("Failed to deregister from DF", e);
			}
		}
		logInfo("Terminating");
		doDelete();
	}

	/*
	 * MISCELLANEOUS INTERFACE
	 */

	public Codec getCodec() {
		return codec;
	}

	public abstract List<Ontology> getOntologies();

	protected String getAgentType() {
		return this.getClass().getName();
	}

	public String getArgumentValue(String argName) {
		return arguments.getArgumentValue(argName);
	}

	public Boolean isArgumentValueTrue(String argName) {
		return arguments.isArgumentValueTrue(argName);
	}

	public Boolean containsArgument(String argName) {
		return arguments.containsArgument(argName);
	}

	private void parseArguments(Object[] args) {
		Map<String, Argument> argumentsMap = new HashMap<String, Argument>();
		arguments = new Arguments(argumentsMap);
		if (args == null) {
			return;
		}
		for (Object argI : args) {
			if (argI instanceof Argument) {
				Argument argumentToAdd = (Argument) argI;
				argumentsMap.put(argumentToAdd.getName(), argumentToAdd);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	public boolean isSameNode(AID id) {
		return ((nodeName == null || nodeName.isEmpty()) && !id.getLocalName()
				.contains("-"))
				|| nodeName != null
				&& id.getLocalName().endsWith("-" + nodeName);
	}

	public AID createAgent(String type, String name, Arguments options) {
		return ManagerAgentService.createAgent(this, type, name, options);
	}

	public AID createAgent(String type) {
		return createAgent(type, null, null);
	}

	public AID createAgent(
			org.pikater.core.ontology.subtrees.management.Agent agent) {
		return createAgent(agent.getType(), agent.getName(), null);
		// TODO agent.getOptions ... predelat optiony na argumenty
	}

	/*
	 * LOGGING INTERFACE
	 */

	public void logInfo(String text) {
		logger.log(Level.INFO, text);
	}

	public void logWarning(String message) {
		logger.log(Level.WARNING, message);
	}

	public void logSevere(String message) {
		logger.log(Level.SEVERE, message);
	}

	public void logException(String message, Throwable t) {
		logger.logThrowable(message, t);
	}
}