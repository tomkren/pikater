package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.agent.TerminateSelf;
import org.pikater.shared.logging.core.ConsoleLogger;


public class TerminationOntology extends BeanOntology {
	private static final long serialVersionUID = -7595856728415263726L;

	private TerminationOntology() {
        super("TerminationOntology");

        try {
            add(TerminateSelf.class);
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static TerminationOntology theInstance = new TerminationOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
