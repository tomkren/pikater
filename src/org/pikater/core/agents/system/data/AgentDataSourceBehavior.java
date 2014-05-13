package org.pikater.core.agents.system.data;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.pikater.core.ontology.data.DataSourcePath;
import org.pikater.core.ontology.data.GetDataSourcePath;
import org.pikater.core.ontology.data.RegisterDataSourceConcept;

/**
 * User: Kuba
 * Date: 2.5.2014
 * Time: 12:40
 * Behaviors for AgentDataSource - registering datasources and obtaining path to Local DataSources
 */
   public class AgentDataSourceBehavior extends CyclicBehaviour {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1953739710427491422L;

	protected Codec codec;
    protected Ontology ontology;
    protected AgentDataSource dsAgent;
    public AgentDataSourceBehavior(Codec codec,Ontology ontology,AgentDataSource agent)
    {
        super(agent);
        dsAgent=agent;
        this.codec=codec;
        this.ontology=ontology;
    }

    //one template for registering files
    private MessageTemplate RegisterDSTemplate =
            MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
                            MessageTemplate.MatchOntology(ontology.getName())));
    //one template for obtaining path on local server. If not on local server, DataSourceAgent must copy
    // the file to local server. TODO
    private MessageTemplate GetDSPathTemplate =
            MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
                            MessageTemplate.MatchOntology(ontology.getName())));

    @Override
    public void action() {

        ContentElement content;
        try {
            ACLMessage inf = myAgent.receive(RegisterDSTemplate);
            if (inf != null) {
                content = myAgent.getContentManager().extractContent(inf);
                if (((Action) content).getAction() instanceof RegisterDataSourceConcept)  {
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
                return;
            }

            ACLMessage req = myAgent.receive(GetDSPathTemplate);
            if (req != null) {
                content = myAgent.getContentManager().extractContent(req);
                if (((Action) content).getAction() instanceof GetDataSourcePath)  {
                    GetDataSourcePath getDSPathAction=(GetDataSourcePath)((Action) content).getAction();
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
                ACLMessage result_msg = inf.createReply();
                result_msg.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                myAgent.send(result_msg);
                return;
            }

        } catch (UngroundedException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        }
    }
}
