package org.pikater.core.ontology.actions;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.experiment.Experiment;


public class ExperimentOntology extends BeanOntology {

	private static final long serialVersionUID = 4471377586541937606L;

	private ExperimentOntology() {
        super("ExperimentOntology");

        String experimentPackage = Experiment.class.getPackage().getName();
        
        try {
            add(experimentPackage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ExperimentOntology theInstance = new ExperimentOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}