package org.pikater.core.ontology.subtrees.newOption;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import jade.content.Concept;

public class ValuesForOption implements Concept, Iterable<Value>
{
	private static final long serialVersionUID = -3600291732186684079L;

	private List<Value> values;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public ValuesForOption() {}
	public ValuesForOption(List<Value> values)
	{
		this.values = values;
	}
	
	public List<Value> getValues() {
		return values;
	}
	public void setValues(List<Value> values) {
		this.values = values;
	}
	
	@Override
	public Iterator<Value> iterator()
	{
		return values.iterator();
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
	
	public boolean containsQuestionMark()
	{
		for (Value valueI : values)
		{
			IValueData ivalueI = valueI.getCurrentValue();
			if (ivalueI instanceof QuestionMarkRange ||
					ivalueI instanceof QuestionMarkSet) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isValid()
	{
		for(Value value : values)
		{
			if(!value.isValid())
			{
				return false;
			}
		}
		return true;
	}
	
	public String exportToWeka()
	{
		String result = "";
		for (Value valueI : values)
		{
			if (!(valueI.getCurrentValue() instanceof NullValue))
			{
				result += " " + valueI.getCurrentValue().exportToWeka();
			}
		}
		return result;
	}
	
	@Override
	public ValuesForOption clone()
	{
		List<Value> valuesCopied = new ArrayList<Value>();
		for(Value value : values)
		{
			valuesCopied.add(value.clone());
		}
		return new ValuesForOption(valuesCopied);
	}
}