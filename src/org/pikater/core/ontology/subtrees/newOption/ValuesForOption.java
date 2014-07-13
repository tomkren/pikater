package org.pikater.core.ontology.subtrees.newOption;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.Value;

import jade.content.Concept;

public class ValuesForOption implements Concept
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3600291732186684079L;

	private List<Value> values;

	public ValuesForOption() {
		values = new ArrayList<Value>();
	}
	public ValuesForOption(List<Value> values) {
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
	public ValuesForOption clone()
	{
		return new ValuesForOption(new ArrayList<Value>(values));
	}
}