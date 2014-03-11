package pikater.ontology.description.boxes.searchers;

import java.util.ArrayList;

import pikater.ontology.description.Box;
import pikater.ontology.description.elements.Parameter;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Search extends Box {

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
