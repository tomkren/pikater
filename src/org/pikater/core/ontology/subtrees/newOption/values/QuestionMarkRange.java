package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValidatedValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class QuestionMarkRange implements IValidatedValueData
{
	private static final long serialVersionUID = 4064649544713291827L;

	private int countOfValuesToTry;
	private IValueData min;
	private IValueData max;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public QuestionMarkRange() {}

    public QuestionMarkRange(IValueData min, IValueData max)
    {
        this.min = min;
        this.max = max;
    }

	public QuestionMarkRange(IValueData min, IValueData max, int countOfValuesToTry)
	{
		this.min = min;
		this.max = max;
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
	public IValueData getMin()
	{
		return min;
	}
	public void setMin(IValueData min)
	{
		this.min = min;
	}
	public IValueData getMax()
	{
		return max;
	}
	public void setMax(IValueData max)
	{
		this.max = max;
	}
	
	@Override
	public Object hackValue()
	{
		return null;
	}
	@Override
	public IValueData clone()
	{
		return new QuestionMarkRange(min.clone(), max.clone(), countOfValuesToTry);
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
	@Override
	public boolean isValid()
	{
		if((min == null) || (max == null) || !min.getClass().equals(max.getClass()))
		{
			return false;
		}
		else if(!(min instanceof IComparableValueData) || ((IComparableValueData) min).compareTo((IComparableValueData) max) > 0)
		{
			return false;
		}
		return (countOfValuesToTry > 0);
	}
}