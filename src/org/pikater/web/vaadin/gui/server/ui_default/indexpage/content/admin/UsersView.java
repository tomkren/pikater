package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import org.pikater.shared.database.views.jirka.users.UsersTableView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;
import org.pikater.web.vaadin.tabledbview.DBTableLayout;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class UsersView extends DBTableLayout implements IContentComponent
{
	private static final long serialVersionUID = -6440037882833424701L;
	
	public UsersView()
	{
		super(new UsersTableView(), UsersTableView.Column.LOGIN);
		setSizeUndefined();
		
		getTable().setColumnWidth(UsersTableView.Column.LOGIN, 150);
		getTable().setColumnWidth(UsersTableView.Column.EMAIL, 200);
		getTable().setColumnWidth(UsersTableView.Column.REGISTERED_AT, 100);
		getTable().setColumnWidth(UsersTableView.Column.ACCOUNT_STATUS, 100);
		getTable().setColumnWidth(UsersTableView.Column.MAXIMUM_PRIORITY, 100);
		getTable().setColumnWidth(UsersTableView.Column.RESET_PASSWORD, 150);
		
		// TODO:
		// this.table.setColumnExpandRatio(propertyId, expandRatio);
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
}