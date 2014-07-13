package org.pikater.core.ontology.subtrees.newOption.values;

public class QuestionMarkRange implements ITypedValue
{
	private static final long serialVersionUID = 4064649544713291827L;

	private int countOfValuesToTry;
	private ITypedValue min;
	private ITypedValue max;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public QuestionMarkRange() {}
	public QuestionMarkRange(ITypedValue min, ITypedValue max, int countOfValuesToTry)
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
	
	public ITypedValue getMin()
	{
		return min;
	}
	
	public ITypedValue getMax()
	{
		return max;
	}
	
	@Override
	public Object getValue()
	{
		return getCountOfValuesToTry();
	}

	@Override
	public ITypedValue clone()
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
}
