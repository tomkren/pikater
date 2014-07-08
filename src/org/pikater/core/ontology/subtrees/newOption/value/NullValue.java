package org.pikater.core.ontology.subtrees.newOption.value;

public class NullValue implements ITypedValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4240750027791781820L;

	@Override
	public ITypedValue cloneValue() {

		return new NullValue();
	}

	@Override
	public String exportToWeka() {
		
		return "";
	}	
}
