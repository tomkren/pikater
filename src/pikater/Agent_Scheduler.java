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


		// wait until another agents start
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


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


        AID receiver = new AID("ComputingManager", false);		

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiver);
        msg.setLanguage(getCodec().getName());
        msg.setOntology(getOntology().getName());
        try {
			getContentManager().fillContent(msg, new Action(receiver, executeExpAction));
			
			ACLMessage reply = FIPAService.doFipaRequestClient(this, msg, 10000);
			
			System.out.println("Reply: " + reply.getContent());
			
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FIPAException e) {
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
