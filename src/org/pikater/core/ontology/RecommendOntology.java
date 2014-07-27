package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.batchDescription.Recommend;


/**
 * Created by Stepan on 18.6.14.
 */
public class RecommendOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5355736320938592917L;

	private RecommendOntology() {
        super("RecomendOntology");

        try {
            add(Recommend.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static RecommendOntology theInstance = new RecommendOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
