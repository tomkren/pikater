package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.batch.Batch;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchdescription.duration.ExpectedDuration;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.base.ValueType;
import org.pikater.core.ontology.subtrees.newoption.restrictions.IRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.BooleanValue;
import org.pikater.shared.logging.core.ConsoleLogger;

public class BatchOntology extends BeanOntology {

	private static final long serialVersionUID = 4471377586541937606L;

	private BatchOntology() {
        super("BatchOntology");

        String computingDescriptionPackage = ComputationDescription.class.getPackage().getName();
        String batchPackage = Batch.class.getPackage().getName();
        String shortTimeDurationPackage = ExpectedDuration.class.getPackage().getName();
        
        String optionPackage = NewOption.class.getPackage().getName();
        String restrictionPackage = IRestriction.class.getPackage().getName();
        String typePackage = ValueType.class.getPackage().getName();
        String valuePackage = BooleanValue.class.getPackage().getName();
        
        try {
            add(computingDescriptionPackage);
            add(batchPackage);
            add(shortTimeDurationPackage);
            
            add(optionPackage);
            add(restrictionPackage);
            add(typePackage);
            add(valuePackage);

        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static BatchOntology theInstance = new BatchOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
