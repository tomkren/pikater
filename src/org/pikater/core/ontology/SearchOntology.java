package org.pikater.core.ontology;

import org.jfree.util.Log;
import org.pikater.core.ontology.subtrees.newoption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.search.ExecuteParameters;
import org.pikater.core.ontology.subtrees.search.GetParameters;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchitems.SearchItem;
import org.pikater.core.ontology.subtrees.task.Evaluation;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;


/**
 * Created by Stepan Balcar on 18.5.14.
 */
public class SearchOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1829196889268218770L;

	private SearchOntology() {
        super("SearchOntology");

        try {

            String searchItemPackage = SearchItem.class.getPackage().getName();
            String boolPackage = BooleanValue.class.getPackage().getName();

            add(SearchSolution.class);
            add(ExecuteParameters.class);
            add(GetParameters.class);

            add(searchItemPackage);
            add(boolPackage);
            add(SearchItem.class);
            add(Evaluation.class);

        } catch (Exception e) {
            Log.error("Unexpected error occured:", e);
        }
    }

    static SearchOntology theInstance = new SearchOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}