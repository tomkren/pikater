package org.pikater.core.agents.system.data;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.pikater.core.ontology.subtrees.datasource.RegisterDataSourceConcept;

/**
 * User: Kuba
 * Date: 2.5.2014
 * Time: 12:40
 * Behaviors for AgentDataSource - registering datasources and obtaining path to Local DataSources
 */
public class AgentDataSourceBehavior extends CyclicBehaviour {
	
	private static final long serialVersionUID = 1953739710427491422L;

	protected Codec codec;
    protected Ontology ontology;
    protected AgentDataSource dsAgent;
    
    // One template for registering files
    private MessageTemplate registerDSTemplate;

    /**
     *
     * @param codec  Codec to be used
     * @param ontology Ontology to be used
     * @param agent Owner agent
     */
    public AgentDataSourceBehavior(Codec codec, Ontology ontology,
    		AgentDataSource agent) {
    	
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

        try {
            ACLMessage inf = myAgent.receive(registerDSTemplate);
            
            if (inf != null) {
            	ContentElement content =
            			myAgent.getContentManager().extractContent(inf);
                Concept concept = ((Action) content).getAction();
                
                if (concept instanceof RegisterDataSourceConcept) {
                    RegisterDataSourceConcept regConcept =
                    		(RegisterDataSourceConcept) concept;
                    
                    for (String typeI : regConcept.getDataTypes()) {
                    	String dataSouceI =
                    			regConcept.getTaskId() + "." + typeI;
                        dsAgent.addDataSourceToOwned(dataSouceI);
                    }
                    ACLMessage resultMsg = inf.createReply();
                    resultMsg.setPerformative(ACLMessage.AGREE);
                    myAgent.send(resultMsg);
                    return;
                }

                ACLMessage resultMsg = inf.createReply();
                resultMsg.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                myAgent.send(resultMsg);
            }

        } catch (OntologyException | Codec.CodecException e) {
        	dsAgent.logException(e.getMessage(), e);
        }
    }
}
