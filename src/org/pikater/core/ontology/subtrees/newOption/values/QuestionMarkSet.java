package org.pikater.core.ontology.subtrees.newOption.values;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValidatedValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class QuestionMarkSet implements IValidatedValueData
{
	private static final long serialVersionUID = 4434448192843651477L;

	private int countOfValuesToTry;
	private List<IValueData> values;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public QuestionMarkSet() {}
	public QuestionMarkSet(List<IValueData> values, int countOfValuesToTry)
	{
		this.values = values;
		this.countOfValuesToTry = countOfValuesToTry;
	}
	
	public int getCountOfValuesToTry()
	{
		return countOfValuesToTry;
	}
	public void setCountOfValuesToTry(int countOfValuesToTry)
	{
		this.countOfValuesToTry = countOfValuesToTry;
	}
	public List<IValueData> getValues()
	{
		return values;
	}
	public void setValues(List<IValueData> values)
	{
		this.values = values;
	}
	
	@Override
	public Object hackValue()
	{
		return null;
	}
	
	@Override
	public IValueData clone()
	{
		List<IValueData> valuesCopied = new ArrayList<IValueData>();
		for(IValueData value : values)
		{
			valuesCopied.add(value.clone());
		}
		return new QuestionMarkSet(valuesCopied, countOfValuesToTry);
	}
	
	@Override
	public String exportToWeka()
	{
		return "?";
	}
	
	@Override
	public String toDisplayName()
	{
		return "QuestionMarkSet";
	}
	
	@Override
	public boolean isValid()
	{
		if((values == null) || values.isEmpty())
		{
			return false; 
		}
		return (countOfValuesToTry > 0) && (countOfValuesToTry <= values.size());
	}
}