package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.management.CreateAgent;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.base.ValueType;
import org.pikater.core.ontology.subtrees.newoption.restrictions.IRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.ping.Ping;
import org.pikater.core.ontology.subtrees.systemload.SystemLoad;
import org.pikater.shared.logging.core.ConsoleLogger;


public class AgentManagementOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7595856728415263726L;

	private AgentManagementOntology() {
        super("AgentManagementOntology");

        String createAgentPackage = CreateAgent.class.getPackage().getName();
        
        String optionPackage = NewOption.class.getPackage().getName();
        String restrictionPackage = IRestriction.class.getPackage().getName();
        String typePackage = ValueType.class.getPackage().getName();
        String valueDataPackage = IValueData.class.getPackage().getName();
        String valuePackage = BooleanValue.class.getPackage().getName();
        String systemLoadPackage = SystemLoad.class.getPackage().getName();
        
        String pingPackage = Ping.class.getPackage().getName();
        
        try {
            add(createAgentPackage);

            add(optionPackage);
            add(restrictionPackage);
            add(typePackage);
            add(valueDataPackage);
            add(valuePackage);
            add(systemLoadPackage);
            add(pingPackage);
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static AgentManagementOntology theInstance = new AgentManagementOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
