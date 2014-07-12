package org.pikater.core.ontology.subtrees.newOption;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class Values implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3600291732186684079L;

	private List<Value> values;

	public Values() {
		values = new ArrayList<Value>();
	}
	public Values(List<Value> values) {
		this.values = values;
	}
	
	public List<Value> getValues() {
		return values;
	}
	public void setValues(List<Value> values) {
		this.values = values;
	}
	
	public void addValue(Value value) {
		this.values.add(value);
	}
	
	public Values cloneValues() {
		
		List<Value> valuesClone = new ArrayList<Value>();
		for (Value valueI : values) {
			valuesClone.add(valueI.cloneValue());
		}
		return new Values(valuesClone);
	}

    public int size()
    {
        return getValues().size();
    }
}
