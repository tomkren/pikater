package org.pikater.core.ontology.subtrees.newOption.values;

import java.util.ArrayList;
import java.util.List;

public class QuestionMarkSet implements ITypedValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4434448192843651477L;

	private int countOfValuesToTry;
	private List<ITypedValue> values;
	
	public int getCountOfValuesToTry() {
		return countOfValuesToTry;
	}
	public void setCountOfValuesToTry(int countOfValuesToTry) {
		this.countOfValuesToTry = countOfValuesToTry;
	}
	public List<ITypedValue> getValues() {
		return values;
	}
	public void setValues(List<ITypedValue> values) {
		this.values = values;
	}
	
	@Override
	public ITypedValue cloneValue() {

		QuestionMarkSet setNew = new QuestionMarkSet();
		setNew.setCountOfValuesToTry(countOfValuesToTry);
		
		List<ITypedValue> valuesNew = new ArrayList<ITypedValue>();
		for (ITypedValue valueI : values) {
			valuesNew.add(valueI.cloneValue());
		}
		setNew.setValues(valuesNew);
		
		return setNew;
	}
	
	@Override
	public String exportToWeka() {
		
		return "?";
	}
	
	@Override
	public String toString()
	{
		return "QuestionMarkRange";
	}
}
