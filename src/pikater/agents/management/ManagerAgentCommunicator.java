package pikater.agents.management;

import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.util.leap.List;
import pikater.agents.PikaterAgent;
import org.pikater.core.ontology.messages.CreateAgent;

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

    public AID createAgent(PikaterAgent agent, String type, String name, List options) {
        ACLMessage msg_ca = new ACLMessage(ACLMessage.REQUEST);
        msg_ca.addReceiver(new AID(_initAgentAID, false));
        msg_ca.setLanguage(agent.getCodec().getName());
        msg_ca.setOntology(agent.getOntology().getName());

        CreateAgent ca = new CreateAgent();
        if (name != null){
            ca.setName(name);
        }
        if (options != null){
            ca.setArguments(options);
        }
        ca.setType(type);

        Action a = new Action();
        a.setAction(ca);
        a.setActor(agent.getAID());

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
