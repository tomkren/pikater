package org.pikater.shared.database.views.base.values;

/**
 * A single string value, read-only or editable.
 */
public abstract class StringDBViewValue extends AbstractDBViewValue<String>
{
	public StringDBViewValue(String value, boolean readOnly)
	{
		super(DBViewValueType.STRING, value, readOnly);
	}
}