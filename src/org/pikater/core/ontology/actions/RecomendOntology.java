package org.pikater.core.ontology.actions;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.description.Recommend;


/**
 * Created by Stepan on 18.6.14.
 */
public class RecomendOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5355736320938592917L;

	private RecomendOntology() {
        super("RecomendOntology");

        try {
            add(Recommend.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static RecomendOntology theInstance = new RecomendOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
