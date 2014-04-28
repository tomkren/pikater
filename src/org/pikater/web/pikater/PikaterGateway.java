package org.pikater.web.pikater;

import org.pikater.core.ontology.messages.MessagesOntology;
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

@SuppressWarnings("serial")
public class PikaterGateway extends GatewayAgent {
    static final public Codec codec = new SLCodec();
    static final public Ontology ontology = MessagesOntology.getInstance();
    static private ContentManager contentManager;

    @Override
    protected void setup() {
        super.setup();
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);

        System.out.println("PikaterGateway started");
    }

    /** receiver je jmeno agenta (prijemce), action je nejaka AgentAction z Pikateri ontologie */
    static public ACLMessage makeActionRequest(String receiver, AgentAction action) throws CodecException, OntologyException, ControllerException {
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
