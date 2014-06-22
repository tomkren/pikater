package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import org.pikater.shared.database.views.jirka.users.UsersTableDBView;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class UsersView extends DBTableLayout implements IContentComponent
{
	private static final long serialVersionUID = -6440037882833424701L;
	
	public UsersView()
	{
		super(new UsersTableDBView());
		setSizeUndefined();
		
		
		// TODO: a wizard to add datasets
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