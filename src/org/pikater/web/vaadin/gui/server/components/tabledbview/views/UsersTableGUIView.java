package org.pikater.web.vaadin.gui.server.components.tabledbview.views;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.users.UsersTableDBView;

public class UsersTableGUIView extends AbstractTableGUIView<UsersTableDBView>
{
	public UsersTableGUIView(AbstractTableDBView underlyingDBView)
	{
		super(underlyingDBView);
	}

	@Override
	public int getColumnSize(IColumn column)
	{
		UsersTableDBView.Column specificColumn = (UsersTableDBView.Column) column;
		switch(specificColumn)
		{
			case LOGIN:
				return 150;
			case EMAIL:
				return 200;
			case REGISTERED_AT:
				return 100;
			case ACCOUNT_STATUS:
				return 100;
			case MAXIMUM_PRIORITY:
				return 100;
			case RESET_PASSWORD:
				return 150;
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
		}
	}
}