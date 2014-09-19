package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;

public class DoubleValue implements IComparableValueData
{
	private static final long serialVersionUID = 1276470189024492227L;

	private double value;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public DoubleValue() {}
	public DoubleValue(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
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
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		DoubleValue other = (DoubleValue) obj;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}
	//-------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public Double hackValue()
	{
		return value;
	}
	@Override
	public DoubleValue clone() throws CloneNotSupportedException
	{
		DoubleValue result = (DoubleValue) super.clone();
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
		return "Double";
	}
	@Override
	public int compareTo(IComparableValueData o)
	{
		return hackValue().compareTo((Double) o.hackValue());
	}
}
