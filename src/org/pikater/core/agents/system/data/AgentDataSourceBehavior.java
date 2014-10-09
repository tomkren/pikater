package org.pikater.core.agents.system.data;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.pikater.core.ontology.subtrees.dataSource.RegisterDataSourceConcept;

/**
 * User: Kuba
 * Date: 2.5.2014
 * Time: 12:40
 * Behaviors for AgentDataSource - registering datasources and obtaining path to Local DataSources
 */
public class AgentDataSourceBehavior extends CyclicBehaviour
{
	private static final long serialVersionUID = 1953739710427491422L;

	protected Codec codec;
    protected Ontology ontology;
    protected AgentDataSource dsAgent;
    
    private MessageTemplate registerDSTemplate; // one template for registering files

    /**
     *
     * @param codec  Codec to be used
     * @param ontology Ontology to be used
     * @param agent Owber agent
     */
    public AgentDataSourceBehavior(Codec codec,Ontology ontology,AgentDataSource agent) {
        super(agent);
        dsAgent = agent;
        this.codec = codec;
        this.ontology = ontology;
        this.registerDSTemplate =
        		MessageTemplate.and(
        				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
                        MessageTemplate.MatchOntology(ontology.getName())));
        MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
                        MessageTemplate.MatchOntology(ontology.getName())));
    }

    @Override
    public void action() {

        ContentElement content;
        try {
            ACLMessage inf = myAgent.receive(registerDSTemplate);
            if (inf != null) {
                content = myAgent.getContentManager().extractContent(inf);
                if (((Action) content).getAction() instanceof RegisterDataSourceConcept) {
                    RegisterDataSourceConcept regConcept = (RegisterDataSourceConcept) ((Action) content).getAction();
                    for (String type : regConcept.getDataTypes()) {
                        dsAgent.addDataSourceToOwned(regConcept.getTaskId() + "." + type);
                    }
                    ACLMessage result_msg = inf.createReply();
                    result_msg.setPerformative(ACLMessage.AGREE);
                    myAgent.send(result_msg);
                    return;
                }

                ACLMessage result_msg = inf.createReply();
                result_msg.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                myAgent.send(result_msg);
            }

            /* Inevitable null-pointer dereference
            ACLMessage req = myAgent.receive(GetDSPathTemplate);
            if (req != null) {
                content = myAgent.getContentManager().extractContent(req);
                if (((Action) content).getAction() instanceof GetDataSourcePath)  {
                    GetDataSourcePath getDSPathAction=(GetDataSourcePath)((Action) content).getAction();
                    
                    @SuppressWarnings("null")
					ACLMessage result_msg = inf.createReply();
                    result_msg.setPerformative(ACLMessage.INFORM);
                    DataSourcePath dsPath=new DataSourcePath();
                    dsPath.setPath(dsAgent.getPathToDataSource(getDSPathAction.getTaskId()+"."+getDSPathAction.getType()));
                    //List results = new ArrayList();
                    //results.add(dsPath);
                    Result result = new Result((Action) content, dsPath);
                    myAgent.getContentManager().fillContent(result_msg, result);
                    myAgent.send(result_msg);
                    return;
                }

                @SuppressWarnings("null")
                ACLMessage result_msg = inf.createReply();
                result_msg.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                myAgent.send(result_msg);
                return;
            }
            */

        } catch (OntologyException | Codec.CodecException e) {
        	dsAgent.logException(e.getMessage(), e);
        }
    }
}
