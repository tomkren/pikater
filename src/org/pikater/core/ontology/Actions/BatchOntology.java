package org.pikater.core.ontology.Actions;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.batch.Batch;
import org.pikater.core.ontology.description.DescriptionOntology;
import org.pikater.core.ontology.messages.option.Option;


public class BatchOntology extends BeanOntology {

	private static final long serialVersionUID = 4471377586541937606L;

	private BatchOntology() {
        super("BatchOntology");

        String computingDescriptionPackage = DescriptionOntology.class.getPackage().getName();
        String batchPackage = Batch.class.getPackage().getName();
        String optionPackage = Option.class.getPackage().getName();
        
        try {
            add(computingDescriptionPackage);
            add(batchPackage);
            add(optionPackage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static BatchOntology theInstance = new BatchOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
