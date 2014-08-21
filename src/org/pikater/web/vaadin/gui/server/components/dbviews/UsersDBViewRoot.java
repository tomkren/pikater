package org.pikater.web.vaadin.gui.server.components.dbviews;

import org.pikater.shared.database.views.base.values.StringDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.users.UsersTableDBView;
import org.pikater.shared.database.views.tableview.users.UsersTableDBView.Column;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;

import com.vaadin.ui.AbstractComponent;

public class UsersDBViewRoot extends AbstractDBViewRoot<UsersTableDBView>
{
	public UsersDBViewRoot(UsersTableDBView view)
	{
		super(view);
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
				return 75;
			
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
		}
	}
	
	@Override
	public ITableColumn getExpandColumn()
	{
		return UsersTableDBView.Column.EMAIL;
	}
	
	@Override
	public void onCellCreate(ITableColumn column, AbstractComponent component)
	{
		UsersTableDBView.Column specificColumn = (UsersTableDBView.Column) column;
		if(specificColumn == Column.MAX_PRIORITY)
		{
			component.setDescription("The higher, the better.");
		}
	}

	@Override
	public void approveAction(ITableColumn column, AbstractTableRowDBView row, final Runnable action)
	{
		final String targetUserLogin = ((StringDBViewValue) row.getValueWrapper(UsersTableDBView.Column.LOGIN)).getValue();
		
		UsersTableDBView.Column specificColumn = (UsersTableDBView.Column) column;
		if(specificColumn == UsersTableDBView.Column.RESET_PSWD)
		{
			GeneralDialogs.confirm(null, String.format("Really reset password for user '%s'?", targetUserLogin), new GeneralDialogs.IDialogResultHandler()
			{
				@Override
				public boolean handleResult(Object[] args)
				{
					action.run(); // approve
					return true;
				}
			});
		}
		else if(specificColumn == UsersTableDBView.Column.DELETE)
		{
			GeneralDialogs.confirm(null, String.format("Really delete account for user '%s'?", targetUserLogin), new GeneralDialogs.IDialogResultHandler()
			{
				@Override
				public boolean handleResult(Object[] args)
				{
					action.run(); // approve
					return true;
				}
			});
		}
		else
		{
			throw new IllegalStateException(String.format("Action '%s' has to be approved before being executed", specificColumn.name())); 
		}
	}
}
