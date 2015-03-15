package org.pikater.core.ontology;

import org.pikater.core.ontology.subtrees.openml.GetDatasets;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;

public class OpenMLOntology extends BeanOntology {

	private static final long serialVersionUID = 4520694615841230496L;

	static OpenMLOntology instance = new OpenMLOntology();
	
	private OpenMLOntology() {
	    super("OpenMLOntology");
	    
	    String getOpenMLPackage = GetDatasets.class.getPackage().getName();
		
		try {
			add(getOpenMLPackage);
		} catch (BeanOntologyException e) {
			e.printStackTrace();
		}
	    
	}
	
	public static OpenMLOntology getInstance(){
		return instance;
	}
}
