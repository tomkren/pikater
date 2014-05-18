package org.pikater.core.ontology.actions;

import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;

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
            
            add(SearchSolution.class);

            add(searchItemPackage);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static SearchOntology theInstance = new SearchOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}