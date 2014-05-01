package org.pikater.core.ontology.description;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class DescriptionOntology extends BeanOntology {

	private static final long serialVersionUID = 4471377586541937606L;

	private DescriptionOntology() {
        super("DescriptionOntology");

        String thisPackage = DescriptionOntology.class.getPackage().getName();

        try {
            add(thisPackage);
            add(org.pikater.core.ontology.messages.option.Option.class);
            add(org.pikater.core.ontology.messages.option.Interval.class);
            add(org.pikater.core.ontology.messages.ExecuteExperiment.class);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static DescriptionOntology theInstance = new DescriptionOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
