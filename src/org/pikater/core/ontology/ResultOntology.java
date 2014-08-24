package org.pikater.core.ontology;


import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.jfree.util.Log;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.IRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.result.Results;
import org.pikater.core.ontology.subtrees.task.Task;

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
            e.printStackTrace();
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
            Log.error(e.getMessage(), e);
        }
    }

    static ResultOntology theInstance = new ResultOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
