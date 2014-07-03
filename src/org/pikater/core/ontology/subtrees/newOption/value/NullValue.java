package org.pikater.core.ontology.subtrees.newOption.value;

public class NullValue implements IValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4240750027791781820L;

	@Override
	public IValue cloneValue() {

		return new NullValue();
	}

	@Override
	public String exportToWeka() {
		
		return "";
	}	
}
