package org.pikater.core.ontology;


import org.pikater.core.ontology.subtrees.result.Results;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Stepan on 4.5.14.
 */
public class ResultOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5355736320938592917L;

	private ResultOntology() {
        super("ResultOntology");

        String resultPackage =
        		Results.class.getPackage().getName();

        try {
            add(resultPackage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ResultOntology theInstance = new ResultOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
