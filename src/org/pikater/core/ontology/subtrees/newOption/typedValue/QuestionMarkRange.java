package org.pikater.core.ontology.subtrees.newOption.typedValue;

public class QuestionMarkRange implements ITypedValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064649544713291827L;

	private int countOfValuesToTry;
	private ITypedValue min;
	private ITypedValue max;
	
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
	public ITypedValue cloneValue() {
		
		QuestionMarkRange valueNew = new QuestionMarkRange();
		valueNew.setCountOfValuesToTry(countOfValuesToTry);
		valueNew.setMin(min.cloneValue());
		valueNew.setMin(max.cloneValue());
		
		return valueNew;		
	}
	
	@Override
	public String exportToWeka() {
		
		return "?";
	}

}
