package org.pikater.core.agents.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.MailingOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.shared.logging.core.ConsoleLogger;

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

	private static final long serialVersionUID = -2247543952895575432L;

	static final public Codec codec = new SLCodec();

	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(BatchOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
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

        ConsoleLogger.log(Level.INFO, "PikaterGateway started");
    }

    /** receiver je jmeno agenta (prijemce), action je nejaka AgentAction z Pikateri ontologie */
    static public ACLMessage makeActionRequest(String receiver,
    		Ontology ontology, AgentAction action) throws CodecException,
    		OntologyException, ControllerException {
    	
    	PikaterGateway_General.initJadeGateway();
        
    	JadeGateway.checkJADE();
        // force gateway container initialization to make AID resolution possible
        
        ContentManager contentManager = new ContentManager();
        contentManager.registerLanguage(codec);
        contentManager.registerOntology(ontology);
        

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        try {
        	AID target = new AID(receiver, AID.ISLOCALNAME);
        	
        	msg.addReceiver(target);
        	msg.setLanguage(codec.getName());
        	msg.setOntology(ontology.getName());
        	contentManager.fillContent(msg, new Action(target, action));
        } catch(CodecException ce) {
        	JadeGateway.shutdown();
        	throw ce;
        } catch(OntologyException oe) {
        	JadeGateway.shutdown();
        	throw oe;
        }
        return msg;
    }
}
