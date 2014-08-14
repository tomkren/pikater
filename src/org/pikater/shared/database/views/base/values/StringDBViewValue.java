package org.pikater.shared.database.views.base.values;

/**
 * A single string value, editable.
 */
public abstract class StringDBViewValue extends AbstractDBViewValue<String>
{
	public StringDBViewValue(String value)
	{
		super(DBViewValueType.STRING, value == null ? "" : value);
	}
	
	@Override
	public void setValue(String newValue) throws UnsupportedOperationException
	{
		super.setValue(newValue == null ? "" : newValue);
	}
	
	@Override
	public boolean isReadOnly()
	{
		return false;
	}
}