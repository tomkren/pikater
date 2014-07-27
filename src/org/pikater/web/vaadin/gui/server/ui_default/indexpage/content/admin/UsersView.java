package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import org.pikater.shared.database.views.base.values.StringDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.users.UsersTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.IDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class UsersView extends DBTableLayout implements IContentComponent, IDBViewRoot<UsersTableDBView>
{
	private static final long serialVersionUID = -6440037882833424701L;
	
	private final UsersTableDBView dbView;
	
	public UsersView()
	{
		super();
		setSizeUndefined();
		
		this.dbView = new UsersTableDBView();
		setView(this); // required to be executed after initializing db view
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
	}

	@Override
	public boolean hasUnsavedProgress()
	{
		return false;
	}

	@Override
	public String getCloseDialogMessage()
	{
		return null;
	}
	
	@Override
	public UsersTableDBView getUnderlyingDBView()
	{
		return dbView;
	}

	@Override
	public void onCellCreate(ITableColumn column, Object component)
	{
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

	@Override
	public void approveAction(ITableColumn column, AbstractTableRowDBView row, final Runnable action)
	{
		String targetUserLogin = ((StringDBViewValue) row.getValueWrapper(UsersTableDBView.Column.LOGIN)).getValue();
		GeneralDialogs.confirm(null, String.format("Really reset password for user '%s'?", targetUserLogin), new GeneralDialogs.IDialogResultHandler()
		{
			@Override
			public boolean handleResult(Object[] args)
			{
				action.run();
				return true;
			}
		});
	}
}