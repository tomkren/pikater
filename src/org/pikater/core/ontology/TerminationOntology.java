package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.agent.TerminateSelf;


public class TerminationOntology extends BeanOntology {
	private static final long serialVersionUID = -7595856728415263726L;

	private TerminationOntology() {
        super("TerminationOntology");

        try {
            add(TerminateSelf.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static TerminationOntology theInstance = new TerminationOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
