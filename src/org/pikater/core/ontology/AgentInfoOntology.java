package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.option.Option;


public class AgentInfoOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7595856728415263726L;

	private AgentInfoOntology() {
        super("AgentInfoOntology");

        String optionPackage = Option.class.getPackage().getName();
        String agentInfoPackage = AgentInfo.class.getPackage().getName();
        
        try {
            add(optionPackage);
            add(agentInfoPackage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static AgentInfoOntology theInstance = new AgentInfoOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
