package org.pikater.core.ontology.subtrees.newOption;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class ValueList implements Concept
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3600291732186684079L;

	private List<Value> values;

	public ValueList() {
		values = new ArrayList<Value>();
	}
	public ValueList(List<Value> values) {
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
	public Value getValue(int index)
	{
		return values.get(index);
	}
	
	public int size()
	{
		return values.size();
	}
	
	@Override
	public ValueList clone()
	{
		return new ValueList(new ArrayList<Value>(values));
	}
}