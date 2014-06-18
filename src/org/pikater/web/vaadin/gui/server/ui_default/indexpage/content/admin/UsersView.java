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
		
		// TODO: set column sizes
		// this.table.setColumnWidth(column, -1);
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