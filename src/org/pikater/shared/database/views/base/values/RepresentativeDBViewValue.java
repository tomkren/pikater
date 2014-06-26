package org.pikater.shared.database.views.base.values;

import java.util.Set;

/**
 * A single value chosen from a set of values, editable. Should you wish for this value
 * not to be editable, use {@link StringReadOnlyDBViewValue} instead.
 */
public abstract class RepresentativeDBViewValue extends AbstractDBViewValue<String>
{
	private final Set<String> values;
	
	public RepresentativeDBViewValue(Set<String> values, String selectedValue)
	{
		super(DBViewValueType.REPRESENTATIVE, selectedValue);
		this.values = values;
	}
	
	@Override
	public boolean isReadOnly()
	{
		return false;
	}
	
	@Override
	public void setValue(String newValue) throws UnsupportedOperationException, IllegalArgumentException
	{
		if(values.contains(newValue))
		{
			super.setValue(newValue);
		}
		else
		{
			throw new IllegalArgumentException("The provided value does not belong to the set of allowed values.");
		}
	}
	
	public Set<String> getValues()
	{
		return values;
	}
}
