package pikater.ontology.description;

import jade.content.Concept;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Search implements Concept{

    String searchClass;
    ArrayList<Parameter> searchParameters;

    public String getSearchClass() {
        return searchClass;
    }

    public void setSearchClass(String searchClass) {
        this.searchClass = searchClass;
    }

    public ArrayList<Parameter> getSearchParameters() {
        return searchParameters;
    }

    public void setSearchParameters(ArrayList<Parameter> searchParameters) {
        this.searchParameters = searchParameters;
    }
}
