package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;
import org.pikater.shared.database.views.jirka.users.UsersTableDBView;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class UsersView extends DBTableLayout implements IContentComponent
{
	private static final long serialVersionUID = -6440037882833424701L;
	
	public UsersView()
	{
		super(new UsersTableDBView(), false);
		setSizeUndefined();
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
	public void dbViewActionCalled(final IColumn column, final AbstractTableRowDBView row, final NamedActionDBViewValue originalAction)
	{
		// the only action in this view is reset password for a user
		
		String targetUserLogin = ((StringDBViewValue) row.getValueWrapper(UsersTableDBView.Column.LOGIN)).getValue();
		MyDialogs.confirm(null, String.format("Really reset password for user '%s'?", targetUserLogin), new MyDialogs.IDialogResultHandler()
		{
			@Override
			public boolean handleResult(Object[] args)
			{
				UsersView.super.dbViewActionCalled(column, row, originalAction);
				return true;
			}
		});
	}
}