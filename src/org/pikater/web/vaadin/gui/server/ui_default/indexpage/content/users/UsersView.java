package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.users;

import org.pikater.shared.database.views.tableview.users.UsersTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.UsersDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class UsersView extends DBTableLayout implements IContentComponent
{
	private static final long serialVersionUID = -6440037882833424701L;
	
	public UsersView()
	{
		super();
		setSizeUndefined();
		setWidth("100%");
	}
	
	//----------------------------------------------------
	// VIEW INTERFACE

	@Override
	public void enter(ViewChangeEvent event)
	{
		setView(new UsersDBViewRoot(new UsersTableDBView())); // required to be executed after initializing DB view
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