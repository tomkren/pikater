package org.pikater.core.ontology.description;

import jade.content.Concept;

import java.util.ArrayList;

import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.ontology.messages.Option;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Search implements Concept{

    String searchClass;
    ArrayList<Option> searchOptions;

    public String getSearchClass() {
        return searchClass;
    }

    public void setSearchClass(String searchClass) {
        this.searchClass = searchClass;
    }

    public ArrayList<Option> getSearchOptions() {
        return searchOptions;
    }

    public void setSearchOptions(ArrayList<Option> searchOptions) {
        this.searchOptions = searchOptions;
    }
}
