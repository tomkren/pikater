package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.IRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;

public class BatchOntology extends BeanOntology {

	private static final long serialVersionUID = 4471377586541937606L;

	private BatchOntology() {
        super("BatchOntology");

        String computingDescriptionPackage = ComputationDescription.class.getPackage().getName();
        String batchPackage = Batch.class.getPackage().getName();
        
        String optionPackage = NewOption.class.getPackage().getName();
        String restrictionPackage = IRestriction.class.getPackage().getName();
        String typePackage = Type.class.getPackage().getName();
        String valuePackage = ITypedValue.class.getPackage().getName();
        
        try {
            add(computingDescriptionPackage);
            add(batchPackage);
            add(optionPackage);
            add(restrictionPackage);
            add(typePackage);
            add(valuePackage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static BatchOntology theInstance = new BatchOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
