package org.pikater.core.ontology.subtrees.newOption.values;

import java.util.ArrayList;
import java.util.List;

public class QuestionMarkSet implements ITypedValue
{
	private static final long serialVersionUID = 4434448192843651477L;

	private int countOfValuesToTry;
	private List<ITypedValue> values;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public QuestionMarkSet() {}
	public QuestionMarkSet(List<ITypedValue> values, int countOfValuesToTry)
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
	public List<ITypedValue> getValues()
	{
		return values;
	}
	public void setValues(List<ITypedValue> values)
	{
		this.values = values;
	}
	
	@Override
	public Object getValue()
	{
		return getCountOfValuesToTry();
	}
	
	@Override
	public ITypedValue clone()
	{
		List<ITypedValue> valuesCopied = new ArrayList<ITypedValue>();
		for(ITypedValue value : values)
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
		return "QuestionMarkRange";
	}
}