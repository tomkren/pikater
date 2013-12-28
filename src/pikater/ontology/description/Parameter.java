package pikater.ontology.description;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public abstract class Parameter implements Concept {
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
