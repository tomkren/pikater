package org.pikater.core.ontology.description;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class DescriptionOntology extends BeanOntology{

    private DescriptionOntology() {
        super("DescriptionOntology");

        try {
            add("pikater.ontology.description");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static DescriptionOntology theInstance = new DescriptionOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
