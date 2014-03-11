package pikater.ontology.description.elements;

import pikater.ontology.description.Element;
import pikater.ontology.description.IElement;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public abstract class Parameter extends Element {
    String name;
    boolean searchable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }
}
