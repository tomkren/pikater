package org.pikater.core.ontology;

import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.shared.logging.core.ConsoleLogger;

import jade.content.onto.BeanOntology;

public class ModelOntology extends BeanOntology {
	private static final long serialVersionUID = 5957497401928598917L;

	private ModelOntology(){
		super("ModelOntology");

        String modelPackage = Model.class.getPackage().getName();
                
        try {
            add(modelPackage);
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
	}
	
	static ModelOntology theInstance = new ModelOntology();
	
	public static ModelOntology getInstance(){
		return theInstance;
	}
}
