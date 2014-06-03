package org.pikater.shared.database.views.jirka.abstractview.values;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView.ColumnType;

/**
 * A single string value, read-only or editable.
 */
public abstract class StringDBViewValue extends AbstractDBViewValue<String>
{
	public StringDBViewValue(String value, boolean readOnly)
	{
		super(ColumnType.STRING, value, readOnly);
	}
}