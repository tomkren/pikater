package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.agent.NewAgent;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.IRestriction;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.ITypedValue;
import org.pikater.core.ontology.subtrees.option.GetOptions;


public class AgentInfoOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7595856728415263726L;

	private AgentInfoOntology() {
        super("AgentInfoOntology");

        String optionPackage = NewOption.class.getPackage().getName();
        String restrictionPackage = IRestriction.class.getPackage().getName();
        String typePackage = ValueType.class.getPackage().getName();
        String valuePackage = ITypedValue.class.getPackage().getName();
        
        String agentInfoPackage = AgentInfo.class.getPackage().getName();
        String modelPackage = Model.class.getPackage().getName();
        
        String agentPackage = NewAgent.class.getPackage().getName();
        
        try {
            add(optionPackage);
            add(restrictionPackage);
            add(typePackage);
            add(valuePackage);
            
            add(agentInfoPackage);
            add(modelPackage);
            
            add(agentPackage);

            add(GetOptions.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static AgentInfoOntology theInstance = new AgentInfoOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
