package org.pikater.core.ontology.subtrees.search.searchItems;

import jade.content.Concept;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import java.util.List;
import java.util.Random;


/**
 * Item in solution-schema
 */
public abstract class SearchItem implements Concept {

    private String name;
	private static final long serialVersionUID = 3249399049389780447L;
	private Integer numberOfValuesToTry;

	/**
	 * Create random solution item
	 * 
	 */
	public IValueData randomValue(Random rndGen)
    {
        List<IValueData> possibleValues = possibleValues();
        int index = rndGen.nextInt(possibleValues.size());
        return possibleValues.get(index);
    }
	
	/**
	 * Returns all possible values from this schema
	 * 
	 */
	public abstract List<IValueData> possibleValues();
	
	public Integer getNumberOfValuesToTry() {
		return numberOfValuesToTry;
	}
	
	public void setNumberOfValuesToTry(Integer numberOfValuesToTry) {
		this.numberOfValuesToTry = numberOfValuesToTry;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
