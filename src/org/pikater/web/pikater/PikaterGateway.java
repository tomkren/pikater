package org.pikater.web.pikater;

import org.pikater.core.ontology.messages.MessagesOntology;

import jade.content.AgentAction;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.wrapper.AgentContainer;
import jade.lang.acl.ACLMessage;
import jade.wrapper.gateway.GatewayAgent;

@SuppressWarnings("serial")
public class PikaterGateway extends GatewayAgent {
    static final public Codec codec = new SLCodec();
    static final public Ontology ontology = MessagesOntology.getInstance();
    static private ContentManager contentManager;
    // nevim jak se obejit bez jednoho kontejneru navic, pokud totiz neexistuje, tak nejde vytvaret AID (haze runtime vyjimky "No platform found")
    // (kontejner s GW agentem v doby kdy vytvarim pozadavek jeste nemusi existovat)
    static private AgentContainer container;

    @Override
    protected void setup() {
        super.setup();
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);

        System.out.println("PikaterGateway started");
    }

    /** receiver je jmeno agenta (prijemce), action je nejaka AgentAction z Pikateri ontologie */
    static public ACLMessage makeActionRequest(String receiver, AgentAction action) throws CodecException, OntologyException {
        if (contentManager == null) {
            contentManager = new ContentManager();
            contentManager.registerLanguage(codec);
            contentManager.registerOntology(ontology);
            container = Runtime.instance().createAgentContainer(new ProfileImpl());
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
