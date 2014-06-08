package org.pikater.core.agents.system.management;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.util.leap.List;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.actions.MessagesOntology;
import org.pikater.core.ontology.management.CreateAgent;
import org.pikater.core.ontology.messages.option.Option;

/**
 * Created with IntelliJ IDEA.
 * User: Kuba
 * Date: 25.8.13
 * Time: 9:21
 * To change this template use File | Settings | File Templates.
 */
public class ManagerAgentCommunicator {
    private String _initAgentAID;

    public ManagerAgentCommunicator(String initAgentAID) {
        _initAgentAID = initAgentAID;
    }

    public AID createAgent(PikaterAgent agent, String type, String name, java.util.List<Option> options) {
    	Ontology ontology = MessagesOntology.getInstance();
    	
        ACLMessage msg_ca = new ACLMessage(ACLMessage.REQUEST);
        msg_ca.addReceiver(new AID(_initAgentAID, false));
        msg_ca.setLanguage(agent.getCodec().getName());
        msg_ca.setOntology(ontology.getName());

        CreateAgent ca = new CreateAgent();
        if (name != null){
            ca.setName(name);
        }
        if (options != null){
            ca.setArguments(options);
        }
        ca.setType(type);

        Action a = new Action(agent.getAID(),ca);

        AID aid = null;
        try {
            agent.getContentManager().fillContent(msg_ca, a);
            ACLMessage msg_name = FIPAService.doFipaRequestClient(agent, msg_ca);

            aid = new AID(msg_name.getContent(), AID.ISLOCALNAME);
        } catch (FIPAException e) {
            System.err.println(agent.getLocalName() + ": Exception while adding agent "
                    + type + ": " + e);
        } catch (Codec.CodecException e) {
            System.err.print(agent.getLocalName() + ": ");
            e.printStackTrace();
        } catch (OntologyException e) {
            System.err.print(agent.getLocalName() + ": ");
            e.printStackTrace();
        }

        return aid;
    }
}
