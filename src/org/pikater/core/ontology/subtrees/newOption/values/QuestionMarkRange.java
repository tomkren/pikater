package org.pikater.core.ontology.subtrees.newOption.values;

public class QuestionMarkRange implements ITypedValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064649544713291827L;

	private int countOfValuesToTry;
	private ITypedValue min;
	private ITypedValue max;
	
	/*
	 * Default constructor is needed.
	 */
	public QuestionMarkRange() {}
	public QuestionMarkRange(ITypedValue min, ITypedValue max) {
		this.min = min;
		this.max = max;
	}

	public QuestionMarkRange(ITypedValue min, ITypedValue max, int countOfValuesToTry) {
		this.min = min;
		this.max = max;
		this.countOfValuesToTry = countOfValuesToTry;
	}
	
	public int getCountOfValuesToTry() {
		return countOfValuesToTry;
	}
	public void setCountOfValuesToTry(int countOfValuesToTry) {
		this.countOfValuesToTry = countOfValuesToTry;
	}
	
	public ITypedValue getMin() {
		return min;
	}
	public void setMin(ITypedValue min) {
		this.min = min;
	}
	
	public ITypedValue getMax() {
		return max;
	}
	public void setMax(ITypedValue max) {
		this.max = max;
	}

	@Override
	public ITypedValue clone() {
		
		QuestionMarkRange valueNew = new QuestionMarkRange();
		valueNew.setCountOfValuesToTry(countOfValuesToTry);
		valueNew.setMin(min.clone());
		valueNew.setMin(max.clone());
		
		return valueNew;		
	}
	
	@Override
	public String exportToWeka() {
		
		return "?";
	}
	
	@Override
	public String toDisplayName()
	{
		return "QuestionMarkRange";
	}
}
