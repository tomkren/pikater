package pikater;

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
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;
import pikater.agents.PikaterAgent;
import pikater.agents.management.ManagerAgentCommunicator;
import pikater.ontology.description.ComputationDescription;
import pikater.ontology.description.ComputingAgent;
import pikater.ontology.description.IComputationElement;
import pikater.ontology.messages.MessagesOntology;
import pikater.ontology.messages.Solve;

/*
 *  Napsano Stepanem - nedivte se kdyz to nebude fungovat :-)
 */
public class Agent_Scheduler extends PikaterAgent {
	
	private static final long serialVersionUID = 7226837600070711675L;

	@Override
	protected void setup() {
		initDefault();
		//registerWithDF();
		
	  	System.out.println("Hello World! My name is " ); //+getLocalName());
	  	
	  	//addBehaviour(new SchedulerBehaviour(this));

/*	  	
	  	
		// Creating new computing angent
        ManagerAgentCommunicator communicator =
        		new ManagerAgentCommunicator("agentManager");
        String type = "Agent_ComputingManager";
        String name = null;
        List options = null;
        AID computingAgent = communicator.createAgent((PikaterAgent) this,
        		type, name, options);

	  	
        // Creating instance of ontology for computing angent
        Solve solve = new Solve();
        
        Action solveAction = new Action();
        solveAction.setAction(solve);
        solveAction.setActor(getAID());


        // Creating new message
        ACLMessage msgOut = new ACLMessage(ACLMessage.REQUEST);
        msgOut.addReceiver(computingAgent);

		try {

			getContentManager().fillContent(msgOut, solveAction);

		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		send(msgOut);

*/	  	
			  	
	  	// Make this agent terminate
	  	//doDelete();
	}


	@Override
	protected String getAgentType(){
		return "Scheduler";
	}
	
}


class SchedulerBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	private Agent agent;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = MessagesOntology.getInstance();

	private MessageTemplate requestMsgTemplate = MessageTemplate
			.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
					MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
							MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
									MessageTemplate.MatchOntology(ontology.getName()))));
	
	public SchedulerBehaviour(Agent agent) {	
		super(agent);
		this.agent = agent;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub			
	}

}
