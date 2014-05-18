package org.pikater.core.ontology.actions;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.management.CreateAgent;


public class AgentManagementOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7595856728415263726L;

	private AgentManagementOntology() {
        super("AgentManagementOntology");

        String createAgentPackage = CreateAgent.class.getPackage().getName();
        
        try {
            add(createAgentPackage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static AgentManagementOntology theInstance = new AgentManagementOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
