package org.pikater.core.ontology;


import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.jfree.util.Log;
import org.pikater.core.ontology.subtrees.batchdescription.duration.ExpectedDuration;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.base.ValueType;
import org.pikater.core.ontology.subtrees.newoption.restrictions.IRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.result.Results;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.shared.logging.core.ConsoleLogger;

/**
 * Created by Stepan on 4.5.14.
 */
public class ResultOntology extends BeanOntology {
	private static final long serialVersionUID = 5355736320938592917L;

	private ResultOntology() {
        super("ResultOntology");

        String resultPackage =
        		Results.class.getPackage().getName();
        String taskPackage = Task.class.getPackage().getName();
        String dataPackage = Datas.class.getPackage().getName();
        
        String expectedDurationPackage = ExpectedDuration.class.getPackage().getName();
        
        String optionPackage = NewOption.class.getPackage().getName();
        String restrictionPackage = IRestriction.class.getPackage().getName();
        String typePackage = ValueType.class.getPackage().getName();
        String valuePackage = BooleanValue.class.getPackage().getName();

        try {
            add(resultPackage);

        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
        
        try {
            add(taskPackage);
            add(dataPackage);
            
            add(expectedDurationPackage);            
            
            add(optionPackage);
            add(restrictionPackage);
            add(typePackage);
            add(valuePackage);

        } catch (Exception e) {
            Log.error("Unexpected error occured:", e);
        }
    }

    static ResultOntology theInstance = new ResultOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
