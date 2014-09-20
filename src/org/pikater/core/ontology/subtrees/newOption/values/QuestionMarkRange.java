package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValidatedValueData;

public class QuestionMarkRange implements IValidatedValueData
{
	private static final long serialVersionUID = 4064649544713291827L;

	private int countOfValuesToTry;
	private RangeRestriction userDefinedRestriction;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public QuestionMarkRange() {}

	/**
	 * Basic constructor, calls the other one & sets the {@link #countOfValuesToTry}
	 * field to default.
	 * @param min
	 * @param max
	 */
    public QuestionMarkRange(IComparableValueData min, IComparableValueData max)
    {
    	this(min, max, 0);
    }
    /**
     * Main constructor.
     * @param min
     * @param max
     * @param countOfValuesToTry
     */
	public QuestionMarkRange(IComparableValueData min, IComparableValueData max, int countOfValuesToTry)
	{
		this(countOfValuesToTry, new RangeRestriction(min, max));
	}
	/**
	 * More or less a copy constructor. For internal use only.
	 * @param min
	 * @param max
	 * @param countOfValuesToTry
	 */
	private QuestionMarkRange(int countOfValuesToTry, RangeRestriction userDefinedRestriction)
	{
		this.countOfValuesToTry = countOfValuesToTry;
		this.userDefinedRestriction = userDefinedRestriction;
	}
	
	public int getCountOfValuesToTry()
	{
		return countOfValuesToTry;
	}
	public void setCountOfValuesToTry(int countOfValuesToTry)
	{
		this.countOfValuesToTry = countOfValuesToTry;
	}
	public RangeRestriction getUserDefinedRestriction()
	{
		return userDefinedRestriction;
	}
	public void setUserDefinedRestriction(RangeRestriction userDefinedRestriction)
	{
		this.userDefinedRestriction = userDefinedRestriction;
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
		result = prime
				* result
				+ ((userDefinedRestriction == null) ? 0
						: userDefinedRestriction.hashCode());
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
		if (userDefinedRestriction == null)
		{
			if (other.userDefinedRestriction != null)
				return false;
		}
		else if (!userDefinedRestriction.equals(other.userDefinedRestriction))
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
	public QuestionMarkRange clone()
	{
		QuestionMarkRange result;
		try
		{
			result = (QuestionMarkRange) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		result.setCountOfValuesToTry(countOfValuesToTry);
		result.setUserDefinedRestriction(userDefinedRestriction.clone());
		return result;
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
		if((userDefinedRestriction == null) || !userDefinedRestriction.isValid())
		{
			return false;
		}
		else
		{
			return countOfValuesToTry > 0;
		}
	}
}