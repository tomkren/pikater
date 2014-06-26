package org.pikater.shared.database.views.base.values;

/**
 * True of false value. Read-only or editable.
 */
public abstract class BooleanDBViewValue extends AbstractDBViewValue<Boolean>
{
	public BooleanDBViewValue(Boolean value, boolean readOnly)
	{
		super(DBViewValueType.BOOLEAN, value, readOnly);
	}
}