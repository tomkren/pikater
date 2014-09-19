package org.pikater.core.ontology.subtrees.newOption;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.IValidated;
import org.pikater.core.ontology.subtrees.newOption.base.IWekaItem;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.shared.logging.core.ConsoleLogger;

import jade.content.Concept;

public class ValuesForOption implements Concept, IValidated, IWekaItem, Iterable<Value>
{
	private static final long serialVersionUID = -3600291732186684079L;

	private List<Value> values;
	
	/**
	 * Should only be used internally and by JADE.
	 */
	@Deprecated
	public ValuesForOption()
	{
		this.values = new ArrayList<Value>();
	}
    public ValuesForOption(Value value)
    {
    	this();
        this.values.add(value);
    }
	public ValuesForOption(List<Value> values)
	{
		this();
		this.values.addAll(values);
	}
	
	public List<Value> getValues() {
		return values;
	}
	public void setValues(List<Value> values) {
		this.values = values;
	}
	
	/*
	 * Some convenience interface.
	 */
	public void addValue(Value value)
	{
		this.values.add(value);
	}
	public Value returnByIndex(int index)
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
	
	/*
	 * Inherited interface.
	 */
	@Override
	public ValuesForOption clone()
	{
		try
		{
			ValuesForOption result = (ValuesForOption) super.clone();
			for(Value value : values)
			{
				result.addValue(value.clone());
			}
			return result;
		}
		catch (CloneNotSupportedException e)
		{
			ConsoleLogger.logThrowable("Could not clone values. Returning null...", e);
			return null;
		}
	}
	@Override
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
	@Override
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
	public Iterator<Value> iterator()
	{
		return values.iterator();
	}
}