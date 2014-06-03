package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import org.pikater.shared.database.views.jirka.users.UsersTableView;
import org.pikater.web.vaadin.gui.server.tabledbview.TableDBContainer;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class UsersView extends VerticalLayout implements IContentComponent
{
	private static final long serialVersionUID = -6440037882833424701L;
	
	public UsersView()
	{
		super();
		setSizeFull();
		
		Table table_users = new Table();
		table_users.setSizeFull();
		table_users.setContainerDataSource(new TableDBContainer(table_users, new UsersTableView()));
		
		addComponent(table_users);
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
