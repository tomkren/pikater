package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;

public class FloatValue implements IComparableValueData
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

	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
	/* -------------------------------------------------------------
	 * CUSTOM INSTANCE COMPARING - GENERATED WITH ECLIPSE
	 * - generate again when you change local fields or their types
	 * - required in {@link org.pikater.web.vaadin.gui.server.components.forms.OptionValueForm}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(value);
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FloatValue other = (FloatValue) obj;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}
	//-------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public Float hackValue()
	{
		return value;
	}
	@Override
	public FloatValue clone()
	{
		FloatValue result;
		try
		{
			result = (FloatValue) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		result.setValue(value);
		return result;
	}
	@Override
	public String exportToWeka()
	{
		return String.valueOf(value);
	}
	@Override
	public String toDisplayName()
	{
		return "Float";
	}
	@Override
	public int compareTo(IComparableValueData o)
	{
		return hackValue().compareTo((Float) o.hackValue());
	}
}
