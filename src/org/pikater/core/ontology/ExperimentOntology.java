package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.IRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.shared.logging.core.ConsoleLogger;


public class ExperimentOntology extends BeanOntology {

	private static final long serialVersionUID = 4471377586541937606L;

	private ExperimentOntology() {
        super("ExperimentOntology");

        String experimentPackage = Experiment.class.getPackage().getName();

        String taskPackage = Task.class.getPackage().getName();
        String dataPackage = Datas.class.getPackage().getName();
        
        String optionPackage = NewOption.class.getPackage().getName();
        String restrictionPackage = IRestriction.class.getPackage().getName();
        String typePackage = ValueType.class.getPackage().getName();
        String valuePackage = IValueData.class.getPackage().getName();
                
        try {
            add(experimentPackage);
            add(taskPackage);
            add(dataPackage);
            
            add(optionPackage);
            add(restrictionPackage);
            add(typePackage);
            add(valuePackage);

        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static ExperimentOntology theInstance = new ExperimentOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}