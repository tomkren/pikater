package org.pikater.core.agents.gateway;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.actions.AgentInfoOntology;
import org.pikater.core.ontology.actions.BatchOntology;
import org.pikater.core.ontology.actions.MailingOntology;
import org.pikater.core.ontology.actions.MessagesOntology;
import org.pikater.core.ontology.agentInfo.AgentInfo;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.wrapper.ControllerException;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREResponder;
import jade.wrapper.gateway.GatewayAgent;
import jade.wrapper.gateway.JadeGateway;


public class Agent_PikaterGateway extends GatewayAgent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6530381228391705988L;

	static final public Codec codec = new SLCodec();
    static private ContentManager contentManager;

	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(MessagesOntology.getInstance());
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(MailingOntology.getInstance());
		
		return ontologies;
	}

    @Override
    protected void setup() {
        super.setup();
        getContentManager().registerLanguage(codec);
        
        for ( Ontology ontologyI : getOntologies() ) {
        	getContentManager().registerOntology(ontologyI);
        }

        System.out.println("PikaterGateway started");
        
        this.addBehaviour(new AchieveREResponder(this, null) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 324270572581200118L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
				
				Action action = null;
				try {
					action = (Action) getContentManager().extractContent(request);
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (action.getAction() instanceof AgentInfo) {
					return respondToAgentInfo(request, action);
				}
				

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				
				System.out.println("Failure responding to request: " + request.getContent());
				return failure;
			}
        	
        });
    }

    protected ACLMessage respondToAgentInfo(ACLMessage request, Action action) {
    	
    	AgentInfo agentInfo = (AgentInfo) action.getAction();
    	System.out.println("Agent " + getName() + ": recieved AgentInfo from " + agentInfo.getAgentClass() );
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result r = new Result(action, "OK");
		try {
			getContentManager().fillContent(reply, r);
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return reply;
	}

	/** receiver je jmeno agenta (prijemce), action je nejaka AgentAction z Pikateri ontologie */
    static public ACLMessage makeActionRequest(String receiver, Ontology ontology, AgentAction action) throws CodecException, OntologyException, ControllerException {
        JadeGateway.checkJADE(); // force gateway container initialization to make AID resolution possible
        if (contentManager == null) {
            contentManager = new ContentManager();
            contentManager.registerLanguage(codec);
            contentManager.registerOntology(ontology);
        }

        AID target = new AID(receiver, AID.ISLOCALNAME);
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(target);
        msg.setLanguage(codec.getName());
        msg.setOntology(ontology.getName());
        contentManager.fillContent(msg, new Action(target, action));
        return msg;
    }
}
