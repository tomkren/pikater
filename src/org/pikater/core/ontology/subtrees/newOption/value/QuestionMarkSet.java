package org.pikater.core.ontology.subtrees.newOption.value;

import java.util.List;

public class QuestionMarkSet implements IValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4434448192843651477L;

	private int countOfValuesToTry;
	private List<IValue> values;
	public int getCountOfValuesToTry() {
		return countOfValuesToTry;
	}
	public void setCountOfValuesToTry(int countOfValuesToTry) {
		this.countOfValuesToTry = countOfValuesToTry;
	}
	public List<IValue> getValues() {
		return values;
	}
	public void setValues(List<IValue> values) {
		this.values = values;
	}
	
}
