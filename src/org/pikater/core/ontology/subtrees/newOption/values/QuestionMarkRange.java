package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValidatedValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class QuestionMarkRange implements IValidatedValueData
{
	private static final long serialVersionUID = 4064649544713291827L;

	private int countOfValuesToTry;
	private IComparableValueData min;
	private IComparableValueData max;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public QuestionMarkRange() {}

    public QuestionMarkRange(IComparableValueData min, IComparableValueData max)
    {
        this.min = min;
        this.max = max;
    }

	public QuestionMarkRange(IComparableValueData min, IComparableValueData max, int countOfValuesToTry)
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
	public IComparableValueData getMin()
	{
		return min;
	}
	public void setMin(IComparableValueData min)
	{
		this.min = min;
	}
	public IComparableValueData getMax()
	{
		return max;
	}
	public void setMax(IComparableValueData max)
	{
		this.max = max;
	}
	
	/* -------------------------------------------------------------
	 * CUSTOM INSTANCE COMPARING - GENERATED WITH ECLIPSE
	 * - generate again when you change local fields or their types
	 * - required in {@link org.pikater.web.vaadin.gui.server.components.forms.OptionValueForm}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + countOfValuesToTry;
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionMarkRange other = (QuestionMarkRange) obj;
		if (countOfValuesToTry != other.countOfValuesToTry)
			return false;
		if (max == null)
		{
			if (other.max != null)
				return false;
		}
		else if (!max.equals(other.max))
			return false;
		if (min == null)
		{
			if (other.min != null)
				return false;
		}
		else if (!min.equals(other.min))
			return false;
		return true;
	}
	//-------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public Object hackValue()
	{
		return null;
	}
	@Override
	public IValueData clone()
	{
		return new QuestionMarkRange((IComparableValueData) min.clone(), (IComparableValueData) max.clone(), countOfValuesToTry);
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
		if((min == null) && (max == null))
		{
			return false;
		}
		else if((min != null) && (max != null) && (!min.getClass().equals(max.getClass()) || (min.compareTo(max) > 0))) 
		{
			return false;
		}
		else
		{
			return countOfValuesToTry > 0;
		}
	}
}