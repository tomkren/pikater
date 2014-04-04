package pikater;

import java.io.IOException;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.util.leap.List;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import pikater.agents.PikaterAgent;
import pikater.agents.management.ManagerAgentCommunicator;
import pikater.ontology.description.ComputationDescription;
import pikater.ontology.description.ComputingAgent;
import pikater.ontology.description.DataSourceDescription;
import pikater.ontology.description.FileDataProvider;
import pikater.ontology.description.FileVisualizer;
import pikater.ontology.description.IComputationElement;
import pikater.ontology.messages.ExecuteExperiment;
import pikater.ontology.messages.MessagesOntology;
import pikater.ontology.messages.SendEmail;
import pikater.ontology.messages.Solve;

import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;


public class Agent_Scheduler extends PikaterAgent {
	
	private static final long serialVersionUID = 7226837600070711675L;

	
	@Override
	protected void setup() {
		
	  	System.out.println("Agent: " +getLocalName() + " starts.");

		initDefault();
		registerWithDF("Scheduler");


        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setTrainingData(fileDataSource);
		comAgent.setModelClass("pikater.agents.computing.Agent_WekaCA");

		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setDataProvider(comAgent);

        FileVisualizer visualizer = new FileVisualizer();
        visualizer.setDataSource(computingDataSource);

        ComputationDescription comDescription = new ComputationDescription();
		comDescription.setRootElement(visualizer);


		ExecuteExperiment executeExpAction = new ExecuteExperiment(comDescription);

		try {
			Thread.sleep(9000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        AID receiver = new AID("ComputingManager", false);		

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiver);
        msg.setLanguage(getCodec().getName());
        msg.setOntology(getOntology().getName());
        try {
			getContentManager().fillContent(msg, new Action(receiver, executeExpAction));
			this.addBehaviour(new SendProblemToComManager(this, msg) );
			
			//ACLMessage reply = FIPAService.doFipaRequestClient(this, msg, 10000);
			//System.out.println("Reply: " + reply.getContent());
			
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

	  	// Make this agent terminate
	  	//doDelete();
	}


	@Override
	protected String getAgentType(){
		return "Scheduler";
	}
	
}




class SendProblemToComManager extends AchieveREInitiator {

	private static final long serialVersionUID = 8923548223375000884L;

	String gui_id;
	PikaterAgent agent;
	
	public SendProblemToComManager(Agent agent, ACLMessage msg) {
		super(agent, msg);
		this.gui_id = gui_id;
		this.agent = (PikaterAgent) agent;
	}

	protected void handleAgree(ACLMessage agree) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ agree.getSender().getName() + " agreed.");
	}

	protected void handleInform(ACLMessage inform) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ inform.getSender().getName() + " replied.");
	}

	protected void handleRefuse(ACLMessage refuse) {
		System.out.println(agent.getLocalName() + ": Agent "
				+ refuse.getSender().getName()
				+ " refused to perform the requested action");
	}

	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver
			// does not exist
			System.out.println(agent.getLocalName() + ": Responder does not exist");
		} else {
			System.out.println(agent.getLocalName() + ": Agent " + failure.getSender().getName()
					+ " failed to perform the requested action");
		}
	}

}

