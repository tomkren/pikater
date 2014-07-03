package org.pikater.core.ontology.subtrees.newOption.value;

public class QuestionMarkRange implements IValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064649544713291827L;

	private int countOfValuesToTry;
	private IValue min;
	private IValue max;
	
	public QuestionMarkRange() {}
	public QuestionMarkRange(IValue min, IValue max) {
		this.min = min;
		this.max = max;
	}

	public QuestionMarkRange(IValue min, IValue max, int countOfValuesToTry) {
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
	
	public IValue getMin() {
		return min;
	}
	public void setMin(IValue min) {
		this.min = min;
	}
	
	public IValue getMax() {
		return max;
	}
	public void setMax(IValue max) {
		this.max = max;
	}

	@Override
	public IValue cloneValue() {
		
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
