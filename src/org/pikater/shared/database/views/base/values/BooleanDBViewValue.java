package org.pikater.shared.database.views.base.values;

/**
 * True of false value, editable.
 */
public abstract class BooleanDBViewValue extends AbstractDBViewValue<Boolean>
{
	public BooleanDBViewValue(Boolean value)
	{
		super(DBViewValueType.BOOLEAN, value);
	}
	
	@Override
	public boolean isReadOnly()
	{
		return false;
	}
}