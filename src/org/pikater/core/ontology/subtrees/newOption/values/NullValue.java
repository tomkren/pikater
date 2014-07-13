package org.pikater.core.ontology.subtrees.newOption.values;

public class NullValue implements ITypedValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4240750027791781820L;
	
	/*
	 * Default constructor is needed.
	 */
	public NullValue() {}

	@Override
	public ITypedValue clone() {

		return new NullValue();
	}

	@Override
	public String exportToWeka() {
		
		return "";
	}
	
	@Override
	public String toDisplayName()
	{
		return "NONE";
	}
}
