package pikater;


import pikater.agents.PikaterAgent;
import pikater.agents.management.ManagerAgentCommunicator;
import pikater.ontology.description.ComputationDescription;
import pikater.ontology.description.ComputingAgent;
import pikater.ontology.description.IComputationElement;
import pikater.ontology.messages.MessagesOntology;
import pikater.ontology.messages.Problem;
import pikater.ontology.messages.Solve;
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


/*
 *  Napsano Stepanem - nedivte se kdyz to nebude fungovat :-)
 */
public class Agent_ComputingManager extends PikaterAgent {
	
	private static final long serialVersionUID = 7116837600070411675L;
	
	@Override
	protected void setup() {
		initDefault();
		registerWithDF();
		
	  	System.out.println("Hello World! My name is "+getLocalName());
	  	
	  	addBehaviour(new ComputingManagerBehaviour(this));
	  	
	  	// Make this agent terminate
	  	//doDelete();
	}

	@Override
	protected String getAgentType(){
		return "ComputingManager";
	}
	
}


class ComputingManagerBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	private Agent agent;

	private Codec codec = new SLCodec();
	private Ontology ontology = MessagesOntology.getInstance();

	private MessageTemplate requestMsgTemplate = MessageTemplate
			.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
					MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
							MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
									MessageTemplate.MatchOntology(ontology.getName()))));
	
	public ComputingManagerBehaviour(Agent agent) {	
		super(agent);
		this.agent = agent;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		ACLMessage request = agent.receive(requestMsgTemplate);
		
		if (request == null) {
			block();
		}

		System.out.println(agent.getLocalName()
					+ ": REQUEST received from "
					+ request.getSender().getName());

		try {
			ContentElement content = agent.getContentManager().extractContent(request);
			if (((Action) content).getAction() instanceof ComputationDescription) {

				ComputationDescription description =
						(ComputationDescription) (((Action) content).getAction());
				
				IComputationElement element = description.getRootElement();

				if (element instanceof ComputingAgent) {
					
					ComputingAgent ca = (ComputingAgent) element;

					// Creating new computing angent
			        ManagerAgentCommunicator communicator =
			        		new ManagerAgentCommunicator("agentManager");
			        String type = ca.getModelClass();
			        String name = null;
			        List options = null;
			        AID computingAgent = communicator.createAgent((PikaterAgent) this.agent,
			        		type, name, options);
			        

			        // Creating instance of ontology for computing angent
			        Solve solve = new Solve();
			        
			        Action solveAction = new Action();
			        solveAction.setAction(solve);
			        solveAction.setActor(agent.getAID());


			        // Creating new message
			        ACLMessage msgOut = new ACLMessage(ACLMessage.REQUEST);
			        msgOut.addReceiver(computingAgent);

					agent.getContentManager().fillContent(msgOut, solveAction);
					
					agent.send(msgOut);
			        
				}

				ACLMessage agree = request.createReply();
				agree.setPerformative(ACLMessage.AGREE);
				//String problemID = agent.generateProblemID();
				//agree.setContent(problemID);
				agent.send(agree);

			}
		} catch (UngroundedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CodecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OntologyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	}		

}
