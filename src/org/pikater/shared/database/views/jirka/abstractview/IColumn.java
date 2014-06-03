package org.pikater.shared.database.views.jirka.abstractview;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView.ColumnType;

public interface IColumn
{
	String getDisplayName();
	ColumnType getColumnType();
}
