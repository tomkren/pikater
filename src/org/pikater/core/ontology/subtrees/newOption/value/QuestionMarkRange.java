package org.pikater.core.ontology.subtrees.newOption.value;

public class QuestionMarkRange implements IValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064649544713291827L;

	private int countOfValuesToTry;
	private IValue min;
	private IValue max;
	
	
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

}
