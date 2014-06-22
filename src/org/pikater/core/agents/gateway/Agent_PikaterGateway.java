package org.pikater.core.agents.gateway;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.MailingOntology;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.wrapper.ControllerException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.gateway.GatewayAgent;
import jade.wrapper.gateway.JadeGateway;

public class Agent_PikaterGateway extends GatewayAgent {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2247543952895575432L;

	static final public Codec codec = new SLCodec();
    static private ContentManager contentManager;

	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentInfoOntology.getInstance());
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
