package org.pikater.core.agents.system;


import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.DescriptionOntology;
import org.pikater.core.ontology.messages.ExecuteExperiment;


public class Agent_Scheduler extends PikaterAgent {
	
	private static final long serialVersionUID = 7226837600070711675L;


	@Override
	protected void setup() {

	  	System.out.println("Agent: " +getLocalName() + " starts.");

		initDefault();
		registerWithDF("Scheduler");

		this.getContentManager().registerLanguage(getCodec());
		this.getContentManager().registerOntology(DescriptionOntology.getInstance());


		RecieveExperiment recieveExp =
			new RecieveExperiment(this, getCodec());
		addBehaviour(recieveExp);

	}


	@Override
	protected String getAgentType(){
		return "Scheduler";
	}

}


class RecieveExperiment extends CyclicBehaviour {

	private Agent agent = null;
	private Codec codec = null;
	
	public RecieveExperiment(Agent agent, Codec codec) {
		this.agent = agent;
		this.codec = codec;
	}

	public void action() {

		MessageTemplate reguestTemplate = 
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage request= this.agent.receive(reguestTemplate);
		
		if (request != null) {
			
			ContentElement ce = null;
			try {
				ce = agent.getContentManager().extractContent(request);
			} catch (CodecException | OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Action act = (Action)ce;
			
			if (!(act.getAction() instanceof ExecuteExperiment)) {
				return;
			}
           
			System.out.println(agent.getName() + ": Agent recieved ComputingDescription from " + request.getSender().getName() );
			ExecuteExperiment exeExperiment = (ExecuteExperiment) act.getAction();
            ComputationDescription compDescription = exeExperiment.getDescription();
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("OK");
            agent.send(reply);



            AID receiver = new AID("ComputationDescriptionParser", false);		

            Ontology ontology = DescriptionOntology.getInstance();
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(receiver);
            msg.setLanguage(codec.getName());
            msg.setOntology(ontology.getName());
            try {
    			agent.getContentManager().fillContent(msg, new Action(receiver, exeExperiment));
    			agent.addBehaviour(new SendProblemToCompManager(agent, msg) );
    			
    			ACLMessage replyOK = FIPAService.doFipaRequestClient(agent, msg, 10000);
    			System.out.println("Reply: " + replyOK.getContent());
    			
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

		}
	}
}



class SendProblemToCompManager extends AchieveREInitiator {

	private static final long serialVersionUID = 8923548223375000884L;

	String gui_id;
	PikaterAgent agent;
	
	public SendProblemToCompManager(Agent agent, ACLMessage msg) {
		super(agent, msg);
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

