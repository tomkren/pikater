package org.pikater.core.ontology.subtrees.newOption.values;

public class FloatValue implements ITypedValue
{
	private static final long serialVersionUID = -6154392916809467193L;
	
	private float value;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public FloatValue() {}
	public FloatValue(float value) {
		this.value = value;
	}

	public Float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
	@Override
	public ITypedValue clone()
	{
		return new FloatValue(value);
	}

	@Override
	public String exportToWeka() {
		
		return String.valueOf(value);
	}
	
	@Override
	public String toDisplayName()
	{
		return "Float";
	}
}
