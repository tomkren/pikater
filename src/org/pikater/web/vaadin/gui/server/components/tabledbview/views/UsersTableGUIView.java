package org.pikater.web.vaadin.gui.server.components.tabledbview.views;

import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.users.UsersTableDBView;

public class UsersTableGUIView extends AbstractTableGUIView<UsersTableDBView>
{
	public UsersTableGUIView(AbstractTableDBView underlyingDBView)
	{
		super(underlyingDBView);
	}
	
	@Override
	public int getColumnSize(ITableColumn column)
	{
		UsersTableDBView.Column specificColumn = (UsersTableDBView.Column) column;
		switch(specificColumn)
		{
			case LOGIN:
				return 125;
			case EMAIL:
				return 200;
			case REGISTERED:
				return 100;
			case STATUS:
				return 100;
			case MAX_PRIORITY:
				return 100;
			case ADMIN:
				return 75;
			case RESET_PSWD:
				return 100;
			case DELETE:
				return 100;
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
		}
	}
}