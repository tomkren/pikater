package org.pikater.shared.database.views.jirka.abstractview.values;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView.ColumnType;

/**
 * True of false value. Read-only or editable.
 */
public abstract class BooleanDBViewValue extends AbstractDBViewValue<Boolean>
{
	public BooleanDBViewValue(Boolean value, boolean readOnly)
	{
		super(ColumnType.BOOLEAN, value, readOnly);
	}
}